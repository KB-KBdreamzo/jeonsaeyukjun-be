package com.jeonsaeyukjun.jeonsaeyukjunbe.report.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponseDto {
    private long deposit;
    private String jbAddress;
    private long legalCode; // 행정동코드임!

    private int safetyScore;
    private long nowPrice;
    private double salePriceRatio;
    private boolean highTaxDelinquent;
    private boolean rentalFraud;
    private RegisterDto registerDto;
}