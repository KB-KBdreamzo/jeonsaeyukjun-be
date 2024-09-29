package com.jeonsaeyukjun.jeonsaeyukjunbe.report.service;

import com.jeonsaeyukjun.jeonsaeyukjunbe.report.Dto.*;
import com.jeonsaeyukjun.jeonsaeyukjunbe.report.mapper.ReportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final CrawlingService crawlingService;
    private final OpenApiService openApiService;
    private final PropertyAddressService propertyAddressService;

    private final ReportMapper reportMapper;

    public ReportResponseDto generateReport
            (RegisterDto registerDto, String legalCode, String jbAddress, Long deposit) {

        Long nowPrice = openApiService.getNowPrice(jbAddress, legalCode, registerDto.getBuildingType(), registerDto.getBuildingArea());
        double salePriceRatio = crawlingService.getSalePriceRatio(jbAddress, registerDto.getBuildingType());

        boolean rentalFraud = crawlingService.getRentalFraud(registerDto.getLessorName(), registerDto.getLessorBirth());
        boolean highTaxDelinquent = crawlingService.getHighTaxDelinquent(registerDto.getLessorName(), registerDto.getLessorBirth());

        int safetyScore = caculateSafetyScore(registerDto, deposit, nowPrice, salePriceRatio);

        // registerDto의 도로명 주소 및 상세주소로 주소 테이블에 있/없 확인
        String roadName = registerDto.getRoadName();
        String detailAddress = registerDto.getDetailAddress();
        PropertyAddressDto propertyAddressDto = new PropertyAddressDto(roadName, detailAddress, legalCode);
        propertyAddressService.addPropertyAddressDto(propertyAddressDto);

        // 주소랑 연결된 리포트 저장 => 이때 report의 id를 받아오게 지정!
        ReportDto resultReport = new ReportDto();
        resultReport.setUserId(1);  // 사용자는 다르게 넣어야하는거 알지
        resultReport.setRoadName(roadName); resultReport.setDetailAddress(detailAddress); resultReport.setSafetyScore(safetyScore);
        resultReport.setLessorName(registerDto.getLessorName()); resultReport.setDeposit(resultReport.getDeposit());
        reportMapper.addReport(resultReport);

        reportMapper.addBuildingInfo(new BuildingInfoDto(resultReport.getReportId(), registerDto.getLandType(),registerDto.getLandArea(), registerDto.getBuildingType(), registerDto.getBuildingArea(), registerDto.getArea()));
        reportMapper.addLandlordIncident(new LandlordIncidentDto(resultReport.getReportId(), rentalFraud, highTaxDelinquent));
        reportMapper.addMoney(new MoneyDto(resultReport.getReportId(), salePriceRatio, nowPrice));
        reportMapper.addOwnershipInfo(new OwnershipInfoDto(resultReport.getReportId(), registerDto.isAuctionRecord(), registerDto.isInjuctionRecord(), registerDto.isTrustRegistrationRecord(), registerDto.isRedemptionRecord(), registerDto.isRegistrationRecord(), registerDto.getSeizureCount(), registerDto.getProvisionalSeizureCount()));
        reportMapper.addRightInfo(new RightInfoDto(resultReport.getReportId(),  registerDto.getPriorityDeposit(), registerDto.getMortgageCount(), registerDto.getLeaseholdRegistrationCount()));

        return new ReportResponseDto(deposit, jbAddress, legalCode, safetyScore, nowPrice, salePriceRatio, highTaxDelinquent, rentalFraud, registerDto);
    }

    private static int caculateSafetyScore(RegisterDto registerDto, Long deposit, Long nowPrice, double salePriceRatio) {
        int safetyScore = 0;
        // 환불 가능할 때 25점 (예측 손실액 0) 시세 * 낙찰가율 - 선순위 채권액 > 전세금
        if (nowPrice * salePriceRatio - registerDto.getPriorityDeposit() > deposit) safetyScore += 25;

        // 깡통전세 아닐 경우 10점 (추정시세 * 0.7 > 전세금)
        if (nowPrice * 0.7 > deposit) safetyScore += 10;

        safetyScore += (int) Stream.of(
                        registerDto.isAuctionRecord(),
                                registerDto.isInjuctionRecord(),
                                registerDto.isTrustRegistrationRecord(),
                                registerDto.isRedemptionRecord(),
                                registerDto.isRegistrationRecord(),
                                registerDto.getSeizureCount() > 0,
                                registerDto.getProvisionalSeizureCount() > 0,
                                registerDto.getLeaseholdRegistrationCount() > 0,
                                registerDto.getMortgageCount() > 0
                        ).filter(Boolean::booleanValue)
                        .count() * 5;
        return safetyScore;
    }

    public ReportResponseDto fetchReport(int reportId) {
        ReportResponseDto reportDto = new ReportResponseDto();
        return reportDto;
    }

    public void deleteReport(ReportResponseDto reportDto){
    }
}
