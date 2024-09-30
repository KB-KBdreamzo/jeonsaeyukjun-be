package com.jeonsaeyukjun.jeonsaeyukjunbe.map.controller;


import com.jeonsaeyukjun.jeonsaeyukjunbe.map.dto.AccidentDto;
import com.jeonsaeyukjun.jeonsaeyukjunbe.map.service.AccidentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/incidents")
public class AccidentController {

    private final AccidentService accidentService;

    public AccidentController(AccidentService accidentService) {
        this.accidentService = accidentService;
    }

    @GetMapping
    public List<AccidentDto> getAccidents() {
        return accidentService.getAccidents();
    }
}

