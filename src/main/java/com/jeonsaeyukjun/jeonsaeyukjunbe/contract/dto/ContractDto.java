package com.jeonsaeyukjun.jeonsaeyukjunbe.contract.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ContractDto {
    private String conditionType;

    private String landlordName; // 임대인 이름
    private String tenantName;   // 임차인 이름
    private String address;      // 계약 주소
    private String landType;     // 토지 지목 (예: 대지, 농지 등)
    private double landArea;     // 토지 면적 (제곱미터)
    private String structurePurpose; // 건물 구조 및 용도 (예: 주거용, 상업용 등)
    private double buildingArea; // 건물 면적 (제곱미터)
    private String leasedPortion; // 임차할 부분 (예: 전체, 일부 등)
    private double leasedPortionArea; // 임차할 부분 면적 (제곱미터)
    private String contractType; // 계약의 종류 (예: 신규 계약, 합의에 의한 재계약, 갱신계약)

    // 임대차 계약 시작일
    private int lsSY; // 계약 시작 연도 (YYYY 형식) (leaseStartYear)
    private int lsSM; // 계약 시작 월 (1~12) (leaseStartMonth)
    private int lsSD; // 계약 시작 일 (1~31) (leaseStartDay)

    // 임대차 계약 종료일
    private int lsEY; // 계약 종료 연도 (YYYY 형식) (leaseEndYear)
    private int lsEM; // 계약 종료 월 (1~12) (leaseEndMonth)
    private int lsED; // 계약 종료 일 (1~31) (leaseEndDay)

    private String unpaidNationalAndLocalTax; // 미납 세금 내역 (세목 및 금액 등)
    private String priorityConfirmedDateDetails; // 선순위 확정일자 내역 (확정일자 정보)
    private String contractConfirmationDate; // 계약 확정일자 부여일 (확정일자)
    private String paymentAccount; // 입금 계좌 정보 (은행명 및 계좌번호)
    private int depositAmount; // 보증금 (원)
    private int downPayment; // 계약금 (원)
    private int interimPayment; // 중도금 (원)

    // 중도금 지급일
    private int interYear; // 중도금 지급 연도 (YYYY 형식)
    private int interMonth; // 중도금 지급 월 (1~12)
    private int interDay; // 중도금 지급 일 (1~31)

    private int finalPayment; // 잔금 (원)

    // 잔금 지급일
    private int finalYear; // 잔금 지급 연도 (YYYY 형식)
    private int finalMonth; // 잔금 지급 월 (1~12)
    private int finalDay; // 잔금 지급 일 (1~31)

    private int managementFee; // 관리비 (원)
    private String repairDetails; // 수리 필요 사항에 대한 설명 (예: 수리 내용 및 필요성)
    private int taxAmount; // 체납액

    // 임대인 정보
    private String landlordAddress; // 임대인 주소
    private String landlordResidentId; // 임대인 주민등록번호 (주민등록번호)
    private String landlordPhone; // 임대인 전화번호 (연락처)

    // 임차인 정보
    private String tenantAddress; // 임차인 주소
    private String tenantResidentId; // 임차인 주민등록번호 (주민등록번호)
    private String tenantPhone; // 임차인 전화번호 (연락처)

    // 오늘 날짜
    private int todayYear;
    private int todayMonth;
    private int todayDay;

    public ContractDto() {}

}