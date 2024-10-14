package com.jeonsaeyukjun.jeonsaeyukjunbe.login.mapper;

import com.jeonsaeyukjun.jeonsaeyukjunbe.login.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Mapper
public interface LoginMapper {
    int register(UserDto userDto);
    UserDto findByUserKey(@Param("userKey") String userKey);
}