package com.jeonsaeyukjun.jeonsaeyukjunbe.report.service;

import com.jeonsaeyukjun.jeonsaeyukjunbe.report.Dto.RegisterDto;
import com.jeonsaeyukjun.jeonsaeyukjunbe.report.Dto.ReportResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final CrawlingService crawlingService;
    private final OpenApiService openApiService;

    public ReportResponseDto addReport(RegisterDto registerDto, Long legalCode, String jbAddress, Long deposit) {
        ReportResponseDto reportResponseDto = new ReportResponseDto();

        // 경매 건수 판단 로직

        // 추정시세
        // 선순위 채권액
        // 환불가능보증금이 있는지 여부만 나타내자 (안전리포트에 필요하니까)
        Long nowPrice = openApiService.getNowPrice(jbAddress, legalCode, registerDto.getBuildingType(), registerDto.getBuildingArea());
        double salePriceRatio = crawlingService.getSalePriceRatio(jbAddress, registerDto.getBuildingType());

        // 환불 가능 보증금 (전세금 < 시세 * 낙찰가율 - 선순위 채권액) 이어야 환불 가능
        // 추정시세 * 0.7 > 전세금  이어야 깡통전세 (80프로인가 70프로인가 암튼 그럼)



        System.out.println(nowPrice + " " + salePriceRatio);
        boolean highTaxDelinquent = crawlingService.getHighTaxDelinquent(registerDto.getLessorName(), registerDto.getRoadName());
        boolean rentalFraud = crawlingService.getRentalFraud(registerDto.getLessorName(), registerDto.getRoadName());
        System.out.println(rentalFraud + " " + highTaxDelinquent);

        reportResponseDto.setRegisterDto(registerDto);
        reportResponseDto.setDeposit(String.valueOf(deposit));
        reportResponseDto.setScore("58");
        reportResponseDto.setJbAddress(jbAddress);
        reportResponseDto.setHighTaxDelinquent(highTaxDelinquent);
        reportResponseDto.setRentalFraud(rentalFraud);
        reportResponseDto.setLegalCode(String.valueOf(legalCode));
        return reportResponseDto;
    }

    public ReportResponseDto fetchReport(int reportId) {
        ReportResponseDto reportDto = new ReportResponseDto();
        return reportDto;
    }

    public void deleteReport(ReportResponseDto reportDto){
    }
}
