package com.jeonsaeyukjun.jeonsaeyukjunbe.contract.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractRequestDto {
    private ContractDto contractDto;
    private OwnershipInfoDto ownershipInfoDto;
    private int userId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
