package com.eco.food4us.service.mapper;

import static com.eco.food4us.domain.SupplierAsserts.*;
import static com.eco.food4us.domain.SupplierTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SupplierMapperTest {

    private SupplierMapper supplierMapper;

    @BeforeEach
    void setUp() {
        supplierMapper = new SupplierMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSupplierSample1();
        var actual = supplierMapper.toEntity(supplierMapper.toDto(expected));
        assertSupplierAllPropertiesEquals(expected, actual);
    }
}
