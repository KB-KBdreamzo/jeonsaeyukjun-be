package com.jeonsaeyukjun.jeonsaeyukjunbe.mypage.controller;

import com.jeonsaeyukjun.jeonsaeyukjunbe.mypage.service.MypageService;
import com.jeonsaeyukjun.jeonsaeyukjunbe.report.Dto.ReportDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MypageController {

    private final MypageService mypageService;

    @GetMapping("/reports/{userId}")
    public ResponseEntity<List<ReportDto>> getReports(
            @PathVariable("userId") Long userId,
            @RequestParam(value = "limit", required = false) Integer limit) {
        if (limit != null) {
            return ResponseEntity.ok(mypageService.getLimitReports(userId, limit));
        }
        return ResponseEntity.ok(mypageService.getReports(userId));

    }

    // 페이징 처리를 위한 새로운 리포트 조회
    @GetMapping("/reports/paged/{userId}")
    public ResponseEntity<Map<String, Object>> getReportsWithPaging(
            @PathVariable("userId") Long userId,
            @RequestParam("sortKey") String sortKey,
            @RequestParam(value = "query", defaultValue = "") String query,
            @RequestParam("page") int page,      // 페이지 번호
            @RequestParam("size") int size) {     // 페이지당 항목 수
        Map<String, Object> result = mypageService.getReportsWithPaging(userId, sortKey, query, page, size);
        return ResponseEntity.ok(result);
    }

}
