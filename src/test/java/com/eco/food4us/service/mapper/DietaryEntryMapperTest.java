package com.eco.food4us.service.mapper;

import static com.eco.food4us.domain.DietaryEntryAsserts.*;
import static com.eco.food4us.domain.DietaryEntryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DietaryEntryMapperTest {

    private DietaryEntryMapper dietaryEntryMapper;

    @BeforeEach
    void setUp() {
        dietaryEntryMapper = new DietaryEntryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDietaryEntrySample1();
        var actual = dietaryEntryMapper.toEntity(dietaryEntryMapper.toDto(expected));
        assertDietaryEntryAllPropertiesEquals(expected, actual);
    }
}
