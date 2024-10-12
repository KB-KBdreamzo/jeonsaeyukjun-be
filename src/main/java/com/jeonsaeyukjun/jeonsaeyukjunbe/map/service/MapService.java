package com.jeonsaeyukjun.jeonsaeyukjunbe.map.service;

import com.jeonsaeyukjun.jeonsaeyukjunbe.map.dto.AccidentDto;
import com.jeonsaeyukjun.jeonsaeyukjunbe.map.dto.SafetyScoreDto;
import com.jeonsaeyukjun.jeonsaeyukjunbe.map.mapper.MapMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MapService {

    private final MapMapper mapMapper;

    public List<AccidentDto> fetchAccidents() {
        return mapMapper.fetchAccidents();
    }

    public List<SafetyScoreDto> fetchSafetyScores(String legalCode) {
        return mapMapper.fetchSafetyScores(legalCode);
    }
}
