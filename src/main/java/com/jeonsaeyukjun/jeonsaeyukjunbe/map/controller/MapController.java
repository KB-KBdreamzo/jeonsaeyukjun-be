package com.jeonsaeyukjun.jeonsaeyukjunbe.map.controller;

import com.jeonsaeyukjun.jeonsaeyukjunbe.map.dto.AccidentDto;
import com.jeonsaeyukjun.jeonsaeyukjunbe.map.dto.SafetyScoreDto;
import com.jeonsaeyukjun.jeonsaeyukjunbe.map.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/map")
public class MapController {

    private final MapService mapService;

    @GetMapping("/incidents")
    public List<AccidentDto> fetchAccidents() {
        return mapService.fetchAccidents();
    }

    @GetMapping("/safetyScores")
    public List<SafetyScoreDto> fetchSafetyScores(@RequestParam("legalCode") String legalCode) {
        return mapService.fetchSafetyScores(legalCode);
    }
}
