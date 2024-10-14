package com.jeonsaeyukjun.jeonsaeyukjunbe.contract.service;

import com.jeonsaeyukjun.jeonsaeyukjunbe.contract.dto.ContractDto;
import com.jeonsaeyukjun.jeonsaeyukjunbe.contract.dto.OwnershipInfoDto;
import com.jeonsaeyukjun.jeonsaeyukjunbe.contract.mapper.ContractMapper;
import com.jeonsaeyukjun.jeonsaeyukjunbe.contract.dto.SpecialContractDto;
import com.jeonsaeyukjun.jeonsaeyukjunbe.contract.mapper.OwnershipMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.pdfbox.pdmodel.PDDocument; // PDF 문서를 생성하고 수정하기 위한 클래스
import org.apache.pdfbox.pdmodel.PDPage; // PDF 페이지를 생성하고 조작하기 위한 클래스
import org.apache.pdfbox.pdmodel.PDPageContentStream; // PDF 페이지에 텍스트 및 그래픽을 추가하기 위한 클래스
import org.apache.pdfbox.pdmodel.font.PDType0Font; // PDF에서 사용할 수 있는 폰트 클래스
import org.apache.pdfbox.text.PDFTextStripper; // PDF에서 텍스트를 추출하기 위한 클래스

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File; // 파일을 다루기 위한 클래스
import java.io.IOException; // I/O 예외 처리를 위한 클래스
import java.nio.file.Files; // 파일 작업을 위한 클래스
import java.time.LocalDate; // 날짜를 다루기 위한 클래스
import java.util.HashMap;
import java.util.Map; // 키-값 쌍으로 데이터를 저장하는 맵 인터페이스
import java.util.regex.Pattern; // 정규 표현식을 다루기 위한 클래스
import java.util.List;

@Service
public class ContractService {

    @Autowired
    private final ContractMapper contractMapper;

    @Autowired
    private OwnershipMapper ownershipMapper;

    @Autowired
    private S3Service s3Service;

    public void addContractDto(ContractDto contractDto) {
        if (fetchContract(contractDto) == null) {
            contractMapper.addContract(contractDto);
        }
    }

    public ContractDto fetchContract(ContractDto contractDto) {
        return contractMapper.fetchContract(contractDto);
    }

    public List<SpecialContractDto> fetchSpecialContract(OwnershipInfoDto ownershipInfoDto) {
        return contractMapper.fetchSpecialContracts(ownershipInfoDto);
    }


    @Autowired
    public ContractService(ContractMapper contractMapper) {
        this.contractMapper = contractMapper;
    }

    // 계약서 PDF를 생성하는 메서드
    public void generatePDF(ContractDto contractDTO, OwnershipInfoDto ownershipInfoDto) throws IOException {
        try {
            List<SpecialContractDto> specialContracts = null;

            // OwnershipInfoDto가 null이 아니면 SpecialContract를 가져옴
            if (ownershipInfoDto != null) {
                specialContracts = fetchSpecialContract(ownershipInfoDto);
                System.out.println("SpecialContracts: " + specialContracts);
            }
            // 클래스패스에서 리소스 파일을 불러오는 방법
            ClassLoader classLoader = getClass().getClassLoader();
            File templateFile = new File(classLoader.getResource("standard_contract.pdf").getFile());

            PDDocument frameDocument = PDDocument.load(templateFile); // 표준 계약서를 표현하는 PDDocument 객체 생성
            PDDocument document = new PDDocument(); // 최종 계약서를 표현하는 PDDocument 객체 생성

            // 계약서 작성 날짜 삽입
            LocalDate today = LocalDate.now();
            contractDTO.setTodayYear(today.getYear());
            contractDTO.setTodayMonth(today.getMonthValue());
            contractDTO.setTodayDay(today.getDayOfMonth());

            // 텍스트 추출
            PDFTextStripper stripper = new PDFTextStripper(); // PDF에서 텍스트를 추출하기 위한 객체 생성
            String text = stripper.getText(frameDocument); // 원본 텍스트 추출

            // ContractDTO를 맵으로 변환
            Map<String, String> placeholders = mapToTemplate(contractDTO);

            // 플레이스 홀더 대체
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                String key = entry.getKey(); // 플레이스 홀더 키
                String value = entry.getValue() != null ? entry.getValue() : ""; // null 체크만 수행
                text = text.replace(key, value); // 플레이스 홀더를 실제 값으로 대체
            }

            // MySQL 데이터 삽입 (예: 특정 플레이스 홀더에 데이터 추가)
            System.out.println("==========================================================================");

            if(specialContracts == null) {
                text = text.replace("{{specialContract}}", " "); // 첫 번째 계약 내용 대체
            }
            else if (specialContracts != null && !specialContracts.isEmpty()){
                // 첫 번째 플레이스홀더 대체
                text = text.replace("{{specialContract}}", "- " + specialContracts.get(0).getContent() + "\n"); // 첫 번째 계약 내용 대체

                // 두 번째 줄부터는 기존 내용 뒤에 추가
                for (int i = 1; i < specialContracts.size(); i++) {
                    int index = text.indexOf(specialContracts.get(i - 1).getContent());

                    if (index != -1) { // 내용이 존재하는 경우
                        // 두 번째 계약 내용의 끝 인덱스 계산
                        int endIndex = index + specialContracts.get(i - 1).getContent().length();

                        // 두 번째 계약 내용 이전과 이후의 문자열 분리
                        String before = text.substring(0, endIndex); // 두 번째 계약 내용까지
                        String after = text.substring(endIndex); // 그 다음의 내용
                        System.out.println(before);
                        System.out.println();
                        System.out.println(after);

                        // 원하는 내용 추가
                        SpecialContractDto contract = specialContracts.get(i);
                        System.out.println("content: " + contract.getContent());
                        //String additionalContent = contract.getContent() + "\n "; // 추가할 내용
                        String newText = before + "\n" + "- " + contract.getContent() + "\n" + after; // 두 번째 계약 내용 뒤에 추가
                        System.out.println(newText);

                        // text를 새로운 내용으로 업데이트
                        text = newText;
                        System.out.println(text);
                    }
                }

            }

            System.out.println("==========================================================================");

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
            float startX = 60; // X 좌표 설정
            float startY = 750; // Y 좌표 설정 (페이지의 높이에서 여백을 고려한 값)
            float margin = 430; // 페이지 여백
            PDPageContentStream contentStream = null; // 페이지 내용 스트림 초기화

            // 제n조 패턴 정의 (ex: 제1조, 제2조 등)
            Pattern sectionPattern = Pattern.compile("제\\d+조");

            PDPage page = null; // 페이지 객체 초기화

            // ClassLoader를 사용해 리소스 파일 접근
            File fontFile = new File(classLoader.getResource("fonts/NotoSansKR-Regular.ttf").getFile());
            PDType0Font font = PDType0Font.load(document, Files.newInputStream(fontFile.toPath()));

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
                        drawCenteredTextWithFontSize(contentStream, page, document, "주택임대차표준계약서", font, 20, leading, startX, startY);
                    } else {
                        drawTextWithLineBreakIfNeeded(contentStream, page, document, line, font, 12, 520, leading, startX, startY);
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

            // 메모리에 저장하기 위한 ByteArrayOutputStream 사용
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            document.save(byteArrayOutputStream);  // 메모리에 PDF 저장
            document.close();

            // ByteArrayInputStream을 사용하여 S3에 업로드
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            String pdfFileName = System.currentTimeMillis() + ".pdf";
            long contentLength = byteArrayOutputStream.size();

            Integer reportId = (ownershipInfoDto != null) ? ownershipInfoDto.getReportId() : null;
            String fileUrl = s3Service.uploadFileAndSaveToDb(byteArrayInputStream, pdfFileName, contentLength, reportId);
            System.out.println("PDF 파일이 S3에 업로드되었습니다: " + fileUrl);

        } catch (IOException e) {
            e.printStackTrace(); // 스택 트레이스를 출력하여 자세한 오류 정보 확인
            // 필요시 사용자에게 에러 메시지를 전달하거나 추가 처리를 수행
        }
    }

    public Map<String, String> mapToTemplate(ContractDto dto) {
        Map<String, String> data = new HashMap<>();

        // 임대인 이름
        data.put("{{landlordName}}", dto.getLandlordName() != null ? dto.getLandlordName() : "");
        // 임차인 이름
        data.put("{{tenantName}}", dto.getTenantName() != null ? dto.getTenantName() : "");
        // 계약 주소
        data.put("{{address}}", dto.getAddress() != null ? dto.getAddress() : "");
        // 토지 지목
        data.put("{{landType}}", dto.getLandType() != null ? dto.getLandType() : "");
        // 토지 면적
        data.put("{{landArea}}", String.valueOf(dto.getLandArea()));
        // 건물 구조 및 용도
        data.put("{{structurePurpose}}", dto.getStructurePurpose() != null ? dto.getStructurePurpose() : "");
        // 건물 면적
        data.put("{{buildingArea}}", String.valueOf(dto.getBuildingArea()));
        // 임차할 부분
        data.put("{{leasedPortion}}", dto.getLeasedPortion() != null ? dto.getLeasedPortion() : "");
        // 임차할 부분 면적
        data.put("{{leasedPortionArea}}", String.valueOf(dto.getLeasedPortionArea()));
        // 계약 종류
        data.put("{{contractType}}", dto.getContractType() != null ? dto.getContractType() : "");

        // 계약 시작일
        data.put("{{lsSY}}", String.valueOf(dto.getLsSY()));
        data.put("{{lsSM}}", String.valueOf(dto.getLsSM()));
        data.put("{{lsSD}}", String.valueOf(dto.getLsSD()));

        // 계약 종료일
        data.put("{{lsEY}}", String.valueOf(dto.getLsEY()));
        data.put("{{lsEM}}", String.valueOf(dto.getLsEM()));
        data.put("{{lsED}}", String.valueOf(dto.getLsED()));

        // 미납 세금 내역
        data.put("{{unpaidNationalAndLocalTax}}", dto.getUnpaidNationalAndLocalTax() != null ? dto.getUnpaidNationalAndLocalTax() : "");
        // 선순위 확정일자 내역
        data.put("{{priorityConfirmedDateDetails}}", dto.getPriorityConfirmedDateDetails() != null ? dto.getPriorityConfirmedDateDetails() : "");
        // 계약 확정일자 부여일
        data.put("{{contractConfirmationDate}}", dto.getContractConfirmationDate() != null ? dto.getContractConfirmationDate() : "");
        // 입금 계좌 정보
        data.put("{{paymentAccount}}", dto.getPaymentAccount() != null ? dto.getPaymentAccount() : "");
        // 보증금
        data.put("{{depositAmount}}", String.valueOf(dto.getDepositAmount()));
        // 계약금
        data.put("{{downPayment}}", String.valueOf(dto.getDownPayment()));
        // 중도금
        data.put("{{interimPayment}}", String.valueOf(dto.getInterimPayment()));
        // 잔금
        data.put("{{finalPayment}}", String.valueOf(dto.getFinalPayment()));

        // 중도금 지급일
        data.put("{{interYear}}", String.valueOf(dto.getInterYear()));
        data.put("{{interMonth}}", String.valueOf(dto.getInterMonth()));
        data.put("{{interDay}}", String.valueOf(dto.getInterDay()));

        // 잔금 지급일
        data.put("{{finalYear}}", String.valueOf(dto.getFinalYear()));
        data.put("{{finalMonth}}", String.valueOf(dto.getFinalMonth()));
        data.put("{{finalDay}}", String.valueOf(dto.getFinalDay()));

        // 관리비
        data.put("{{managementFee}}", String.valueOf(dto.getManagementFee()));
        // 수리 필요 사항
        data.put("{{repairDetails}}", dto.getRepairDetails() != null ? dto.getRepairDetails() : "");
        data.put("{{taxAmount}}", String.valueOf(dto.getDepositAmount() / 10));

        // 임대인 정보
        data.put("{{landlordAddress}}", dto.getLandlordAddress() != null ? dto.getLandlordAddress() : "");
        data.put("{{landlordResidentId}}", dto.getLandlordResidentId() != null ? dto.getLandlordResidentId() : "");
        data.put("{{landlordPhone}}", dto.getLandlordPhone() != null ? dto.getLandlordPhone() : "");

        // 임차인 정보
        data.put("{{tenantAddress}}", dto.getTenantAddress() != null ? dto.getTenantAddress() : "");
        data.put("{{tenantResidentId}}", dto.getTenantResidentId() != null ? dto.getTenantResidentId() : "");
        data.put("{{tenantPhone}}", dto.getTenantPhone() != null ? dto.getTenantPhone() : "");

        // 오늘 날짜
        data.put("{{todayYear}}", String.valueOf(dto.getTodayYear()));
        data.put("{{todayMonth}}", String.valueOf(dto.getTodayMonth()));
        data.put("{{todayDay}}", String.valueOf(dto.getTodayDay()));

        return data;
    }

    // 해당 line 가운데 정렬
    public void drawCenteredTextWithFontSize(PDPageContentStream contentStream, PDPage page, PDDocument document, String line, PDType0Font font, float fontSize, float leading, float startX, float startY) throws IOException {
        // 페이지 너비 가져오기
        float pageWidth = page.getMediaBox().getWidth();

        // 텍스트 너비 계산 (글자 너비를 폰트 크기에 맞게 계산)
        float textWidth = font.getStringWidth(line) / 1000 * fontSize;

        // 중앙 정렬을 위한 x 좌표 계산
        float tempX = (pageWidth - textWidth) / 2;

        // 폰트 설정 및 텍스트 위치 이동
        contentStream.setFont(font, fontSize);
        contentStream.newLineAtOffset(tempX - startX, 0);

        // 텍스트 추가
        contentStream.showText(line);

        // 줄 바꿈 및 Y 좌표 이동
        contentStream.newLineAtOffset(-tempX, -leading); // X 좌표 원상복구, Y 좌표 줄 바꿈
        startY -= leading;

        // 줄 바꿈 및 Y 좌표 이동
        contentStream.newLineAtOffset(startX, -leading);
        startY -= leading;
    }

    //
    public void drawTextWithLineBreakIfNeeded(PDPageContentStream contentStream, PDPage page, PDDocument document, String line, PDType0Font font, float fontSize, float maxWidth, float leading, float startX, float startY) throws IOException {
        // 페이지 너비 가져오기
        float pageWidth = page.getMediaBox().getWidth();

        // 현재 라인의 너비 계산
        float textWidth = font.getStringWidth(line) / 1000 * fontSize;

        // 만약 현재 라인이 maxWidth를 넘는 경우에만 줄바꿈 처리
        if (textWidth > maxWidth) {
            // 줄바꿈 처리를 위한 로직 호출
            StringBuilder currentLine = new StringBuilder();
            String[] words = line.split(" "); // 공백으로 단어를 나눔
            float currentLineWidth = 0;

            for (String word : words) {
                // 단어의 너비 계산
                float wordWidth = font.getStringWidth(word + " ") / 1000 * fontSize;

                // 현재 줄에 단어를 추가해도 너비가 maxWidth를 넘지 않으면 추가
                if (currentLineWidth + wordWidth < maxWidth) {
                    currentLine.append(word).append(" ");
                    currentLineWidth += wordWidth;
                } else {
                    // 너비를 초과한 경우, 현재 줄을 출력하고 새 줄로 시작
                    float tempTextWidth = font.getStringWidth(currentLine.toString()) / 1000 * fontSize;
                    contentStream.setFont(font, fontSize);
                    contentStream.newLineAtOffset(0, 0);
                    contentStream.showText(currentLine.toString().trim());

                    // 줄바꿈 처리
                    contentStream.newLineAtOffset(0, -leading);
                    startY -= leading;

                    // 새로운 줄 시작
                    currentLine = new StringBuilder(word + " ");
                    currentLineWidth = wordWidth;
                }
            }

            // 마지막 줄 출력
            if (currentLine.length() > 0) {
                float tempTextWidth = font.getStringWidth(currentLine.toString()) / 1000 * fontSize;
                contentStream.newLineAtOffset(0, 0);
                contentStream.showText(currentLine.toString().trim());
            }

            // 마지막 줄바꿈 처리
            contentStream.newLineAtOffset(0, -leading);
            startY -= leading;
        } else {
            // 너비가 450 이하일 경우 그대로 출력
            float tempX = (pageWidth - textWidth) / 2;
            contentStream.setFont(font, fontSize);
            contentStream.showText(line); // 텍스트 출력
            contentStream.newLineAtOffset(0, -leading); // 줄바꿈
            startY -= leading; // Y 좌표 이동
        }
    }

    // OwnershipInfo 조회 메소드
    public OwnershipInfoDto getOwnershipInfo(int reportId) {
        OwnershipInfoDto ownershipInfoDto = ownershipMapper.getOwnershipInfoByReportId(reportId);
        System.out.println("OwnershipInfoDto: " + ownershipInfoDto);
        return ownershipInfoDto;
    }
}