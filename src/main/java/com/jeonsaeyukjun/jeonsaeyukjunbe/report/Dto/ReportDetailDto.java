package com.jeonsaeyukjun.jeonsaeyukjunbe.report.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDetailDto {
    private int reportId;
    private int userId;
    private String roadName;
    private String detailAddress;
    private int safetyScore;
    private String lessorName;
    private String lessorBirth;
    private long deposit;
    private boolean isDelete;
    private String createAt;

    // BuildingInfoDto
    private String landType;
    private double landArea;
    private String buildingType;
    private double buildingArea;
    private double area;

    // LandlordIncident
    private Boolean rentalFraud;
    private Boolean highTaxDelinquent;

    // Money
    private double salePriceRatio;
    private long nowPrice;

    //OwnershipInfo
    private boolean auctionRecord;
    private boolean injuctionRecord;
    private boolean trustRegistrationRecord;
    private boolean redemptionRecord;
    private boolean registrationRecord;
    private int seizureCount;
    private int provisionalSeizureCount;

    // RightInfo
    private long priorityDeposit;
    private int mortgageRegistrationCount;
    private int leaseholdRegistrationCount;

    // PropertyAddress
    private String jbAddress;
    private String legalCode;
}
