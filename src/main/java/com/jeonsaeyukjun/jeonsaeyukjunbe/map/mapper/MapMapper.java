package com.jeonsaeyukjun.jeonsaeyukjunbe.map.mapper;

import com.jeonsaeyukjun.jeonsaeyukjunbe.map.dto.AccidentDto;
import com.jeonsaeyukjun.jeonsaeyukjunbe.map.dto.SafetyScoreDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MapMapper {
    List<AccidentDto> fetchAccidents();
    List<SafetyScoreDto> fetchSafetyScores(@Param("legalCode") String legalCode);
}
