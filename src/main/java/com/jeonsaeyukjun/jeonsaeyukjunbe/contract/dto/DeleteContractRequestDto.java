package com.jeonsaeyukjun.jeonsaeyukjunbe.contract.dto;

public class DeleteContractRequestDto {
    private int userId;
    private String contractName;

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }
}

