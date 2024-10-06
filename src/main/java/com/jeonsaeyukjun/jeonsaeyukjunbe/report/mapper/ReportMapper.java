package com.jeonsaeyukjun.jeonsaeyukjunbe.report.mapper;

import com.jeonsaeyukjun.jeonsaeyukjunbe.report.Dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReportMapper {
    void addReport(ReportDto reportDto);
    void addOwnershipInfo(OwnershipInfoDto ownershipInfoDto);
    void addRightInfo(RightInfoDto rightInfoDto);
    void addLandlordIncident(LandlordIncidentDto landlordIncidentDto);
    void addMoney(MoneyDto moneyDto);
    void addBuildingInfo(BuildingInfoDto buildingInfoDto);

    ReportDto fetchReport(int reportId);
    BuildingInfoDto fetchBuildingInfo(int reportId);
    OwnershipInfoDto fetchOwnershipInfo(int reportId);
    RightInfoDto fetchRightInfo(int reportId);
    MoneyDto fetchMoney(int reportId);
    LandlordIncidentDto fetchLandlordIncident(int reportId);
    PropertyAddressDto fetchPropertyAddress(@Param("roadName")String roadName,  @Param("detailAddress") String detailAddress);

    int deleteReport(int reportId);
}
