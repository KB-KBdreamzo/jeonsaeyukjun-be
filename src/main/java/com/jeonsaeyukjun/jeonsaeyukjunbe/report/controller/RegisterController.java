package com.jeonsaeyukjun.jeonsaeyukjunbe.report.controller;

import com.jeonsaeyukjun.jeonsaeyukjunbe.report.Dto.RegisterDto;
import com.jeonsaeyukjun.jeonsaeyukjunbe.report.Dto.ReportResponseDto;
import com.jeonsaeyukjun.jeonsaeyukjunbe.report.Dto.ReportRequestDto;
import com.jeonsaeyukjun.jeonsaeyukjunbe.report.service.RegisterService;
import com.jeonsaeyukjun.jeonsaeyukjunbe.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/report")
public class RegisterController {

    private final RegisterService registerService;
    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<ReportResponseDto> createReport(@RequestBody ReportRequestDto reportRequest) {
        try {
            Long deposit = Long.valueOf(reportRequest.getDeposit());
            Long legalCode = Long.valueOf(reportRequest.getLegalCode());
            String jibunAddress = reportRequest.getJbAddress();
            RegisterDto registerDto = reportRequest.getRegisterDto();

            ReportResponseDto report = reportService.addReport(registerDto, legalCode, jibunAddress, deposit) ;

            return new ResponseEntity<>(report, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return new ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/upload-pdf")
    public ResponseEntity<RegisterDto> getRegisterInformation(@RequestParam("file") MultipartFile file) {
        try {
            RegisterDto analysisResult = registerService.processPdf(file);
            return new ResponseEntity<>(analysisResult, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return new ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
