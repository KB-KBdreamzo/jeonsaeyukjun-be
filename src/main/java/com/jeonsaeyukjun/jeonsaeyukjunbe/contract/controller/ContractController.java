package com.jeonsaeyukjun.jeonsaeyukjunbe.contract.controller;

import com.jeonsaeyukjun.jeonsaeyukjunbe.contract.dto.ContractDto;
import com.jeonsaeyukjun.jeonsaeyukjunbe.contract.dto.ContractRequestDto;
import com.jeonsaeyukjun.jeonsaeyukjunbe.contract.dto.OwnershipInfoDto;
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
    public ResponseEntity<String> generateContract(@RequestBody ContractRequestDto contractRequestDto) {
        try {
            System.out.println(contractRequestDto);
            ContractDto contractDto = contractRequestDto.getContractDto();
            OwnershipInfoDto ownershipInfoDto = contractRequestDto.getOwnershipInfoDto();
            System.out.println(contractDto);
            System.out.println(ownershipInfoDto);
            System.out.println("====================================================");

            // 서비스 계층으로 ContractDto와 OwnershipInfoDto를 전달하여 로직 처리
            contractService.generatePDF(contractDto, ownershipInfoDto);

            return ResponseEntity.ok("PDF 생성 완료");
        } catch (Exception e) {
            e.printStackTrace();  // 에러 로그를 남겨 문제 파악
            return ResponseEntity.status(500).body("PDF 생성 실패: " + e.getMessage());
        }
    }

    // reportId에 따라 Ownership_Info 데이터 조회
    @GetMapping("/ownership-info/{reportId}")
    public ResponseEntity<OwnershipInfoDto> getOwnershipInfo(@PathVariable int reportId) {
        System.out.println("reportId: " + reportId);
        OwnershipInfoDto ownershipInfo = contractService.getOwnershipInfo(reportId);
        System.out.println("OwnershipInfo from Controller: " + ownershipInfo);
        if (ownershipInfo != null) {
            return ResponseEntity.ok(ownershipInfo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
