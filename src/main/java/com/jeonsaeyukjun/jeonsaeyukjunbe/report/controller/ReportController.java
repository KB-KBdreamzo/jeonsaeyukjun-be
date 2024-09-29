package com.jeonsaeyukjun.jeonsaeyukjunbe.report.controller;

import com.jeonsaeyukjun.jeonsaeyukjunbe.report.Dto.RegisterDto;
import com.jeonsaeyukjun.jeonsaeyukjunbe.report.Dto.ReportResponseDto;
import com.jeonsaeyukjun.jeonsaeyukjunbe.report.Dto.ReportRequestDto;
import com.jeonsaeyukjun.jeonsaeyukjunbe.report.service.RegisterService;
import com.jeonsaeyukjun.jeonsaeyukjunbe.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/report")
public class ReportController {

    private final RegisterService registerService;
    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<?> createReport(@RequestBody ReportRequestDto reportRequest) {
        try {
            Long deposit = reportRequest.getDeposit();
            String legalCode = reportRequest.getLegalCode();
            String jbAddress = reportRequest.getJbAddress();
            RegisterDto registerDto = reportRequest.getRegisterDto();

            ReportResponseDto report = reportService.generateReport(registerDto, legalCode, jbAddress, deposit) ;

            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "서버 내부 오류: " + e.getMessage()));

        }
    }

    @PostMapping("/upload-pdf")
    public ResponseEntity<?> getRegisterInformation(@RequestParam("file") MultipartFile file) {
        try {
            RegisterDto analysisResult = registerService.processPdf(file);
            return ResponseEntity.ok(analysisResult);
        } catch (IOException e){
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "파일 처리 중 오류 발생: " + e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.unprocessableEntity()
                    .body(Map.of("message", "서버 내부 오류: " + e.getMessage()));

        }
    }

}
