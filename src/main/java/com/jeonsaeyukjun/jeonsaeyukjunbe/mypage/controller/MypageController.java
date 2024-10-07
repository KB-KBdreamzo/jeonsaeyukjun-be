package com.jeonsaeyukjun.jeonsaeyukjunbe.mypage.controller;

import com.jeonsaeyukjun.jeonsaeyukjunbe.mypage.service.MypageService;
import com.jeonsaeyukjun.jeonsaeyukjunbe.report.Dto.ReportDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MypageController {

    private final MypageService mypageService;

    @GetMapping("/reports/{userId}")
    public ResponseEntity<List<ReportDto>> getReports(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(mypageService.getReports(userId));

    }


}
