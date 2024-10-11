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

    // Getters and Setters
}
