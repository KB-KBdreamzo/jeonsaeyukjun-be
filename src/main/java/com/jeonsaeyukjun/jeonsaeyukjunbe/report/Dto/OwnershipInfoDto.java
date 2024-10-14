package com.jeonsaeyukjun.jeonsaeyukjunbe.report.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OwnershipInfoDto {
    private int reportId;
    private boolean auctionRecord;
    private boolean injuctionRecord;
    private boolean trustRegistrationRecord;
    private boolean redemptionRecord;
    private boolean registrationRecord;
    private int seizureCount;
    private int provisionalSeizureCount;
}
