package com.jeonsaeyukjun.jeonsaeyukjunbe.map.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SafetyScoreDto {

    private String roadName;
    private String detailAddress;
    private int safetyScore;

}
