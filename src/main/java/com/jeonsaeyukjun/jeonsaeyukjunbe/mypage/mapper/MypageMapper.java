package com.jeonsaeyukjun.jeonsaeyukjunbe.mypage.mapper;

import com.jeonsaeyukjun.jeonsaeyukjunbe.report.Dto.ReportDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MypageMapper {
    List<ReportDto> getReports(Long userId);
}
