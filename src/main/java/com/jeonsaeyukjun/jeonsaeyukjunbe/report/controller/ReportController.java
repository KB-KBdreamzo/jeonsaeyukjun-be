package com.jeonsaeyukjun.jeonsaeyukjunbe.report.controller;

import com.jeonsaeyukjun.jeonsaeyukjunbe.report.Dto.RegisterDto;
import com.jeonsaeyukjun.jeonsaeyukjunbe.report.Dto.ReportRequestDto;
import com.jeonsaeyukjun.jeonsaeyukjunbe.report.Dto.ReportResponseDto;
import com.jeonsaeyukjun.jeonsaeyukjunbe.report.service.RegisterService;
import com.jeonsaeyukjun.jeonsaeyukjunbe.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/{userId}")
    public ResponseEntity<?> addReport(@PathVariable int userId, @RequestBody ReportRequestDto reportRequest) {
        try {
            Long deposit = reportRequest.getDeposit();
            String legalCode = reportRequest.getLegalCode();
            String jbAddress = reportRequest.getJbAddress();
            String detailAddress = reportRequest.getDetailAddress();
            RegisterDto registerDto = reportRequest.getRegisterDto();

            Long reportId = reportService.addReport(userId, registerDto, legalCode, jbAddress, detailAddress, deposit) ;

            return ResponseEntity.ok(reportId);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "서버 내부 오류: " + e.getMessage()));

        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> fetchReportList(
            @PathVariable("userId") Long userId,
            @RequestParam("sortKey") String sortKey,
            @RequestParam(value = "query", defaultValue = "") String query,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        Map<String, Object> result = reportService.fetchReportList(userId, sortKey, query, page, size);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{userId}/{reportId}")
    public ResponseEntity<?> fetchReport(@PathVariable Long userId, @PathVariable Long reportId) {
        try {
            ReportResponseDto report = reportService.fetchReport(userId, reportId);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "서버 내부 오류: " + e.getMessage()));
        }
    }


    @DeleteMapping("/{userId}/{reportId}")
    public ResponseEntity<?> deleteReport(@PathVariable Long userId, @PathVariable Long reportId) {
        try {
            reportService.deleteReport(userId, reportId);
            return ResponseEntity.ok(Map.of("message", "리포트가 성공적으로 삭제 처리되었습니다."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
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
