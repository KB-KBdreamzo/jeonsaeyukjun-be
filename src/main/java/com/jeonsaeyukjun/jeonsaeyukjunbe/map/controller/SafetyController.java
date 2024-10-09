package com.jeonsaeyukjun.jeonsaeyukjunbe.map.controller;

import com.jeonsaeyukjun.jeonsaeyukjunbe.map.dto.SafetyScoreDto;
import com.jeonsaeyukjun.jeonsaeyukjunbe.map.service.SafetyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/safety-data")
public class SafetyController {

    private final SafetyService safetyService;

    public SafetyController(SafetyService safetyService) {
        this.safetyService = safetyService;
    }

    @GetMapping
    public List<SafetyScoreDto> getSafetyScores(@RequestParam("법정동코드") String legalCode) {
        return safetyService.getSafetyScoresByLegalCode(legalCode);
    }
}
