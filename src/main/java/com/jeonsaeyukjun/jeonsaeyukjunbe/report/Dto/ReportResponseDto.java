package com.jeonsaeyukjun.jeonsaeyukjunbe.report.Dto;

import lombok.Data;

@Data
public class ReportResponseDto {
    private String score;
    private String deposit;
    private String jbAddress;
    private String legalCode; // 행정동코드임!
    private boolean highTaxDelinquent;
    private boolean rentalFraud;
    private RegisterDto registerDto;
}