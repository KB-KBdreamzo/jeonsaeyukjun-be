package com.jeonsaeyukjun.jeonsaeyukjunbe.map.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AccidentDto {
    private String regionName;
    private int incidentCount;
    private long incidentAmount;
    private float incidentRate;
    private double latitude;
    private double longitude;
}
