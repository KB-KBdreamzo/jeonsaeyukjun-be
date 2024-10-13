package com.jeonsaeyukjun.jeonsaeyukjunbe.contract.dto;

import org.springframework.beans.factory.annotation.Value;

public class FileDto {
    private int contractId;
    private int userId;
    private Integer reportId;
    private String contractName;
    private String contractUrl;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

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

    public String getContractUrl(String contractName) {
        return contractUrl;
    }

    public void setContractUrl(String contractUrl) {
        this.contractUrl = contractUrl;
    }

    public String updateContractUrl(String contractUrl) {
        return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + contractName;
    }
}

