package com.jeonsaeyukjun.jeonsaeyukjunbe.login.controller;

import com.jeonsaeyukjun.jeonsaeyukjunbe.login.dto.KakaoOuathDto;
import com.jeonsaeyukjun.jeonsaeyukjunbe.login.service.KakaoOuathService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login/")
@RequiredArgsConstructor
public class KakaoOauthController {
    private final KakaoOuathService kakaoOuathService;

    @GetMapping("/kakao")
    public void test() {
        System.out.println("#####");
        KakaoOuathDto user = new KakaoOuathDto();
        user.setUserKey("123");
        user.setUserName("123");
        user.setEmail("123@qq.com");
        user.setPlatformType("kakao");
        user.setProfileImg("img");
        kakaoOuathService.createUser(user);
    }

//    @PostMapping("/kakao")
//    public ResponseEntity<KakaoOuathDto> handleKakaoLogin(@RequestBody KakaoOuathDto kakaoUser) {
//        System.out.println("@@@@");
//        System.out.println("Received Kakao User Info: " + kakaoUser);
//        return ResponseEntity.ok(kakaoUser); // 받아온 사용자 정보를 반환
//    }
}
