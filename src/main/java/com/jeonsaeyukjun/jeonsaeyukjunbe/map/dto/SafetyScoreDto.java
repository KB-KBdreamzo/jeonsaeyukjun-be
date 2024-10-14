package com.jeonsaeyukjun.jeonsaeyukjunbe.map.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SafetyScoreDto {
   private String roadName;
   private  String detailAddress;
   private int safetyScore;
}
