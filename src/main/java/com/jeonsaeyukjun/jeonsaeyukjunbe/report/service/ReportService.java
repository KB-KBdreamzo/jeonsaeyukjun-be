package com.jeonsaeyukjun.jeonsaeyukjunbe.report.service;

import com.jeonsaeyukjun.jeonsaeyukjunbe.report.Dto.RegisterDto;
import com.jeonsaeyukjun.jeonsaeyukjunbe.report.Dto.ReportResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final CrawlingService crawlingService;
    private final OpenApiService openApiService;

    public ReportResponseDto addReport(RegisterDto registerDto, String legalCode, String jbAddress, Long deposit) {


        Long nowPrice = openApiService.getNowPrice(jbAddress, legalCode, registerDto.getBuildingType(), registerDto.getBuildingArea());
        double salePriceRatio = crawlingService.getSalePriceRatio(jbAddress, registerDto.getBuildingType());

        boolean highTaxDelinquent = crawlingService.getHighTaxDelinquent(registerDto.getLessorName(), registerDto.getLessorBirth());
        boolean rentalFraud = crawlingService.getRentalFraud(registerDto.getLessorName(), registerDto.getLessorBirth());

        int safetyScore = caculateSafetyScore(registerDto, deposit, nowPrice, salePriceRatio);

        ReportResponseDto reportResponseDto = new ReportResponseDto();
        reportResponseDto.setDeposit(deposit);
        reportResponseDto.setJbAddress(jbAddress);
        reportResponseDto.setLegalCode(legalCode);
        reportResponseDto.setSafetyScore(safetyScore);
        reportResponseDto.setNowPrice(nowPrice);
        reportResponseDto.setSalePriceRatio(salePriceRatio);
        reportResponseDto.setHighTaxDelinquent(highTaxDelinquent);
        reportResponseDto.setRentalFraud(rentalFraud);
        reportResponseDto.setRegisterDto(registerDto);

        return new ReportResponseDto( deposit, jbAddress, legalCode, safetyScore, nowPrice, salePriceRatio, highTaxDelinquent, rentalFraud, registerDto);
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
