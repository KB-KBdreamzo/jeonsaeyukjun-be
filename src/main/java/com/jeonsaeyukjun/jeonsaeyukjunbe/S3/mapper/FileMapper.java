package com.jeonsaeyukjun.jeonsaeyukjunbe.S3.mapper;

import com.jeonsaeyukjun.jeonsaeyukjunbe.S3.Dto.FileDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FileMapper {
    @Insert("INSERT INTO Contract (contract_name, contract_url, user_id, report_id) VALUES (#{contractName}, #{contractUrl}, #{userId}, #{reportId})")
    void insertContract(FileDto fileDto);

    @Select("SELECT * FROM Contract WHERE contract_name = #{contractName}")
    FileDto findByContractName(String contractName);
}

