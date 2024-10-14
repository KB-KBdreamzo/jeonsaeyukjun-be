package com.jeonsaeyukjun.jeonsaeyukjunbe.report.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LandlordIncidentDto {
    private Integer reportId;
    private Boolean rentalFraud;
    private Boolean highTaxDelinquent;
}
