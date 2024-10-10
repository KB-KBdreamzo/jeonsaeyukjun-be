package com.jeonsaeyukjun.jeonsaeyukjunbe.mypage.service;

import com.jeonsaeyukjun.jeonsaeyukjunbe.mypage.mapper.MypageMapper;
import com.jeonsaeyukjun.jeonsaeyukjunbe.report.Dto.ReportDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MypageService {
    private final MypageMapper mypageMapper;

    public Map<String, Object> fetchReportList(Long userId, String sortKey, String query, int page, int size) {
        int offset = (page - 1) * size;
        List<ReportDto> reports = mypageMapper.fetchReportList(userId, sortKey, query, size, offset);
        int totalCount = mypageMapper.fetchTotalReportCount(userId, query);

        Map<String, Object> result = new HashMap<>();
        result.put("reports", reports);
        result.put("totalPages", (int) Math.ceil((double) totalCount / size));
        return result;
    }
}
