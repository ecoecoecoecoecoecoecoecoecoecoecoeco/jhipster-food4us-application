package com.eco.food4us.service.mapper;

import com.eco.food4us.domain.Recipe;
import com.eco.food4us.service.dto.RecipeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Recipe} and its DTO {@link RecipeDTO}.
 */
@Mapper(componentModel = "spring")
public interface RecipeMapper extends EntityMapper<RecipeDTO, Recipe> {}
