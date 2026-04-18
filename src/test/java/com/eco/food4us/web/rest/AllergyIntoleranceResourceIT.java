package com.eco.food4us.web.rest;

import static com.eco.food4us.domain.AllergyIntoleranceAsserts.*;
import static com.eco.food4us.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.eco.food4us.IntegrationTest;
import com.eco.food4us.domain.AllergyIntolerance;
import com.eco.food4us.domain.enumeration.AllergyIntoleranceType;
import com.eco.food4us.repository.AllergyIntoleranceRepository;
import com.eco.food4us.service.dto.AllergyIntoleranceDTO;
import com.eco.food4us.service.mapper.AllergyIntoleranceMapper;
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
 * Integration tests for the {@link AllergyIntoleranceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AllergyIntoleranceResourceIT {

    private static final AllergyIntoleranceType DEFAULT_TYPE = AllergyIntoleranceType.LACTOSE;
    private static final AllergyIntoleranceType UPDATED_TYPE = AllergyIntoleranceType.GLUTEN;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/allergy-intolerances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AllergyIntoleranceRepository allergyIntoleranceRepository;

    @Autowired
    private AllergyIntoleranceMapper allergyIntoleranceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAllergyIntoleranceMockMvc;

    private AllergyIntolerance allergyIntolerance;

    private AllergyIntolerance insertedAllergyIntolerance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AllergyIntolerance createEntity() {
        return new AllergyIntolerance().type(DEFAULT_TYPE).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AllergyIntolerance createUpdatedEntity() {
        return new AllergyIntolerance().type(UPDATED_TYPE).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        allergyIntolerance = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAllergyIntolerance != null) {
            allergyIntoleranceRepository.delete(insertedAllergyIntolerance);
            insertedAllergyIntolerance = null;
        }
    }

    @Test
    @Transactional
    void createAllergyIntolerance() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AllergyIntolerance
        AllergyIntoleranceDTO allergyIntoleranceDTO = allergyIntoleranceMapper.toDto(allergyIntolerance);
        var returnedAllergyIntoleranceDTO = om.readValue(
            restAllergyIntoleranceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(allergyIntoleranceDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AllergyIntoleranceDTO.class
        );

        // Validate the AllergyIntolerance in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAllergyIntolerance = allergyIntoleranceMapper.toEntity(returnedAllergyIntoleranceDTO);
        assertAllergyIntoleranceUpdatableFieldsEquals(
            returnedAllergyIntolerance,
            getPersistedAllergyIntolerance(returnedAllergyIntolerance)
        );

        insertedAllergyIntolerance = returnedAllergyIntolerance;
    }

    @Test
    @Transactional
    void createAllergyIntoleranceWithExistingId() throws Exception {
        // Create the AllergyIntolerance with an existing ID
        allergyIntolerance.setId(1L);
        AllergyIntoleranceDTO allergyIntoleranceDTO = allergyIntoleranceMapper.toDto(allergyIntolerance);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAllergyIntoleranceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(allergyIntoleranceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AllergyIntolerance in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        allergyIntolerance.setType(null);

        // Create the AllergyIntolerance, which fails.
        AllergyIntoleranceDTO allergyIntoleranceDTO = allergyIntoleranceMapper.toDto(allergyIntolerance);

        restAllergyIntoleranceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(allergyIntoleranceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAllergyIntolerances() throws Exception {
        // Initialize the database
        insertedAllergyIntolerance = allergyIntoleranceRepository.saveAndFlush(allergyIntolerance);

        // Get all the allergyIntoleranceList
        restAllergyIntoleranceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(allergyIntolerance.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getAllergyIntolerance() throws Exception {
        // Initialize the database
        insertedAllergyIntolerance = allergyIntoleranceRepository.saveAndFlush(allergyIntolerance);

        // Get the allergyIntolerance
        restAllergyIntoleranceMockMvc
            .perform(get(ENTITY_API_URL_ID, allergyIntolerance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(allergyIntolerance.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingAllergyIntolerance() throws Exception {
        // Get the allergyIntolerance
        restAllergyIntoleranceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAllergyIntolerance() throws Exception {
        // Initialize the database
        insertedAllergyIntolerance = allergyIntoleranceRepository.saveAndFlush(allergyIntolerance);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the allergyIntolerance
        AllergyIntolerance updatedAllergyIntolerance = allergyIntoleranceRepository.findById(allergyIntolerance.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAllergyIntolerance are not directly saved in db
        em.detach(updatedAllergyIntolerance);
        updatedAllergyIntolerance.type(UPDATED_TYPE).description(UPDATED_DESCRIPTION);
        AllergyIntoleranceDTO allergyIntoleranceDTO = allergyIntoleranceMapper.toDto(updatedAllergyIntolerance);

        restAllergyIntoleranceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, allergyIntoleranceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(allergyIntoleranceDTO))
            )
            .andExpect(status().isOk());

        // Validate the AllergyIntolerance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAllergyIntoleranceToMatchAllProperties(updatedAllergyIntolerance);
    }

    @Test
    @Transactional
    void putNonExistingAllergyIntolerance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        allergyIntolerance.setId(longCount.incrementAndGet());

        // Create the AllergyIntolerance
        AllergyIntoleranceDTO allergyIntoleranceDTO = allergyIntoleranceMapper.toDto(allergyIntolerance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAllergyIntoleranceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, allergyIntoleranceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(allergyIntoleranceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AllergyIntolerance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAllergyIntolerance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        allergyIntolerance.setId(longCount.incrementAndGet());

        // Create the AllergyIntolerance
        AllergyIntoleranceDTO allergyIntoleranceDTO = allergyIntoleranceMapper.toDto(allergyIntolerance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAllergyIntoleranceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(allergyIntoleranceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AllergyIntolerance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAllergyIntolerance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        allergyIntolerance.setId(longCount.incrementAndGet());

        // Create the AllergyIntolerance
        AllergyIntoleranceDTO allergyIntoleranceDTO = allergyIntoleranceMapper.toDto(allergyIntolerance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAllergyIntoleranceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(allergyIntoleranceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AllergyIntolerance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAllergyIntoleranceWithPatch() throws Exception {
        // Initialize the database
        insertedAllergyIntolerance = allergyIntoleranceRepository.saveAndFlush(allergyIntolerance);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the allergyIntolerance using partial update
        AllergyIntolerance partialUpdatedAllergyIntolerance = new AllergyIntolerance();
        partialUpdatedAllergyIntolerance.setId(allergyIntolerance.getId());

        partialUpdatedAllergyIntolerance.description(UPDATED_DESCRIPTION);

        restAllergyIntoleranceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAllergyIntolerance.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAllergyIntolerance))
            )
            .andExpect(status().isOk());

        // Validate the AllergyIntolerance in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAllergyIntoleranceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAllergyIntolerance, allergyIntolerance),
            getPersistedAllergyIntolerance(allergyIntolerance)
        );
    }

    @Test
    @Transactional
    void fullUpdateAllergyIntoleranceWithPatch() throws Exception {
        // Initialize the database
        insertedAllergyIntolerance = allergyIntoleranceRepository.saveAndFlush(allergyIntolerance);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the allergyIntolerance using partial update
        AllergyIntolerance partialUpdatedAllergyIntolerance = new AllergyIntolerance();
        partialUpdatedAllergyIntolerance.setId(allergyIntolerance.getId());

        partialUpdatedAllergyIntolerance.type(UPDATED_TYPE).description(UPDATED_DESCRIPTION);

        restAllergyIntoleranceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAllergyIntolerance.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAllergyIntolerance))
            )
            .andExpect(status().isOk());

        // Validate the AllergyIntolerance in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAllergyIntoleranceUpdatableFieldsEquals(
            partialUpdatedAllergyIntolerance,
            getPersistedAllergyIntolerance(partialUpdatedAllergyIntolerance)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAllergyIntolerance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        allergyIntolerance.setId(longCount.incrementAndGet());

        // Create the AllergyIntolerance
        AllergyIntoleranceDTO allergyIntoleranceDTO = allergyIntoleranceMapper.toDto(allergyIntolerance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAllergyIntoleranceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, allergyIntoleranceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(allergyIntoleranceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AllergyIntolerance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAllergyIntolerance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        allergyIntolerance.setId(longCount.incrementAndGet());

        // Create the AllergyIntolerance
        AllergyIntoleranceDTO allergyIntoleranceDTO = allergyIntoleranceMapper.toDto(allergyIntolerance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAllergyIntoleranceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(allergyIntoleranceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AllergyIntolerance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAllergyIntolerance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        allergyIntolerance.setId(longCount.incrementAndGet());

        // Create the AllergyIntolerance
        AllergyIntoleranceDTO allergyIntoleranceDTO = allergyIntoleranceMapper.toDto(allergyIntolerance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAllergyIntoleranceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(allergyIntoleranceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AllergyIntolerance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAllergyIntolerance() throws Exception {
        // Initialize the database
        insertedAllergyIntolerance = allergyIntoleranceRepository.saveAndFlush(allergyIntolerance);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the allergyIntolerance
        restAllergyIntoleranceMockMvc
            .perform(delete(ENTITY_API_URL_ID, allergyIntolerance.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return allergyIntoleranceRepository.count();
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

    protected AllergyIntolerance getPersistedAllergyIntolerance(AllergyIntolerance allergyIntolerance) {
        return allergyIntoleranceRepository.findById(allergyIntolerance.getId()).orElseThrow();
    }

    protected void assertPersistedAllergyIntoleranceToMatchAllProperties(AllergyIntolerance expectedAllergyIntolerance) {
        assertAllergyIntoleranceAllPropertiesEquals(expectedAllergyIntolerance, getPersistedAllergyIntolerance(expectedAllergyIntolerance));
    }

    protected void assertPersistedAllergyIntoleranceToMatchUpdatableProperties(AllergyIntolerance expectedAllergyIntolerance) {
        assertAllergyIntoleranceAllUpdatablePropertiesEquals(
            expectedAllergyIntolerance,
            getPersistedAllergyIntolerance(expectedAllergyIntolerance)
        );
    }
}
