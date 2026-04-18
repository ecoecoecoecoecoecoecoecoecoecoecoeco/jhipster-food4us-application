package com.eco.food4us.service.mapper;

import static com.eco.food4us.domain.RecipeAsserts.*;
import static com.eco.food4us.domain.RecipeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecipeMapperTest {

    private RecipeMapper recipeMapper;

    @BeforeEach
    void setUp() {
        recipeMapper = new RecipeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRecipeSample1();
        var actual = recipeMapper.toEntity(recipeMapper.toDto(expected));
        assertRecipeAllPropertiesEquals(expected, actual);
    }
}
