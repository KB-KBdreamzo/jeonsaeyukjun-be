package com.jeonsaeyukjun.jeonsaeyukjunbe.report.service;

import com.jeonsaeyukjun.jeonsaeyukjunbe.report.Dto.RegisterDto;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final OpenAiService openAiService;

    public RegisterDto processPdf(MultipartFile file) throws IOException {

        String extractedText = extractTextFromPdf(file);

        StringBuilder[] sections = splitSections(extractedText);

        Map<String, Object> analysisResult = new HashMap<>();
        analysisResult.putAll(openAiService.textToJsonWithOpenAI(sections[0], "표제부"));
        analysisResult.putAll(openAiService.textToJsonWithOpenAI(sections[1], "갑구"));
        analysisResult.putAll(openAiService.textToJsonWithOpenAI(sections[2], "을구"));

        return new RegisterDto(
                (String) analysisResult.get("lessorName"),
                (String) analysisResult.get("roadName"),
                (String) analysisResult.get("detailAddress"),
                (String) analysisResult.get("landType"),
                (Double) analysisResult.get("landArea"),
                (String) analysisResult.get("buildingType"),
                (Double) analysisResult.get("buildingArea"),
                (Boolean) analysisResult.get("auctionRecord"),
                (Boolean) analysisResult.get("injuctionRecord"),
                (Boolean) analysisResult.get("trustRegistrationRecord"),
                (Boolean) analysisResult.get("redemptionRecord"),
                (Boolean) analysisResult.get("registrationRecord"),
                Integer.parseInt(analysisResult.get("seizureCount").toString()),
                Integer.parseInt(analysisResult.get("provisionalSeizureCount").toString()),
                Long.parseLong(analysisResult.get("priorityDeposit").toString()),
                Integer.parseInt(analysisResult.get("leaseholdRegistrationCount").toString()),
                Integer.parseInt(analysisResult.get("mortgageCount").toString())
        );
    }


    private String extractTextFromPdf(MultipartFile file) throws IOException {
        PDDocument document = PDDocument.load(file.getInputStream());
        PDFTextStripper pdfStripper = new PDFTextStripper();
        String text = pdfStripper.getText(document);
        document.close();
        return text;
    }

    private StringBuilder[] splitSections(String text) {
        StringBuilder[] sections = new StringBuilder[3];
        sections[0] = new StringBuilder();  // 표제부
        sections[1] = new StringBuilder();  // 갑구
        sections[2] = new StringBuilder();  // 을구

        int currentSectionIndex = 0;
        String[] lines = text.split("\n");

        for (String line : lines) {
            String trimmedLine = line.replaceAll("\\s+", "");

            if (trimmedLine.contains("【표제부】")) {
                currentSectionIndex = 0;
            } else if (trimmedLine.contains("【갑구】")) {
                currentSectionIndex = 1;
            } else if (trimmedLine.contains("【을구】")) {
                currentSectionIndex = 2;
            } else {
                sections[currentSectionIndex].append(line).append("\n");
            }
        }

        return sections;
    }
}
