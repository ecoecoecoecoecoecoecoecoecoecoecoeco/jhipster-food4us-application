package com.eco.food4us.domain;

import static com.eco.food4us.domain.IngredientTestSamples.*;
import static com.eco.food4us.domain.RecipeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.eco.food4us.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RecipeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Recipe.class);
        Recipe recipe1 = getRecipeSample1();
        Recipe recipe2 = new Recipe();
        assertThat(recipe1).isNotEqualTo(recipe2);

        recipe2.setId(recipe1.getId());
        assertThat(recipe1).isEqualTo(recipe2);

        recipe2 = getRecipeSample2();
        assertThat(recipe1).isNotEqualTo(recipe2);
    }

    @Test
    void ingredientTest() {
        Recipe recipe = getRecipeRandomSampleGenerator();
        Ingredient ingredientBack = getIngredientRandomSampleGenerator();

        recipe.addIngredient(ingredientBack);
        assertThat(recipe.getIngredients()).containsOnly(ingredientBack);
        assertThat(ingredientBack.getRecipe()).isEqualTo(recipe);

        recipe.removeIngredient(ingredientBack);
        assertThat(recipe.getIngredients()).doesNotContain(ingredientBack);
        assertThat(ingredientBack.getRecipe()).isNull();

        recipe.ingredients(new HashSet<>(Set.of(ingredientBack)));
        assertThat(recipe.getIngredients()).containsOnly(ingredientBack);
        assertThat(ingredientBack.getRecipe()).isEqualTo(recipe);

        recipe.setIngredients(new HashSet<>());
        assertThat(recipe.getIngredients()).doesNotContain(ingredientBack);
        assertThat(ingredientBack.getRecipe()).isNull();
    }
}
