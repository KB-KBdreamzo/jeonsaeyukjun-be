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

    public List<ReportDto> getReports(Long userId) {
        List<ReportDto> reports = mypageMapper.getReports(userId);
        return reports;
    }

    public List<ReportDto> getLimitReports(Long userId, int limit) {
        List<ReportDto> reports = mypageMapper.getLimitReports(userId, limit);
        return reports;
    }

    // 새로운 페이징 메서드
    public Map<String, Object> getReportsWithPaging(Long userId, String sortKey, String query, int page, int size) {
        int offset = (page - 1) * size; // OFFSET 계산
        List<ReportDto> reports = mypageMapper.getReportsWithPaging(userId, sortKey, query, size, offset); // 페이징 처리된 리포트 목록
        int totalCount = mypageMapper.getTotalReportCount(userId, query); // 전체 리포트 개수

        // 결과를 Map으로 반환하여 컨트롤러에서 사용할 수 있도록 함
        Map<String, Object> result = new HashMap<>();
        result.put("reports", reports); // 페이징된 리포트 목록
        result.put("totalPages", (int) Math.ceil((double) totalCount / size)); // 전체 페이지 수 계산
        return result;
    }
}
