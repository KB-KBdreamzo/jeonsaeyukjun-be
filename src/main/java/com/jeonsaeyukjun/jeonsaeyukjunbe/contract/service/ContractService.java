package com.jeonsaeyukjun.jeonsaeyukjunbe.contract.service;

import com.jeonsaeyukjun.jeonsaeyukjunbe.contract.dto.ContractDTO;
import com.jeonsaeyukjun.jeonsaeyukjunbe.contract.mapper.ContractMapper;
import org.apache.pdfbox.pdmodel.PDDocument; // PDF 문서를 생성하고 수정하기 위한 클래스
import org.apache.pdfbox.pdmodel.PDPage; // PDF 페이지를 생성하고 조작하기 위한 클래스
import org.apache.pdfbox.pdmodel.PDPageContentStream; // PDF 페이지에 텍스트 및 그래픽을 추가하기 위한 클래스
import org.apache.pdfbox.pdmodel.font.PDType0Font; // PDF에서 사용할 수 있는 폰트 클래스
import org.apache.pdfbox.text.PDFTextStripper; // PDF에서 텍스트를 추출하기 위한 클래스
import org.springframework.stereotype.Service; // Spring의 서비스 클래스임을 표시하는 어노테이션

import java.io.File; // 파일을 다루기 위한 클래스
import java.io.IOException; // I/O 예외 처리를 위한 클래스
import java.nio.file.Files; // 파일 작업을 위한 클래스
import java.nio.file.Paths; // 파일 경로를 다루기 위한 클래스
import java.time.LocalDate; // 날짜를 다루기 위한 클래스
import java.util.Map; // 키-값 쌍으로 데이터를 저장하는 맵 인터페이스
import java.util.regex.Pattern; // 정규 표현식을 다루기 위한 클래스

@Service
public class ContractService {

    private final ContractMapper contractMapper;

    // 생성자: ContractMapper를 주입받아 초기화
    public ContractService(ContractMapper contractMapper) {
        this.contractMapper = contractMapper;
    }

    // 계약서 PDF를 생성하는 메서드
    public void generatePDF(ContractDTO contractDTO) throws IOException {
        File templateFile = new File("src/main/resources/standard_contract.pdf"); // PDF 템플릿 파일 로드
        PDDocument frameDocument = PDDocument.load(templateFile); // 표준 계약서를 표현하는 PDDocument 객체 생성
        PDDocument document = new PDDocument(); // 최종 계약서를 표현하는 PDDocument 객체 생성

        // 계약서 작성 날짜 삽입
        LocalDate today = LocalDate.now(); // 현재 날짜 가져오기
        contractDTO.setTodayYear(today.getYear()); // 연도 설정
        contractDTO.setTodayMonth(today.getMonthValue()); // 월 설정
        contractDTO.setTodayDay(today.getDayOfMonth()); // 일 설정

        // 텍스트 추출
        PDFTextStripper stripper = new PDFTextStripper(); // PDF에서 텍스트를 추출하기 위한 객체 생성
        String text = stripper.getText(frameDocument); // 원본 텍스트 추출

        // ContractDTO를 맵으로 변환
        Map<String, String> placeholders = contractMapper.mapToTemplate(contractDTO);

        // 플레이스 홀더 대체
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            String key = entry.getKey(); // 플레이스 홀더 키
            String value = entry.getValue() != null ? entry.getValue() : ""; // null 체크만 수행
            text = text.replace(key, value); // 플레이스 홀더를 실제 값으로 대체
        }

        // 폰트에 없는 문자 전처리
        text = text.replace("\u0178", " "); // 트레마가 붙은 Y'Ÿ'를 공백으로 대체
        text = text.replace("\u2024", "."); // 단일 점 문자'․'를 마침표'.'로 대체
        text = text.replace("\u2027", "/"); // 작은 중간 점'‧ '을 슬래시'/'로 대체
        text = text.replace("\uF000", ""); // 제어 문자를 제거
        text = text.replace("\uFF62", " "); // 반각 왼쪽 괄호'｢'를 공백으로 대체
        text = text.replace("\uFF63", " "); // 반각 오른쪽 괄호'｣'를 공백으로 대체
        text = text.replace("\uFF65", "/"); // 중간 점'･'을 슬래시'/'으 대체

        // 줄 바꿈 문자를 기준으로 텍스트 분할
        String[] lines = text.split("\r\n|\r|\n");

        float leading = 14.5f; // 줄 간격 설정
        float startX = 20; // X 좌표 설정
        float startY = 750; // Y 좌표 설정 (페이지의 높이에서 여백을 고려한 값)
        float margin = 130; // 페이지 여백
        PDPageContentStream contentStream = null; // 페이지 내용 스트림 초기화

        // 제n조 패턴 정의 (ex: 제1조, 제2조 등)
        Pattern sectionPattern = Pattern.compile("제\\d+조");

        PDPage page = null; // 페이지 객체 초기화
        PDType0Font font = PDType0Font.load(document, Files.newInputStream(Paths.get("src/main/resources/fonts/NotoSansKR-Regular.ttf"))); // 사용자 정의 폰트 로드
        // 텍스트를 줄 단위로 PDF에 추가
        for (int i = 0; i < lines.length; i++) {

            // 페이지 하단에 도달하면 다음 페이지로 넘어가도록 처리
            if (i == 0 || startY <= margin) { // 여백 이하로 내려가면
                if (contentStream != null) {
                    contentStream.endText(); // 텍스트 추가 종료
                    contentStream.close(); // 내용 스트림 종료
                }

                // 새로운 페이지 생성 및 시작
                page = new PDPage(); // 새로운 페이지 생성
                document.addPage(page); // 문서에 페이지 추가
                contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.OVERWRITE, true); // 새로운 내용 스트림 생성

                contentStream.beginText(); // 텍스트 추가 시작
                contentStream.setFont(font, 12); // 폰트 설정
                startY = 750; // 새 페이지의 시작 Y 좌표 설정
                contentStream.newLineAtOffset(startX, startY); // 텍스트 시작 위치 설정
            }

            String line = lines[i]; // 현재 줄 가져오기
            if (line != null && !line.trim().isEmpty()) { // 빈 줄 체크
                // 대괄호 또는 줄 바꿈 문자, 제n조 앞에서 한 줄 띄우기
                if (line.contains("[") || line.contains("]") || sectionPattern.matcher(line).find()) {
                    contentStream.newLineAtOffset(0, -leading); // 한 줄 띄우기
                }

                // "주택임대차표준계약서" 라인 처리
                if (line.contains("주택임대차표준계약서")) {
                    float pageWidth = page.getMediaBox().getWidth(); // 페이지 너비 가져오기
                    float textWidth = PDType0Font.load(document, Files.newInputStream((Paths.get("src/main/resources/fonts/NotoSansKR-Regular.ttf")))).getStringWidth(line) / 1000 * 20; // 텍스트 너비 계산
                    float tempX = (pageWidth - textWidth) / 2; // 중앙 정렬을 위한 x 좌표 계산

                    contentStream.setFont(font, 20); // 큰 폰트로 설정
                    contentStream.newLineAtOffset(tempX, 0); // 텍스트 위치 이동
                    contentStream.showText(line); // 텍스트 추가
                    contentStream.newLineAtOffset(tempX * (-1), -leading); // 줄 바꿈
                    startY -= leading; // Y 좌표 감소
                    contentStream.newLineAtOffset(0, -leading); // 줄 바꿈
                    startY -= leading; // Y 좌표 감소
                    contentStream.setFont(font, 12); // 원래 폰트 크기로 복귀
                }
                else {
                    contentStream.showText(line); // 텍스트 추가
                    contentStream.newLineAtOffset(0, -leading); // 줄 바꿈
                    startY -= leading; // Y 좌표 감소
                }
            }
        }

        // 마지막 스트림 종료 및 PDF 저장
        if (contentStream != null) {
            contentStream.endText(); // 텍스트 추가 종료
            contentStream.close(); // 내용 스트림 종료
        }

        // 새로운 PDF 저장
        document.save("src/main/resources/resulted_contract.pdf");
        document.close();
    }
}