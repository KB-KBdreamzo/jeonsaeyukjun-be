package com.jeonsaeyukjun.jeonsaeyukjunbe.report.mapper;

import com.jeonsaeyukjun.jeonsaeyukjunbe.report.Dto.*;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReportMapper {
    void addReport(ReportDto reportDto);

    void addOwnershipInfo(OwnershipInfoDto ownershipInfoDto);
    void addRightInfo(RightInfoDto rightInfoDto);
    void addLandlordIncident(LandlordIncidentDto landlordIncidentDto);
    void addMoney(MoneyDto moneyDto);
    void addBuildingInfo(BuildingInfoDto buildingInfoDto);

}
