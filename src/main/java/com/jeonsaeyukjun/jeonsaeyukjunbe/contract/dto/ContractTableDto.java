package com.jeonsaeyukjun.jeonsaeyukjunbe.contract.dto;

import java.sql.Date;

public class ContractTableDto {
    private int contractId;
    private int userId;
    private Integer reportId;
    private String contractName;
    private String contractUrl;
    private Date uploadTime;

    public ContractTableDto() {
    }

    public ContractTableDto(int contractId, int userId, Integer reportId, String contractName, String contractUrl, Date uploadTime) {
        this.contractId = contractId;
        this.userId = userId;
        this.reportId = reportId;
        this.contractName = contractName;
        this.contractUrl = contractUrl;
        this.uploadTime = uploadTime;
    }

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

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getContractUrl() {
        return contractUrl;
    }

    public void setContractUrl(String contractUrl) {
        this.contractUrl = contractUrl;
    }
}
