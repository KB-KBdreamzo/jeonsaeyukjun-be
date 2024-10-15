package com.jeonsaeyukjun.jeonsaeyukjunbe.contract.mapper;

import com.jeonsaeyukjun.jeonsaeyukjunbe.contract.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ContractMapper {

    //ContractDto 데이터를 기준으로 특정 계약 정보를 데이터베이스에서 가져오는 메소드
    ContractDto fetchContract(ContractDto contractDto);

    // ContractDto 객체를 받아 데이터베이스에서 새로운 계약 정보를 추가하는 메소드
    void addContract(ContractDto contractDto);

    // 특정 조건에 따라 SpecialContractDto 목록을 가져오는 메소드
    List<SpecialContractDto> fetchSpecialContracts(OwnershipInfoDto ownershipInfoDto);

    List<ContractWithReportDto> findAllByUserId(int userId);

    void deleteContract(@Param("userId") int userId, @Param("contractName") String contractName);
}
