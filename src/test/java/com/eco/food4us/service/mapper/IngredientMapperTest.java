package com.eco.food4us.service.mapper;

import static com.eco.food4us.domain.IngredientAsserts.*;
import static com.eco.food4us.domain.IngredientTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IngredientMapperTest {

    private IngredientMapper ingredientMapper;

    @BeforeEach
    void setUp() {
        ingredientMapper = new IngredientMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getIngredientSample1();
        var actual = ingredientMapper.toEntity(ingredientMapper.toDto(expected));
        assertIngredientAllPropertiesEquals(expected, actual);
    }
}
