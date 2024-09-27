package com.jeonsaeyukjun.jeonsaeyukjunbe.report.Dto;

import lombok.Data;

@Data
public class ReportRequestDto {
    private String deposit;
    private String jbAddress;
    private String legalCode; // 행정동코드임!
    private RegisterDto registerDto;
}