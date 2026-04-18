package com.eco.food4us.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.eco.food4us.domain.Ingredient} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IngredientDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 100)
    private String name;

    @NotNull
    @Size(max = 50)
    private String quantity;

    @NotNull
    private RecipeDTO recipe;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public RecipeDTO getRecipe() {
        return recipe;
    }

    public void setRecipe(RecipeDTO recipe) {
        this.recipe = recipe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IngredientDTO)) {
            return false;
        }

        IngredientDTO ingredientDTO = (IngredientDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ingredientDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IngredientDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", quantity='" + getQuantity() + "'" +
            ", recipe=" + getRecipe() +
            "}";
    }
}
