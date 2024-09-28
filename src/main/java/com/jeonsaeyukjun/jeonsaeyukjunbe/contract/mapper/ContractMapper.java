//package com.jeonsaeyukjun.jeonsaeyukjunbe.contract.mapper;
//
//import com.jeonsaeyukjun.jeonsaeyukjunbe.contract.dto.ContractDTO;
//import org.apache.ibatis.annotations.Mapper;
//
//import java.util.Map;
//
//@Mapper
//public interface ContractMapper {
//    Map<String, String> mapToTemplate(ContractDTO dto);
//}


package com.jeonsaeyukjun.jeonsaeyukjunbe.contract.mapper;

import com.jeonsaeyukjun.jeonsaeyukjunbe.contract.dto.ContractDTO;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ContractMapper {

    public Map<String, String> mapToTemplate(ContractDTO dto) {
        Map<String, String> data = new HashMap<>();

        // 임대인 이름
        data.put("{{landlordName}}", dto.getLandlordName() != null ? dto.getLandlordName() : "");
        // 임차인 이름
        data.put("{{tenantName}}", dto.getTenantName() != null ? dto.getTenantName() : "");
        // 계약 주소
        data.put("{{address}}", dto.getAddress() != null ? dto.getAddress() : "");
        // 토지 지목
        data.put("{{landType}}", dto.getLandType() != null ? dto.getLandType() : "");
        // 토지 면적
        data.put("{{landArea}}", String.valueOf(dto.getLandArea()));
        // 건물 구조 및 용도
        data.put("{{structurePurpose}}", dto.getStructurePurpose() != null ? dto.getStructurePurpose() : "");
        // 건물 면적
        data.put("{{buildingArea}}", String.valueOf(dto.getBuildingArea()));
        // 임차할 부분
        data.put("{{leasedPortion}}", dto.getLeasedPortion() != null ? dto.getLeasedPortion() : "");
        // 임차할 부분 면적
        data.put("{{leasedPortionArea}}", String.valueOf(dto.getLeasedPortionArea()));
        // 계약 종류
        data.put("{{contractType}}", dto.getContractType() != null ? dto.getContractType() : "");

        // 계약 시작일
        data.put("{{lsSY}}", String.valueOf(dto.getLsSY()));
        data.put("{{lsSM}}", String.valueOf(dto.getLsSM()));
        data.put("{{lsSD}}", String.valueOf(dto.getLsSD()));

        // 계약 종료일
        data.put("{{lsEY}}", String.valueOf(dto.getLsEY()));
        data.put("{{lsEM}}", String.valueOf(dto.getLsEM()));
        data.put("{{lsED}}", String.valueOf(dto.getLsED()));

        // 미납 세금 내역
        data.put("{{unpaidNationalAndLocalTax}}", dto.getUnpaidNationalAndLocalTax() != null ? dto.getUnpaidNationalAndLocalTax() : "");
        // 선순위 확정일자 내역
        data.put("{{priorityConfirmedDateDetails}}", dto.getPriorityConfirmedDateDetails() != null ? dto.getPriorityConfirmedDateDetails() : "");
        // 계약 확정일자 부여일
        data.put("{{contractConfirmationDate}}", dto.getContractConfirmationDate() != null ? dto.getContractConfirmationDate() : "");
        // 입금 계좌 정보
        data.put("{{paymentAccount}}", dto.getPaymentAccount() != null ? dto.getPaymentAccount() : "");
        // 보증금
        data.put("{{depositAmount}}", String.valueOf(dto.getDepositAmount()));
        // 계약금
        data.put("{{downPayment}}", String.valueOf(dto.getDownPayment()));
        // 중도금
        data.put("{{interimPayment}}", String.valueOf(dto.getInterimPayment()));
        // 잔금
        data.put("{{finalPayment}}", String.valueOf(dto.getFinalPayment()));

        // 중도금 지급일
        data.put("{{interYear}}", String.valueOf(dto.getInterYear()));
        data.put("{{interMonth}}", String.valueOf(dto.getInterMonth()));
        data.put("{{interDay}}", String.valueOf(dto.getInterDay()));

        // 잔금 지급일
        data.put("{{finalYear}}", String.valueOf(dto.getFinalYear()));
        data.put("{{finalMonth}}", String.valueOf(dto.getFinalMonth()));
        data.put("{{finalDay}}", String.valueOf(dto.getFinalDay()));

        // 관리비
        data.put("{{managementFee}}", String.valueOf(dto.getManagementFee()));
        // 수리 필요 사항
        data.put("{{repairDetails}}", dto.getRepairDetails() != null ? dto.getRepairDetails() : "");
        data.put("{{taxAmount}}", String.valueOf(dto.getDepositAmount() / 10));

        // 임대인 정보
        data.put("{{landlordAddress}}", dto.getLandlordAddress() != null ? dto.getLandlordAddress() : "");
        data.put("{{landlordResidentId}}", dto.getLandlordResidentId() != null ? dto.getLandlordResidentId() : "");
        data.put("{{landlordPhone}}", dto.getLandlordPhone() != null ? dto.getLandlordPhone() : "");

        // 임차인 정보
        data.put("{{tenantAddress}}", dto.getTenantAddress() != null ? dto.getTenantAddress() : "");
        data.put("{{tenantResidentId}}", dto.getTenantResidentId() != null ? dto.getTenantResidentId() : "");
        data.put("{{tenantPhone}}", dto.getTenantPhone() != null ? dto.getTenantPhone() : "");

        // 오늘 날짜
        data.put("{{todayYear}}", String.valueOf(dto.getTodayYear()));
        data.put("{{todayMonth}}", String.valueOf(dto.getTodayMonth()));
        data.put("{{todayDay}}", String.valueOf(dto.getTodayDay()));

        return data;
    }
}
