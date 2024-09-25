package com.jeonsaeyukjun.jeonsaeyukjunbe.contract.dto;

public class ContractDTO {
    private String address;
    private String tenant;
    private String landlord;
    private String rent;

    public ContractDTO() {} // 기본 생성자 추가

    // Getters and Setters
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getLandlord() {
        return landlord;
    }

    public void setLandlord(String landlord) {
        this.landlord = landlord;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }
}