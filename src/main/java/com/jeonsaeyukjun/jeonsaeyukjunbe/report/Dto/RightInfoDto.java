package com.jeonsaeyukjun.jeonsaeyukjunbe.report.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RightInfoDto {
    private int reportId;
    private long priorityDeposit;
    private int mortgageRegistrationCount;
    private int leaseholdRegistrationCount;
}
