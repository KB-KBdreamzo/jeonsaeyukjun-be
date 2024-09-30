package com.jeonsaeyukjun.jeonsaeyukjunbe.map.service;

import com.jeonsaeyukjun.jeonsaeyukjunbe.map.dto.AccidentDto;
import com.jeonsaeyukjun.jeonsaeyukjunbe.map.mapper.AccidentMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccidentService {

    private final AccidentMapper accidentMapper;


    public AccidentService(AccidentMapper accidentMapper) {
        this.accidentMapper = accidentMapper;
    }

    public List<AccidentDto> getAccidents() {
        return accidentMapper.selectAccidents();
    }
}
