package com.jeonsaeyukjun.jeonsaeyukjunbe.agent.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeonsaeyukjun.jeonsaeyukjunbe.agent.dto.AgentDto;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class AgentService {

    private List<AgentDto> agents;

    @PostConstruct
    public void init() {
        System.out.println("파일읽는다");
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream is = getClass().getResourceAsStream("/data/agents.json")) {
            agents = objectMapper.readValue(is, new TypeReference<List<AgentDto>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<AgentDto> getAgentList(int legalCode) {
        return agents.stream()
                .filter(agent -> agent.getStdg_cd().equals(String.valueOf(legalCode)))
                .collect(Collectors.toList());
    }
}
