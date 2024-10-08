package com.jeonsaeyukjun.jeonsaeyukjunbe.login.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Data
@Getter
@Setter
@NoArgsConstructor
public class KakaoOuathDto {
    private int userId;
    private String userKey;
    private String userName;
    private String email;
    private String platformType;
    private String profileImg;
    private Timestamp createdAt;
}
