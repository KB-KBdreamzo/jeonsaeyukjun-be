package com.jeonsaeyukjun.jeonsaeyukjunbe.login.mapper;

import com.jeonsaeyukjun.jeonsaeyukjunbe.login.dto.KakaoOuathDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface KakaoOuathMapper {

    // 사용자 정보를 가져오는 쿼리
    KakaoOuathDto selectUserByUserKey(String userKey);

    // 사용자 정보를 삽입하는 쿼리
    int insertUser(KakaoOuathDto dto);

    // 사용자 정보를 업데이트하는 쿼리
    int updateUser(KakaoOuathDto dto);

    // 사용자 정보를 삭제하는 쿼리
    int deleteUser(String userKey);
}
