package com.jeonsaeyukjun.jeonsaeyukjunbe.map.service;

import com.jeonsaeyukjun.jeonsaeyukjunbe.map.dto.SafetyScoreDto;
import com.jeonsaeyukjun.jeonsaeyukjunbe.map.mapper.SafetyMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SafetyService {

    private final SafetyMapper safetyMapper;

    public SafetyService(SafetyMapper safetyMapper) {
        this.safetyMapper = safetyMapper;
    }

    // 법정동코드를 기준으로 안전도 점수를 가져오는 서비스 메서드
    public List<SafetyScoreDto> getSafetyScoresByLegalCode(String legalCode) {
        return safetyMapper.findSafetyScoresByLegalCode(legalCode);
    }
}
