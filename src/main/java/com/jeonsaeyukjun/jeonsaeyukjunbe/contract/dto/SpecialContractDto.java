package com.jeonsaeyukjun.jeonsaeyukjunbe.contract.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecialContractDto {
    private String conditionType;
    private String content;
}