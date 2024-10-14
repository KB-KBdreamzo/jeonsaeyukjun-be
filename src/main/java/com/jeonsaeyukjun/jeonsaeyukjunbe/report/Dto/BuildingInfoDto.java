package com.jeonsaeyukjun.jeonsaeyukjunbe.report.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BuildingInfoDto {
    private int reportId;
    private String landType;
    private double landArea;
    private String buildingType;
    private double buildingArea;
    private double area;
}
