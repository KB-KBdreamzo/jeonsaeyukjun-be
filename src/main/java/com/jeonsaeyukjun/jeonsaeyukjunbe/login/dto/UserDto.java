package com.jeonsaeyukjun.jeonsaeyukjunbe.login.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int userId;
    private String userKey;
    private String username;
    private String email;
    private String platformType;
    private String profileImg;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createAt;
}