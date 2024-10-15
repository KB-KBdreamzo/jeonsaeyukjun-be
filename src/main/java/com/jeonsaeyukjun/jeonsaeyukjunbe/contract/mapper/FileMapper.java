package com.jeonsaeyukjun.jeonsaeyukjunbe.contract.mapper;

import com.jeonsaeyukjun.jeonsaeyukjunbe.contract.dto.ContractTableDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper {
    void insertContract(ContractTableDto fileDto);

    ContractTableDto findByContractName(String contractName);
}

