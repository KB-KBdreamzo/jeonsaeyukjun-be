package com.jeonsaeyukjun.jeonsaeyukjunbe.contract.mapper;

import com.jeonsaeyukjun.jeonsaeyukjunbe.contract.dto.ContractDTO;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ContractMapper {

    public Map<String, String> mapToTemplate(ContractDTO dto) {
        Map<String, String> data = new HashMap<>();
        data.put("{{address}}", dto.getAddress() != null ? dto.getAddress() : "");
        data.put("{{tenant}}", dto.getTenant() != null ? dto.getTenant() : "");
        data.put("{{landlord}}", dto.getLandlord() != null ? dto.getLandlord() : "");
        data.put("{{rent}}", dto.getRent() != null ? dto.getRent() : "");
        return data;
    }
}
