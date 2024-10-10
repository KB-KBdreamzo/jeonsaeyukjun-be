package com.jeonsaeyukjun.jeonsaeyukjunbe.mypage.mapper;

import com.jeonsaeyukjun.jeonsaeyukjunbe.report.Dto.ReportDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MypageMapper {
    List<ReportDto> fetchReportList(
            @Param("userId") Long userId,
            @Param("sortKey") String sortKey,
            @Param("query") String query,
            @Param("size") int size,
            @Param("offset") int offset
    );
    int fetchTotalReportCount(@Param("userId") Long userId, @Param("query") String query);
}
