package com.jeonsaeyukjun.jeonsaeyukjunbe.report.dto;

import lombok.Data;

@Data
public class ReportRequestDto {
    private long deposit;
    private String jbAddress;
    private String legalCode;
    private RegisterDto registerDto;
}