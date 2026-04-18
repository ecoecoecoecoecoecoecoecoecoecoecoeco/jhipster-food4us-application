package com.eco.food4us.service.mapper;

import static com.eco.food4us.domain.AllergyIntoleranceAsserts.*;
import static com.eco.food4us.domain.AllergyIntoleranceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AllergyIntoleranceMapperTest {

    private AllergyIntoleranceMapper allergyIntoleranceMapper;

    @BeforeEach
    void setUp() {
        allergyIntoleranceMapper = new AllergyIntoleranceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAllergyIntoleranceSample1();
        var actual = allergyIntoleranceMapper.toEntity(allergyIntoleranceMapper.toDto(expected));
        assertAllergyIntoleranceAllPropertiesEquals(expected, actual);
    }
}
