package com.jeonsaeyukjun.jeonsaeyukjunbe.contract.controller;

import com.jeonsaeyukjun.jeonsaeyukjunbe.contract.dto.*;
import com.jeonsaeyukjun.jeonsaeyukjunbe.contract.service.ContractService;
import com.jeonsaeyukjun.jeonsaeyukjunbe.contract.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contract")
public class ContractController {

    @Autowired
    private final ContractService contractService;

    @Autowired
    private final S3Service s3Service;

    @Autowired
    public ContractController(ContractService contractService, S3Service s3Service) {
        this.contractService = contractService;
        this.s3Service = s3Service;
    }

    // 사용자에게서 데이터를 받아 ContractDto 객체 생성
    @PostMapping("/generate")
    public ResponseEntity<String> generateContract(@RequestBody ContractRequestDto contractRequestDto) {
        try {
            int userId = contractRequestDto.getUserId();
            ContractDto contractDto = contractRequestDto.getContractDto();
            OwnershipInfoDto ownershipInfoDto = contractRequestDto.getOwnershipInfoDto();

            if (ownershipInfoDto == null) {
                System.out.println("OwnershipInfoDto가 null이므로 관련 데이터 없이 PDF를 생성합니다.");
            } else {
                System.out.println("OwnershipInfoDto: " + ownershipInfoDto);
            }

            // 서비스 계층으로 ContractDto와 OwnershipInfoDto를 전달하여 로직 처리
            contractService.generatePDF(contractDto, ownershipInfoDto, userId);

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

    // 계약서 목록 조회
    @GetMapping("/list/{userId}")
    public ResponseEntity<List<ContractWithReportDto>> getContractsByUserId(@PathVariable int userId) {
        List<ContractWithReportDto> contracts = contractService.getContractsByUserId(userId);
        if (contracts.isEmpty()) {
            return ResponseEntity.noContent().build(); // 데이터가 없으면 No Content 응답
        } else {
            return ResponseEntity.ok(contracts); // 계약서 목록 반환
        }
    }

    // S3 Presigned URL 생성
    @GetMapping("/presigned-url/{fileName}")
    public String getPresignedUrl(@PathVariable String fileName) {
        return s3Service.generatePresignedUrl(fileName);
    }

    // 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteContract(@RequestBody DeleteContractRequestDto request) {
        try {
            contractService.deleteContract(request.getUserId(), request.getContractName());
            return ResponseEntity.ok("계약서가 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("계약서 삭제에 실패했습니다.");
        }
    }

}
