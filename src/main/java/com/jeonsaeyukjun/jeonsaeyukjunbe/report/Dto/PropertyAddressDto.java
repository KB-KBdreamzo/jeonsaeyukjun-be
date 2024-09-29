package com.jeonsaeyukjun.jeonsaeyukjunbe.report.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PropertyAddressDto {
    private String roadName;
    private String detailAddress;
    private String legalCode;
}
