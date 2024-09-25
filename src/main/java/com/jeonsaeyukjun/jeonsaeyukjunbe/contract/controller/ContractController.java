package com.jeonsaeyukjun.jeonsaeyukjunbe.contract.controller;

import com.jeonsaeyukjun.jeonsaeyukjunbe.contract.dto.ContractDTO;
import com.jeonsaeyukjun.jeonsaeyukjunbe.contract.service.ContractService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/contract")
public class ContractController {

    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateContract(@RequestBody ContractDTO contractDTO) {
        try {
            contractService.generatePDF(contractDTO);
            return ResponseEntity.ok("PDF 생성 완료");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("PDF 생성 실패: " + e.getMessage());
        }
    }
}
