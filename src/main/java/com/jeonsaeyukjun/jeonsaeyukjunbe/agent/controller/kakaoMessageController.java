package com.jeonsaeyukjun.jeonsaeyukjunbe.agent.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jeonsaeyukjun.jeonsaeyukjunbe.agent.Dto.kakaoMessageRequest;
import com.jeonsaeyukjun.jeonsaeyukjunbe.agent.service.kakaoMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


@Slf4j
@RestController
@PropertySource("classpath:application.properties")
@RequestMapping("/kakao")
public class kakaoMessageController {

    private final kakaoMessageService kakaoMsgService;

    @Autowired
    public kakaoMessageController(kakaoMessageService kakaoMsgService) {
        this.kakaoMsgService = kakaoMsgService;
    }

    @GetMapping("/callback")
    public void getKakaoToken(@RequestParam("code") String code, HttpServletResponse response) {
        try {
            String accessToken = kakaoMsgService.getKakaoToken(code);

            String redirectUrl = "http://localhost:5173/agent?accessToken=" + accessToken;
            response.sendRedirect(redirectUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/send-message")
    public ResponseEntity<String> sendKakaoMessage(
            @RequestBody kakaoMessageRequest request,
            @RequestHeader("Authorization") String accessToken) throws JsonProcessingException {

        return kakaoMsgService.sendKakaoMessage(request, accessToken);
    }
}
