package com.jeonsaeyukjun.jeonsaeyukjunbe.report.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDto {
    private int reportId;
    private int userId;
    private String roadName;
    private String detailAddress;
    private int safetyScore;
    private String lessorName;
    private long deposit;
    private boolean isDelete;
    private String createAt;
}
