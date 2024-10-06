package com.jeonsaeyukjun.jeonsaeyukjunbe.report.Dto;

import lombok.Data;

@Data
public class ReportRequestDto {
    private long deposit;
    private String jbAddress;
    private String legalCode;
    private RegisterDto registerDto;
}