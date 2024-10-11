package com.jeonsaeyukjun.jeonsaeyukjunbe.login.controller;

import com.jeonsaeyukjun.jeonsaeyukjunbe.login.dto.UserDto;
import com.jeonsaeyukjun.jeonsaeyukjunbe.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class TestController {
    private final LoginService loginService;

    @PostMapping("")
    public String kakaoLogin() {
        return "인증";
    }
}