package com.jeonsaeyukjun.jeonsaeyukjunbe.report.mapper;

import com.jeonsaeyukjun.jeonsaeyukjunbe.report.Dto.PropertyAddressDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PropertyAddressMapper {

    PropertyAddressDto fetchPropertyAddress(PropertyAddressDto propertyAddressDto);

    // PropertyAddressDto를 삽입 (리턴 타입을 void 또는 삽입된 행 수로 수정)
    void addPropertyAddress(PropertyAddressDto propertyAddressDto);
}
