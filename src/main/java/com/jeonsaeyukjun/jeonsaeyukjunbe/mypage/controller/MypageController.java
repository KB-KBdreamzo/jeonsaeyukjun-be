package com.jeonsaeyukjun.jeonsaeyukjunbe.mypage.controller;

import com.jeonsaeyukjun.jeonsaeyukjunbe.mypage.service.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MypageController {

    private final MypageService mypageService;

    @GetMapping("/reports/{userId}")
    public ResponseEntity<Map<String, Object>> fetchReportList(
            @PathVariable("userId") Long userId,
            @RequestParam("sortKey") String sortKey,
            @RequestParam(value = "query", defaultValue = "") String query,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        Map<String, Object> result = mypageService.fetchReportList(userId, sortKey, query, page, size);
        return ResponseEntity.ok(result);
    }

}
