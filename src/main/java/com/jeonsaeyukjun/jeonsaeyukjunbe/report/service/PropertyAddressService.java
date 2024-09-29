package com.jeonsaeyukjun.jeonsaeyukjunbe.report.service;

import com.jeonsaeyukjun.jeonsaeyukjunbe.report.Dto.PropertyAddressDto;
import com.jeonsaeyukjun.jeonsaeyukjunbe.report.mapper.PropertyAddressMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PropertyAddressService {

    private final PropertyAddressMapper propertyAddressMapper;

    public void addPropertyAddressDto(PropertyAddressDto propertyAddressDto) {
        if (fetchPropertyAddress(propertyAddressDto) == null) {
            propertyAddressMapper.addPropertyAddress(propertyAddressDto);
        }
    }

    public PropertyAddressDto fetchPropertyAddress(PropertyAddressDto propertyAddressDto) {
        return propertyAddressMapper.fetchPropertyAddress(propertyAddressDto);
    }
}

