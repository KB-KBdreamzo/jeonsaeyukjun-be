package com.jeonsaeyukjun.jeonsaeyukjunbe.contract.mapper;

import com.jeonsaeyukjun.jeonsaeyukjunbe.contract.dto.ContractDto;
import com.jeonsaeyukjun.jeonsaeyukjunbe.contract.dto.OwnershipInfoDto;
import com.jeonsaeyukjun.jeonsaeyukjunbe.contract.dto.SpecialContractDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper // MyBatis에서 Mapper로 인식하도록 해준다. 이 인터페이스는 SQL 매핑을 통해 구현체가 생성된다.
public interface ContractMapper {

    //ContractDto 데이터를 기준으로 특정 계약 정보를 데이터베이스에서 가져오는 메소드
    ContractDto fetchContract(ContractDto contractDto);

    // ContractDto 객체를 받아 데이터베이스에서 새로운 계약 정보를 추가하는 메소드
    void addContract(ContractDto contractDto);

    // 특정 조건에 따라 SpecialContractDto 목록을 가져오는 메소드
    List<SpecialContractDto> fetchSpecialContracts(OwnershipInfoDto ownershipInfoDto);
}
