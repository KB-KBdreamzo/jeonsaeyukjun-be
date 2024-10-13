package com.jeonsaeyukjun.jeonsaeyukjunbe.contract.mapper;

import com.jeonsaeyukjun.jeonsaeyukjunbe.contract.dto.FileDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FileMapper {
    void insertContract(FileDto fileDto);

    FileDto findByContractName(String contractName);
}

