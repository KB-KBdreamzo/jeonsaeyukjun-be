package com.jeonsaeyukjun.jeonsaeyukjunbe.login.service;

import com.jeonsaeyukjun.jeonsaeyukjunbe.login.dto.UserDto;
import com.jeonsaeyukjun.jeonsaeyukjunbe.login.mapper.LoginMapper;
import com.jeonsaeyukjun.jeonsaeyukjunbe.login.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final LoginMapper loginMapper;
    private final JwtUtil jwtUtil;

    public String kakaoLogin(UserDto kakaoUser) {
        UserDto user = loginMapper.findByUserKey(kakaoUser.getUserKey());

        if (user == null) {
            loginMapper.register(kakaoUser);
            user = kakaoUser;
        }

        // 로그인 처리
        authenticateUser(user);

        // JWT 생성
        return jwtUtil.generateToken(String.valueOf(user.getUserId()), user.getProfileImg());
    }

    public boolean kakaoCheck(UserDto kakaoUser) {
        UserDto user = loginMapper.findByUserKey(kakaoUser.getUserKey());

        if (user == null) {
            return false;
        }

        return true;
    }

    private void authenticateUser(UserDto user) {
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getUserKey(), null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
