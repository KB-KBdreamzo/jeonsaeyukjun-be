package com.jeonsaeyukjun.jeonsaeyukjunbe.mypage.service;

import com.jeonsaeyukjun.jeonsaeyukjunbe.mypage.mapper.MypageMapper;
import com.jeonsaeyukjun.jeonsaeyukjunbe.report.Dto.ReportDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MypageService {
    private final MypageMapper mypageMapper;

    public List<ReportDto> getReports(Long userId) {
        List<ReportDto> reports = mypageMapper.getReports(userId);
        return reports;
    }
}
