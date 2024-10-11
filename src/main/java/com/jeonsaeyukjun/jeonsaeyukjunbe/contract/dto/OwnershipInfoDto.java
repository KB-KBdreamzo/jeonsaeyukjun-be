package com.jeonsaeyukjun.jeonsaeyukjunbe.contract.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnershipInfoDto {
    private Integer reportId;
    private Boolean auctionRecord;
    private Boolean registrationRecord;
    private Boolean trustRegistrationRecord;
    private Boolean redemptionRecord;
    private Boolean injuctionRecord;
    private Integer seizureCount;
    private Integer provisionalSeizureCount;

    // Getters and Setters
}
