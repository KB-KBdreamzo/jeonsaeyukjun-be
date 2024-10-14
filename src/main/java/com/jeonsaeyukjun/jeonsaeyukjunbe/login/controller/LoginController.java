package com.jeonsaeyukjun.jeonsaeyukjunbe.login.controller;

import com.jeonsaeyukjun.jeonsaeyukjunbe.login.dto.UserDto;
import com.jeonsaeyukjun.jeonsaeyukjunbe.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login")
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestBody UserDto userinfo) {
        try {
            UserDto user = loginService.kakaoLogin(userinfo);
            return ResponseEntity.ok(user); // user(안에 jwt 토큰)
        } catch (Exception e) {
            log.error("Error during kakao login", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("카카오 로그인 처리 중 오류 발생");
        }
    }

    @PostMapping("/kakao/check")
    public ResponseEntity<?> kakaoCheck(@RequestBody UserDto userinfo) {
        try {
            boolean isUser = loginService.kakaoCheck(userinfo);

            if (isUser) {
                return ResponseEntity.ok(true);
            } else {
                return ResponseEntity.ok(false);
            }
        } catch (Exception e) {
            log.error("Error during kakao login", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("카카오 로그인 처리 중 오류 발생");
        }
    }

}
