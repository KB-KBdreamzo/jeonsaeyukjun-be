package com.jeonsaeyukjun.jeonsaeyukjunbe.agent.controller;

import com.jeonsaeyukjun.jeonsaeyukjunbe.agent.dto.AgentDto;
import com.jeonsaeyukjun.jeonsaeyukjunbe.agent.service.AgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/agent")
public class AgentController {
    private final AgentService agentService;

    @GetMapping("/{legalCode}")
    public ResponseEntity<?> fetchAgentList(
            @PathVariable int legalCode,
            @RequestParam int page,
            @RequestParam int size) {
        try {
            // 공인중개사 목록 조회
            List<AgentDto> agents = agentService.getAgentList(legalCode, page, size);
            if (agents.isEmpty()) {
                return ResponseEntity.notFound().build(); // 404 Not Found
            }
            return ResponseEntity.ok(agents); // 200 OK
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "서버 내부 오류: " + e.getMessage()));

        }
    }

}
