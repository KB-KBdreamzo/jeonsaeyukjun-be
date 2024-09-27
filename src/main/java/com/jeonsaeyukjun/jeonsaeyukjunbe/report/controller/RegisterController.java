package com.jeonsaeyukjun.jeonsaeyukjunbe.report.controller;

import com.jeonsaeyukjun.jeonsaeyukjunbe.report.Dto.RegisterDto;
import com.jeonsaeyukjun.jeonsaeyukjunbe.report.service.RegisterService;
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

    @PostMapping("/upload-pdf")
    public ResponseEntity<RegisterDto> uploadRegisterFile(@RequestParam("file") MultipartFile file) {
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
