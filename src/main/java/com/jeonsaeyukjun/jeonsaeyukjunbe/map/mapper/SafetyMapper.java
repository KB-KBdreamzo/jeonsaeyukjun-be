package com.jeonsaeyukjun.jeonsaeyukjunbe.map.mapper;

import com.jeonsaeyukjun.jeonsaeyukjunbe.map.dto.SafetyScoreDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SafetyMapper {
    // 법정동코드에 맞는 안전도 점수를 조회하는 메서드
    List<SafetyScoreDto> findSafetyScoresByLegalCode(@Param("legalCode") String legalCode);
}
