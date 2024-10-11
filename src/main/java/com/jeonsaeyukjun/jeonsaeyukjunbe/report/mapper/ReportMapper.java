package com.jeonsaeyukjun.jeonsaeyukjunbe.report.mapper;

import com.jeonsaeyukjun.jeonsaeyukjunbe.report.Dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReportMapper {
    void addReport(ReportDto reportDto);
    void addOwnershipInfo(OwnershipInfoDto ownershipInfoDto);
    void addRightInfo(RightInfoDto rightInfoDto);
    void addLandlordIncident(LandlordIncidentDto landlordIncidentDto);
    void addMoney(MoneyDto moneyDto);
    void addBuildingInfo(BuildingInfoDto buildingInfoDto);

    List<ReportDto> fetchReportList(
            @Param("userId") Long userId,
            @Param("sortKey") String sortKey,
            @Param("query") String query,
            @Param("size") int size,
            @Param("offset") int offset
    );

    int fetchTotalReportCount(@Param("userId") Long userId, @Param("query") String query);

    ReportDetailDto fetchReport(@Param("userId") Long userId, @Param("reportId") Long reportId);

    int deleteReport(@Param("userId") Long userId, @Param("reportId") Long reportId);
}
