package com.jeonsaeyukjun.jeonsaeyukjunbe.login.service;

import com.jeonsaeyukjun.jeonsaeyukjunbe.login.dto.KakaoOuathDto;
import com.jeonsaeyukjun.jeonsaeyukjunbe.login.mapper.KakaoOuathMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class KakaoOuathService {

    @Autowired
    private KakaoOuathMapper mapper;

    // 사용자 정보를 삽입

    public KakaoOuathDto createUser(KakaoOuathDto dto) {
        mapper.insertUser(dto);
        return dto; // 혹은 DB에서 생성된 ID를 세팅할 수 있습니다.
    }

    // 사용자 정보를 가져오기
    public KakaoOuathDto getUserByUserKey(String userKey) {
        return mapper.selectUserByUserKey(userKey);
    }

    // 사용자 정보 업데이트
    public KakaoOuathDto updateUser(KakaoOuathDto dto) {
        mapper.updateUser(dto);
        return dto;
    }

    // 사용자 정보 삭제
    public void deleteUser(String userKey) {
        mapper.deleteUser(userKey);
    }
}
