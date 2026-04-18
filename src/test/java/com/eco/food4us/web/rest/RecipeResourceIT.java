package com.eco.food4us.web.rest;

import static com.eco.food4us.domain.RecipeAsserts.*;
import static com.eco.food4us.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.eco.food4us.IntegrationTest;
import com.eco.food4us.domain.Recipe;
import com.eco.food4us.repository.RecipeRepository;
import com.eco.food4us.service.dto.RecipeDTO;
import com.eco.food4us.service.mapper.RecipeMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RecipeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RecipeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_PREPARATION_TIME = 0;
    private static final Integer UPDATED_PREPARATION_TIME = 1;

    private static final Integer DEFAULT_CALORIES = 0;
    private static final Integer UPDATED_CALORIES = 1;

    private static final String ENTITY_API_URL = "/api/recipes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeMapper recipeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRecipeMockMvc;

    private Recipe recipe;

    private Recipe insertedRecipe;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recipe createEntity() {
        return new Recipe()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .preparationTime(DEFAULT_PREPARATION_TIME)
            .calories(DEFAULT_CALORIES);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recipe createUpdatedEntity() {
        return new Recipe()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .preparationTime(UPDATED_PREPARATION_TIME)
            .calories(UPDATED_CALORIES);
    }

    @BeforeEach
    void initTest() {
        recipe = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedRecipe != null) {
            recipeRepository.delete(insertedRecipe);
            insertedRecipe = null;
        }
    }

    @Test
    @Transactional
    void createRecipe() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Recipe
        RecipeDTO recipeDTO = recipeMapper.toDto(recipe);
        var returnedRecipeDTO = om.readValue(
            restRecipeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recipeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RecipeDTO.class
        );

        // Validate the Recipe in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRecipe = recipeMapper.toEntity(returnedRecipeDTO);
        assertRecipeUpdatableFieldsEquals(returnedRecipe, getPersistedRecipe(returnedRecipe));

        insertedRecipe = returnedRecipe;
    }

    @Test
    @Transactional
    void createRecipeWithExistingId() throws Exception {
        // Create the Recipe with an existing ID
        recipe.setId(1L);
        RecipeDTO recipeDTO = recipeMapper.toDto(recipe);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecipeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recipeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Recipe in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recipe.setName(null);

        // Create the Recipe, which fails.
        RecipeDTO recipeDTO = recipeMapper.toDto(recipe);

        restRecipeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recipeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRecipes() throws Exception {
        // Initialize the database
        insertedRecipe = recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList
        restRecipeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recipe.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].preparationTime").value(hasItem(DEFAULT_PREPARATION_TIME)))
            .andExpect(jsonPath("$.[*].calories").value(hasItem(DEFAULT_CALORIES)));
    }

    @Test
    @Transactional
    void getRecipe() throws Exception {
        // Initialize the database
        insertedRecipe = recipeRepository.saveAndFlush(recipe);

        // Get the recipe
        restRecipeMockMvc
            .perform(get(ENTITY_API_URL_ID, recipe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(recipe.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.preparationTime").value(DEFAULT_PREPARATION_TIME))
            .andExpect(jsonPath("$.calories").value(DEFAULT_CALORIES));
    }

    @Test
    @Transactional
    void getNonExistingRecipe() throws Exception {
        // Get the recipe
        restRecipeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRecipe() throws Exception {
        // Initialize the database
        insertedRecipe = recipeRepository.saveAndFlush(recipe);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recipe
        Recipe updatedRecipe = recipeRepository.findById(recipe.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRecipe are not directly saved in db
        em.detach(updatedRecipe);
        updatedRecipe
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .preparationTime(UPDATED_PREPARATION_TIME)
            .calories(UPDATED_CALORIES);
        RecipeDTO recipeDTO = recipeMapper.toDto(updatedRecipe);

        restRecipeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recipeDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recipeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Recipe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRecipeToMatchAllProperties(updatedRecipe);
    }

    @Test
    @Transactional
    void putNonExistingRecipe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recipe.setId(longCount.incrementAndGet());

        // Create the Recipe
        RecipeDTO recipeDTO = recipeMapper.toDto(recipe);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecipeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recipeDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recipeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recipe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRecipe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recipe.setId(longCount.incrementAndGet());

        // Create the Recipe
        RecipeDTO recipeDTO = recipeMapper.toDto(recipe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recipeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recipe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRecipe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recipe.setId(longCount.incrementAndGet());

        // Create the Recipe
        RecipeDTO recipeDTO = recipeMapper.toDto(recipe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recipeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Recipe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRecipeWithPatch() throws Exception {
        // Initialize the database
        insertedRecipe = recipeRepository.saveAndFlush(recipe);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recipe using partial update
        Recipe partialUpdatedRecipe = new Recipe();
        partialUpdatedRecipe.setId(recipe.getId());

        partialUpdatedRecipe.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).preparationTime(UPDATED_PREPARATION_TIME);

        restRecipeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecipe.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRecipe))
            )
            .andExpect(status().isOk());

        // Validate the Recipe in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecipeUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedRecipe, recipe), getPersistedRecipe(recipe));
    }

    @Test
    @Transactional
    void fullUpdateRecipeWithPatch() throws Exception {
        // Initialize the database
        insertedRecipe = recipeRepository.saveAndFlush(recipe);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recipe using partial update
        Recipe partialUpdatedRecipe = new Recipe();
        partialUpdatedRecipe.setId(recipe.getId());

        partialUpdatedRecipe
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .preparationTime(UPDATED_PREPARATION_TIME)
            .calories(UPDATED_CALORIES);

        restRecipeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecipe.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRecipe))
            )
            .andExpect(status().isOk());

        // Validate the Recipe in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecipeUpdatableFieldsEquals(partialUpdatedRecipe, getPersistedRecipe(partialUpdatedRecipe));
    }

    @Test
    @Transactional
    void patchNonExistingRecipe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recipe.setId(longCount.incrementAndGet());

        // Create the Recipe
        RecipeDTO recipeDTO = recipeMapper.toDto(recipe);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecipeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, recipeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recipeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recipe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRecipe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recipe.setId(longCount.incrementAndGet());

        // Create the Recipe
        RecipeDTO recipeDTO = recipeMapper.toDto(recipe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recipeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recipe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRecipe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recipe.setId(longCount.incrementAndGet());

        // Create the Recipe
        RecipeDTO recipeDTO = recipeMapper.toDto(recipe);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(recipeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Recipe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRecipe() throws Exception {
        // Initialize the database
        insertedRecipe = recipeRepository.saveAndFlush(recipe);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the recipe
        restRecipeMockMvc
            .perform(delete(ENTITY_API_URL_ID, recipe.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return recipeRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Recipe getPersistedRecipe(Recipe recipe) {
        return recipeRepository.findById(recipe.getId()).orElseThrow();
    }

    protected void assertPersistedRecipeToMatchAllProperties(Recipe expectedRecipe) {
        assertRecipeAllPropertiesEquals(expectedRecipe, getPersistedRecipe(expectedRecipe));
    }

    protected void assertPersistedRecipeToMatchUpdatableProperties(Recipe expectedRecipe) {
        assertRecipeAllUpdatablePropertiesEquals(expectedRecipe, getPersistedRecipe(expectedRecipe));
    }
}
