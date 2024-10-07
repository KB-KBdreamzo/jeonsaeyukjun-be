package com.jeonsaeyukjun.jeonsaeyukjunbe.contract.controller;

import com.jeonsaeyukjun.jeonsaeyukjunbe.contract.dto.ContractDto;
import com.jeonsaeyukjun.jeonsaeyukjunbe.contract.dto.SpecialContractDto;
import com.jeonsaeyukjun.jeonsaeyukjunbe.contract.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Restful API를 제공하는 컨트롤러임을 나타냄
@RequestMapping("/contract") // 기본 URL 경로를 설정함
public class ContractController {

    @Autowired
    private final ContractService contractService;

    @Autowired
    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    // 사용자에게서 데이터를 받아 ContractDto 객체 생성
    @PostMapping("/generate")
    public ResponseEntity<String> generateContract(@RequestBody ContractDto contractDTO) {
        try {
            // 서비스 계층으로 ContractDto를 전달하여 로직 처리
            contractService.generatePDF(contractDTO, "경매 넘어간 횟수");
            return ResponseEntity.ok("PDF 생성 완료");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("PDF 생성 실패: " + e.getMessage());
        }
    }
}
