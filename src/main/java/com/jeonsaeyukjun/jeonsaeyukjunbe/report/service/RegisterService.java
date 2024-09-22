package com.jeonsaeyukjun.jeonsaeyukjunbe.report.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class RegisterService {
    public Map<String, Object> processPdf(MultipartFile file) throws IOException {

        String extractedText = extractTextFromPdf(file);
        System.out.print(extractedText);

        //OpenAI API 및 로직 사용하여 텍스트 분석
        Map<String, String> analysisResult;

        Map<String, Object> response = new HashMap<>();
        response.put("extractedText", extractedText);

        return response;
    }

    private String extractTextFromPdf(MultipartFile file) throws IOException {
        PDDocument document = PDDocument.load(file.getInputStream());
        PDFTextStripper pdfStripper = new PDFTextStripper();
        String text = pdfStripper.getText(document);
        document.close();
        return text;
    }

    // 텍스트 분석
}
