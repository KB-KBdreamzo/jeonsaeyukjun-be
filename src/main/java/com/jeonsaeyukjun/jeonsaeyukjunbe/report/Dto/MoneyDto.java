package com.jeonsaeyukjun.jeonsaeyukjunbe.report.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MoneyDto {
    private int reportId;
    private double salePriceRatio;
    private long nowPrice;
}