package com.jeonsaeyukjun.jeonsaeyukjunbe.report.service;

import com.jeonsaeyukjun.jeonsaeyukjunbe.report.Dto.*;
import com.jeonsaeyukjun.jeonsaeyukjunbe.report.mapper.ReportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final CrawlingService crawlingService;
    private final OpenApiService openApiService;
    private final PropertyAddressService propertyAddressService;

    private final ReportMapper reportMapper;

    public Long addReport (int userId, RegisterDto registerDto, String legalCode, String jbAddress, String detailAddress, Long deposit) {

        Long nowPrice = openApiService.getNowPrice(jbAddress, legalCode, registerDto.getBuildingType(), registerDto.getBuildingArea());
        double salePriceRatio = crawlingService.getSalePriceRatio(jbAddress, registerDto.getBuildingType());

        boolean rentalFraud = crawlingService.getRentalFraud(registerDto.getLessorName(), registerDto.getLessorBirth());
        boolean highTaxDelinquent = crawlingService.getHighTaxDelinquent(registerDto.getLessorName(), registerDto.getLessorBirth());
        System.out.println(deposit + "~~" + salePriceRatio + " : " + nowPrice + "###" );
        int safetyScore = caculateSafetyScore(registerDto, deposit, nowPrice, salePriceRatio);

        // registerDto의 도로명 주소 및 상세주소로 주소 테이블에 있/없 확인
        String roadName = registerDto.getRoadName();
        PropertyAddressDto propertyAddressDto = new PropertyAddressDto(roadName, detailAddress, jbAddress, legalCode);
        propertyAddressService.addPropertyAddressDto(propertyAddressDto);

        // 주소랑 연결된 리포트 저장 => 이때 report의 id를 받아오게 지정!
        ReportDto resultReport = new ReportDto();
        resultReport.setUserId(userId);  // 사용자는 다르게 넣어야하는거 알지
        resultReport.setRoadName(roadName); resultReport.setDetailAddress(detailAddress); resultReport.setSafetyScore(safetyScore);
        resultReport.setLessorName(registerDto.getLessorName()); resultReport.setLessorBirth(registerDto.getLessorBirth()); resultReport.setDeposit(deposit);

        reportMapper.addReport(resultReport);
        reportMapper.addBuildingInfo(new BuildingInfoDto(resultReport.getReportId(), registerDto.getLandType(),registerDto.getLandArea(), registerDto.getBuildingType(), registerDto.getBuildingArea(), registerDto.getArea()));
        reportMapper.addLandlordIncident(new LandlordIncidentDto(resultReport.getReportId(), rentalFraud, highTaxDelinquent));
        reportMapper.addMoney(new MoneyDto(resultReport.getReportId(), salePriceRatio, nowPrice));
        reportMapper.addOwnershipInfo(new OwnershipInfoDto(resultReport.getReportId(), registerDto.isAuctionRecord(), registerDto.isInjuctionRecord(), registerDto.isTrustRegistrationRecord(), registerDto.isRedemptionRecord(), registerDto.isRegistrationRecord(), registerDto.getSeizureCount(), registerDto.getProvisionalSeizureCount()));
        reportMapper.addRightInfo(new RightInfoDto(resultReport.getReportId(),  registerDto.getPriorityDeposit(), registerDto.getMortgageCount(), registerDto.getLeaseholdRegistrationCount()));

        return (long) resultReport.getReportId();
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

    public Map<String, Object> fetchReportList(Long userId, String sortKey, String query, int page, int size) {
        int offset = (page - 1) * size;
        List<ReportDto> reports = reportMapper.fetchReportList(userId, sortKey, query, size, offset);
        int totalCount = reportMapper.fetchTotalReportCount(userId, query);

        Map<String, Object> result = new HashMap<>();
        result.put("reports", reports);
        result.put("totalPages", (int) Math.ceil((double) totalCount / size));
        return result;
    }

    public ReportResponseDto fetchReport(Long userId, Long reportId) {

        ReportDetailDto report = reportMapper.fetchReport(userId, reportId);
        if (report == null) throw new RuntimeException("존재하지 않는 리포트입니다.");


        RegisterDto registerDto = new RegisterDto(
                report.getLessorName(), report.getLessorBirth(), report.getRoadName(), true,
                report.getLandType(), report.getLandArea(), report.getBuildingType(), report.getBuildingArea(), report.getArea(),
                report.isAuctionRecord(), report.isInjuctionRecord(), report.isTrustRegistrationRecord(), report.isRedemptionRecord(), report.isRegistrationRecord(), report.getSeizureCount(), report.getProvisionalSeizureCount(),
                report.getPriorityDeposit(), report.getLeaseholdRegistrationCount(), report.getMortgageRegistrationCount()
        );

        return new ReportResponseDto(
                report.getDeposit(),
                report.getJbAddress(),
                report.getLegalCode(),
                report.getSafetyScore(),
                report.getNowPrice(),
                report.getSalePriceRatio(),
                report.getHighTaxDelinquent(),
                report.getRentalFraud(),
                registerDto
        );
    }

    public void deleteReport(Long userId, Long reportId){
        ReportResponseDto report = fetchReport(userId, reportId);
        if (report == null)  throw new RuntimeException("해당 리포트가 존재하지 않습니다.");

        int updatedRows = reportMapper.deleteReport(userId, reportId);
        if (updatedRows == 0) {
            throw new RuntimeException("리포트 삭제 처리 중 오류가 발생했습니다.");
        }
    }
}
