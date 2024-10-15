package com.jeonsaeyukjun.jeonsaeyukjunbe.contract.dto;

import java.sql.Date;

public class ContractWithReportDto {
    private int contractId;
    private int userId;
    private Integer reportId;
    private String contractName;
    private String contractUrl;
    private Date uploadTime;

    // Report 관련 필드
    private String reportAddress;
    private Long reportDeposit;

    public int getContractId() {
        return contractId;
    }

    public void setContractId(int contractId) {
        this.contractId = contractId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getContractUrl() {
        return contractUrl;
    }

    public void setContractUrl(String contractUrl) {
        this.contractUrl = contractUrl;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getReportAddress() {
        return reportAddress;
    }

    public void setReportAddress(String reportAddress) {
        this.reportAddress = reportAddress;
    }

    public Long getReportDeposit() {
        return reportDeposit;
    }

    public void setReportDeposit(Long reportDeposit) {
        this.reportDeposit = reportDeposit;
    }
}
