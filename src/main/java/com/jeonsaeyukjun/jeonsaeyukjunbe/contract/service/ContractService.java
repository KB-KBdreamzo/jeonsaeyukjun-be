package com.jeonsaeyukjun.jeonsaeyukjunbe.contract.service;

import com.jeonsaeyukjun.jeonsaeyukjunbe.contract.dto.ContractDTO;
import com.jeonsaeyukjun.jeonsaeyukjunbe.contract.mapper.ContractMapper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@Service
public class ContractService {

    private final ContractMapper contractMapper;

    public ContractService(ContractMapper contractMapper) {
        this.contractMapper = contractMapper;
    }

    public void generatePDF(ContractDTO contractDTO) throws IOException {
        File templateFile = new File("src/main/resources/contract.pdf");
        PDDocument document = PDDocument.load(templateFile);

        // 텍스트 추출
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(document);
        System.out.println("Original Text: " + text); // 원본 텍스트 출력

        // U+2027 문자 전처리
        String processedText = text.replace("\u2027", "/"); // 필요에 따라 대체 문자 지정
        // U+0178 문자 대체
        text = text.replace("\u0178", " "); // 공백으로 대체
        text = text.replace("\u2024", " "); // U+2024 대체
        text = text.replace("\uF000", ""); // U+F000 제거
        text = text.replace("\uFF62", " "); // U+FF62 제거
        text = text.replace("\uFF63", " "); // U+FF63 제거
        text = text.replace("\uFF65", " "); // U+FF65 제거
        System.out.println("Processed Text: " + processedText); // 처리된 텍스트 출력

        // ContractDTO를 맵으로 변환
        Map<String, String> placeholders = contractMapper.mapToTemplate(contractDTO);

        // 플레이스 홀더 대체
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue() != null ? entry.getValue() : ""; // null 체크만 수행
            System.out.println("Replacing: " + key + " with: " + value); // 디버깅 출력
            text = text.replace(key, value);
        }

        // 최종 텍스트에서 U+2027 제거
        // U+0178 문자 대체
        text = text.replace("\u0178", " "); // 공백으로 대체
        text = text.replace("\u2024", " "); // U+2024 대체
        text = text.replace("\uF000", ""); // U+F000 제거
        text = text.replace("\u2027", "/"); // 대체 문자 지정
        text = text.replace("\uFF62", " "); // U+FF62 제거
        text = text.replace("\uFF63", " "); // U+FF63 제거
        text = text.replace("\uFF65", " "); // U+FF65 제거

        // 줄 바꿈 문자를 기준으로 텍스트 분할
        String[] lines = text.split("\r\n|\r|\n");

        // 페이지 작성
        PDPage page = document.getPage(0); // 첫 번째 페이지를 가져옵니다.
        PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.OVERWRITE, false);

        contentStream.beginText();
        // 사용자 정의 폰트 설정
        contentStream.setFont(PDType0Font.load(document, Files.newInputStream(Paths.get("src/main/resources/fonts/NotoSansKR-Regular.ttf"))), 12);

        // 수정된 텍스트로 PDF 작성
        contentStream.newLineAtOffset(20, 20); // 텍스트가 출력될 위치 설정
        //contentStream.showText(text);
        float leading = 14.5f; // 줄 간격 설정
        for (String line : lines) {
            if (line != null && !line.trim().isEmpty()) { // 빈 줄 체크
                contentStream.showText(line);
                contentStream.newLineAtOffset(0, -leading); // 줄 바꿈
            }
        }
        contentStream.endText();
        contentStream.close();

        // 새로운 PDF 저장
        document.save("src/main/resources/resulted_contract.pdf");
        document.close();
    }
}













//             contentStream.setFont(PDType0Font.load(document, Files.newInputStream(Paths.get("src/main/resources/fonts/NotoSansKR-Black.ttf"))), 12);




































//import com.jeonsaeyukjun.jeonsaeyukjunbe.contract.dto.ContractDTO;
//import com.jeonsaeyukjun.jeonsaeyukjunbe.contract.mapper.ContractMapper;
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.pdmodel.PDPage;
//import org.apache.pdfbox.pdmodel.PDPageContentStream;
//import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
//import org.apache.pdfbox.pdmodel.font.PDType0Font;
//import org.apache.pdfbox.pdmodel.font.PDType1Font;
//import org.apache.pdfbox.pdmodel.font.encoding.Encoding;
//import org.apache.pdfbox.pdmodel.font.encoding.StandardEncoding;
//import org.apache.pdfbox.text.PDFTextStripper;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.Map;
//
//@Service
//public class ContractService {
//
//    private final ContractMapper contractMapper;
//    //private Object StandardCharsets;
//
//    public ContractService(ContractMapper contractMapper) {
//        this.contractMapper = contractMapper;
//    }
//
//    public void generatePDF(ContractDTO contractDTO) throws IOException {
//        // DTO -> Template로 데이터 매핑
//        Map<String, String> contractData = contractMapper.mapToTemplate(contractDTO);
//
//        // PDF 생성
//        //PDDocument document = PDDocument.load(Files.newInputStream(Paths.get("test_contract.pdf")));
//        PDDocument document = PDDocument.load(Files.newInputStream(Paths.get("src/main/resources/test_contract.pdf")));
//        PDPage page = document.getPage(0);
//
//        // 기존 내용을 유지하며 새로운 ContentStream 생성
//        PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);
//
//        // 임시로 텍스트 추가
//        contentStream.beginText();
//        try (InputStream fontStream = Files.newInputStream(Paths.get("src/main/resources/fonts/NotoSansKR-Black.ttf"))) {
//            contentStream.setFont(PDType0Font.load(document, fontStream), 12);
//            contentStream.newLineAtOffset(100, 700); // 텍스트 시작 위치 설정
//
//            // 매핑된 데이터 텍스트로 추가
//            contentStream.showText("소재지: " + contractData.get("{{address}}"));
//            contentStream.newLineAtOffset(0, -20);
//            contentStream.showText("임차인 이름: " + contractData.get("{{tenantName}}"));
//            contentStream.newLineAtOffset(0, -20);
//            contentStream.showText("임대인 이름: " + contractData.get("{{landlordName}}"));
//            contentStream.newLineAtOffset(0, -20);
//            contentStream.showText("임대료: " + contractData.get("{{rentalAmount}}"));
//            contentStream.endText(); // 텍스트 끝
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new RuntimeException("폰트를 로드하는 데 실패했습니다.", e);
//        } finally {
//            contentStream.close(); // 항상 스트림을 닫기
//        }
//
//        // PDF 저장
//        //Path outputPath = Paths.get("generated_contract.pdf");
//        Path outputPath = Paths.get("src/main/resources/generated_contract.pdf");
//        //Path outputPath = Paths.get(System.getProperty("user.home"), "generated_contract.pdf");
//        document.save(outputPath.toFile());
//        document.close();
//    }
//}
