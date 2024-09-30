package com.jeonsaeyukjun.jeonsaeyukjunbe.map.mapper;


import com.jeonsaeyukjun.jeonsaeyukjunbe.map.dto.AccidentDto;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface AccidentMapper{
    List<AccidentDto> selectAccidents();
}

