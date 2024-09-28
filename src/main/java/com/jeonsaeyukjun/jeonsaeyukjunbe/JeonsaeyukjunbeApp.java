package com.jeonsaeyukjun.jeonsaeyukjunbe;

import com.jeonsaeyukjun.jeonsaeyukjunbe.config.AppConfig;
import com.jeonsaeyukjun.jeonsaeyukjunbe.contract.dto.ContractDTO;
import com.jeonsaeyukjun.jeonsaeyukjunbe.contract.mapper.ContractMapper;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;

public class JeonsaeyukjunbeApp {

    public static void main(String[] args) {
        // AnnotationConfigApplicationContext로 수동 설정
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // ContractMapper 빈 가져오기
        ContractMapper contractMapper = context.getBean(ContractMapper.class);

        // ContractDTO 생성 및 매핑 예시
        ContractDTO dto = new ContractDTO();
        // 필요한 필드 설정
        Map<String, String> mappedData = contractMapper.mapToTemplate(dto);

        // 맵핑된 데이터 출력
        System.out.println(mappedData);

        // 종료 시 컨텍스트 닫기
        context.close();
    }
}
