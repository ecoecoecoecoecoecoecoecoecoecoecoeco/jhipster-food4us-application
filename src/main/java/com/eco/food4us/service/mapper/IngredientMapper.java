package com.eco.food4us.service.mapper;

import com.eco.food4us.domain.Ingredient;
import com.eco.food4us.domain.Recipe;
import com.eco.food4us.service.dto.IngredientDTO;
import com.eco.food4us.service.dto.RecipeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ingredient} and its DTO {@link IngredientDTO}.
 */
@Mapper(componentModel = "spring")
public interface IngredientMapper extends EntityMapper<IngredientDTO, Ingredient> {
    @Mapping(target = "recipe", source = "recipe", qualifiedByName = "recipeName")
    IngredientDTO toDto(Ingredient s);

    @Named("recipeName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    RecipeDTO toDtoRecipeName(Recipe recipe);
}
