package com.jeonsaeyukjun.jeonsaeyukjunbe.report.dto;

import lombok.*;

@Data
@NoArgsConstructor  // 기본 생성자 추가
@AllArgsConstructor
public class RegisterDto {
    private String lessorName;
    private String lessorBirth;
    private String roadName;
    private String detailAddress;
    private boolean isIllegalBuilding;

    // BuildingInfoDto
    private String landType;
    private double landArea;
    private String buildingType;                 // 단지형다세대
    private double buildingArea;
    private double area;

    //OwnershipInfoDto
    private boolean auctionRecord;               // 경매 기록 여부
    private boolean injuctionRecord;             // 가처분 기록 여부
    private boolean trustRegistrationRecord;     // 신탁 설정 여부
    private boolean redemptionRecord;            // 환매 특약 여부
    private boolean registrationRecord;          // 가등기 여부
    private int seizureCount;                    // 압류 내역 개수
    private int provisionalSeizureCount;         // 가압류 내역 개수

    // RightInfoDto
    private long priorityDeposit;              // 선순위채권총액
    private int leaseholdRegistrationCount;      // 전세권 설정 내역 개수
    private int mortgageCount;                   // 근저당 설정 내역 갯수
}
