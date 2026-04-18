package com.eco.food4us.web.rest;

import static com.eco.food4us.domain.DietaryEntryAsserts.*;
import static com.eco.food4us.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.eco.food4us.IntegrationTest;
import com.eco.food4us.domain.DietaryEntry;
import com.eco.food4us.domain.Product;
import com.eco.food4us.domain.UserProfile;
import com.eco.food4us.repository.DietaryEntryRepository;
import com.eco.food4us.service.DietaryEntryService;
import com.eco.food4us.service.dto.DietaryEntryDTO;
import com.eco.food4us.service.mapper.DietaryEntryMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DietaryEntryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DietaryEntryResourceIT {

    private static final DateTimeFormatter LOCAL_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static final LocalDate DEFAULT_CONSUMPTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CONSUMPTION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CONSUMPTION_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalTime DEFAULT_CONSUMPTION_TIME = LocalTime.NOON;
    private static final LocalTime UPDATED_CONSUMPTION_TIME = LocalTime.MAX.withNano(0);

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;
    private static final Integer SMALLER_QUANTITY = 1 - 1;

    private static final String ENTITY_API_URL = "/api/dietary-entries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DietaryEntryRepository dietaryEntryRepository;

    @Mock
    private DietaryEntryRepository dietaryEntryRepositoryMock;

    @Autowired
    private DietaryEntryMapper dietaryEntryMapper;

    @Mock
    private DietaryEntryService dietaryEntryServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDietaryEntryMockMvc;

    private DietaryEntry dietaryEntry;

    private DietaryEntry insertedDietaryEntry;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DietaryEntry createEntity(EntityManager em) {
        DietaryEntry dietaryEntry = new DietaryEntry()
            .consumptionDate(DEFAULT_CONSUMPTION_DATE)
            .consumptionTime(DEFAULT_CONSUMPTION_TIME)
            .quantity(DEFAULT_QUANTITY);
        // Add required entity
        UserProfile userProfile;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            userProfile = UserProfileResourceIT.createEntity();
            em.persist(userProfile);
            em.flush();
        } else {
            userProfile = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        dietaryEntry.setUserProfile(userProfile);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        dietaryEntry.setProduct(product);
        return dietaryEntry;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DietaryEntry createUpdatedEntity(EntityManager em) {
        DietaryEntry updatedDietaryEntry = new DietaryEntry()
            .consumptionDate(UPDATED_CONSUMPTION_DATE)
            .consumptionTime(UPDATED_CONSUMPTION_TIME)
            .quantity(UPDATED_QUANTITY);
        // Add required entity
        UserProfile userProfile;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            userProfile = UserProfileResourceIT.createUpdatedEntity();
            em.persist(userProfile);
            em.flush();
        } else {
            userProfile = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        updatedDietaryEntry.setUserProfile(userProfile);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        updatedDietaryEntry.setProduct(product);
        return updatedDietaryEntry;
    }

    @BeforeEach
    void initTest() {
        dietaryEntry = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedDietaryEntry != null) {
            dietaryEntryRepository.delete(insertedDietaryEntry);
            insertedDietaryEntry = null;
        }
    }

    @Test
    @Transactional
    void createDietaryEntry() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DietaryEntry
        DietaryEntryDTO dietaryEntryDTO = dietaryEntryMapper.toDto(dietaryEntry);
        var returnedDietaryEntryDTO = om.readValue(
            restDietaryEntryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dietaryEntryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DietaryEntryDTO.class
        );

        // Validate the DietaryEntry in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDietaryEntry = dietaryEntryMapper.toEntity(returnedDietaryEntryDTO);
        assertDietaryEntryUpdatableFieldsEquals(returnedDietaryEntry, getPersistedDietaryEntry(returnedDietaryEntry));

        insertedDietaryEntry = returnedDietaryEntry;
    }

    @Test
    @Transactional
    void createDietaryEntryWithExistingId() throws Exception {
        // Create the DietaryEntry with an existing ID
        dietaryEntry.setId(1L);
        DietaryEntryDTO dietaryEntryDTO = dietaryEntryMapper.toDto(dietaryEntry);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDietaryEntryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dietaryEntryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DietaryEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkConsumptionDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dietaryEntry.setConsumptionDate(null);

        // Create the DietaryEntry, which fails.
        DietaryEntryDTO dietaryEntryDTO = dietaryEntryMapper.toDto(dietaryEntry);

        restDietaryEntryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dietaryEntryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkConsumptionTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dietaryEntry.setConsumptionTime(null);

        // Create the DietaryEntry, which fails.
        DietaryEntryDTO dietaryEntryDTO = dietaryEntryMapper.toDto(dietaryEntry);

        restDietaryEntryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dietaryEntryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dietaryEntry.setQuantity(null);

        // Create the DietaryEntry, which fails.
        DietaryEntryDTO dietaryEntryDTO = dietaryEntryMapper.toDto(dietaryEntry);

        restDietaryEntryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dietaryEntryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDietaryEntries() throws Exception {
        // Initialize the database
        insertedDietaryEntry = dietaryEntryRepository.saveAndFlush(dietaryEntry);

        // Get all the dietaryEntryList
        restDietaryEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dietaryEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].consumptionDate").value(hasItem(DEFAULT_CONSUMPTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].consumptionTime").value(hasItem(DEFAULT_CONSUMPTION_TIME.format(LOCAL_DATE_TIME_FORMAT))))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDietaryEntriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(dietaryEntryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDietaryEntryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(dietaryEntryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDietaryEntriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(dietaryEntryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDietaryEntryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(dietaryEntryRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDietaryEntry() throws Exception {
        // Initialize the database
        insertedDietaryEntry = dietaryEntryRepository.saveAndFlush(dietaryEntry);

        // Get the dietaryEntry
        restDietaryEntryMockMvc
            .perform(get(ENTITY_API_URL_ID, dietaryEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dietaryEntry.getId().intValue()))
            .andExpect(jsonPath("$.consumptionDate").value(DEFAULT_CONSUMPTION_DATE.toString()))
            .andExpect(jsonPath("$.consumptionTime").value(DEFAULT_CONSUMPTION_TIME.format(LOCAL_DATE_TIME_FORMAT)))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY));
    }

    @Test
    @Transactional
    void getDietaryEntriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedDietaryEntry = dietaryEntryRepository.saveAndFlush(dietaryEntry);

        Long id = dietaryEntry.getId();

        defaultDietaryEntryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDietaryEntryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDietaryEntryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDietaryEntriesByConsumptionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDietaryEntry = dietaryEntryRepository.saveAndFlush(dietaryEntry);

        // Get all the dietaryEntryList where consumptionDate equals to
        defaultDietaryEntryFiltering(
            "consumptionDate.equals=" + DEFAULT_CONSUMPTION_DATE,
            "consumptionDate.equals=" + UPDATED_CONSUMPTION_DATE
        );
    }

    @Test
    @Transactional
    void getAllDietaryEntriesByConsumptionDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDietaryEntry = dietaryEntryRepository.saveAndFlush(dietaryEntry);

        // Get all the dietaryEntryList where consumptionDate in
        defaultDietaryEntryFiltering(
            "consumptionDate.in=" + DEFAULT_CONSUMPTION_DATE + "," + UPDATED_CONSUMPTION_DATE,
            "consumptionDate.in=" + UPDATED_CONSUMPTION_DATE
        );
    }

    @Test
    @Transactional
    void getAllDietaryEntriesByConsumptionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDietaryEntry = dietaryEntryRepository.saveAndFlush(dietaryEntry);

        // Get all the dietaryEntryList where consumptionDate is not null
        defaultDietaryEntryFiltering("consumptionDate.specified=true", "consumptionDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDietaryEntriesByConsumptionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDietaryEntry = dietaryEntryRepository.saveAndFlush(dietaryEntry);

        // Get all the dietaryEntryList where consumptionDate is greater than or equal to
        defaultDietaryEntryFiltering(
            "consumptionDate.greaterThanOrEqual=" + DEFAULT_CONSUMPTION_DATE,
            "consumptionDate.greaterThanOrEqual=" + UPDATED_CONSUMPTION_DATE
        );
    }

    @Test
    @Transactional
    void getAllDietaryEntriesByConsumptionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDietaryEntry = dietaryEntryRepository.saveAndFlush(dietaryEntry);

        // Get all the dietaryEntryList where consumptionDate is less than or equal to
        defaultDietaryEntryFiltering(
            "consumptionDate.lessThanOrEqual=" + DEFAULT_CONSUMPTION_DATE,
            "consumptionDate.lessThanOrEqual=" + SMALLER_CONSUMPTION_DATE
        );
    }

    @Test
    @Transactional
    void getAllDietaryEntriesByConsumptionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDietaryEntry = dietaryEntryRepository.saveAndFlush(dietaryEntry);

        // Get all the dietaryEntryList where consumptionDate is less than
        defaultDietaryEntryFiltering(
            "consumptionDate.lessThan=" + UPDATED_CONSUMPTION_DATE,
            "consumptionDate.lessThan=" + DEFAULT_CONSUMPTION_DATE
        );
    }

    @Test
    @Transactional
    void getAllDietaryEntriesByConsumptionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDietaryEntry = dietaryEntryRepository.saveAndFlush(dietaryEntry);

        // Get all the dietaryEntryList where consumptionDate is greater than
        defaultDietaryEntryFiltering(
            "consumptionDate.greaterThan=" + SMALLER_CONSUMPTION_DATE,
            "consumptionDate.greaterThan=" + DEFAULT_CONSUMPTION_DATE
        );
    }

    @Test
    @Transactional
    void getAllDietaryEntriesByConsumptionTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDietaryEntry = dietaryEntryRepository.saveAndFlush(dietaryEntry);

        // Get all the dietaryEntryList where consumptionTime equals to
        defaultDietaryEntryFiltering(
            "consumptionTime.equals=" + DEFAULT_CONSUMPTION_TIME,
            "consumptionTime.equals=" + UPDATED_CONSUMPTION_TIME
        );
    }

    @Test
    @Transactional
    void getAllDietaryEntriesByConsumptionTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDietaryEntry = dietaryEntryRepository.saveAndFlush(dietaryEntry);

        // Get all the dietaryEntryList where consumptionTime in
        defaultDietaryEntryFiltering(
            "consumptionTime.in=" + DEFAULT_CONSUMPTION_TIME + "," + UPDATED_CONSUMPTION_TIME,
            "consumptionTime.in=" + UPDATED_CONSUMPTION_TIME
        );
    }

    @Test
    @Transactional
    void getAllDietaryEntriesByConsumptionTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDietaryEntry = dietaryEntryRepository.saveAndFlush(dietaryEntry);

        // Get all the dietaryEntryList where consumptionTime is not null
        defaultDietaryEntryFiltering("consumptionTime.specified=true", "consumptionTime.specified=false");
    }

    @Test
    @Transactional
    void getAllDietaryEntriesByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDietaryEntry = dietaryEntryRepository.saveAndFlush(dietaryEntry);

        // Get all the dietaryEntryList where quantity equals to
        defaultDietaryEntryFiltering("quantity.equals=" + DEFAULT_QUANTITY, "quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllDietaryEntriesByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDietaryEntry = dietaryEntryRepository.saveAndFlush(dietaryEntry);

        // Get all the dietaryEntryList where quantity in
        defaultDietaryEntryFiltering("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY, "quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllDietaryEntriesByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDietaryEntry = dietaryEntryRepository.saveAndFlush(dietaryEntry);

        // Get all the dietaryEntryList where quantity is not null
        defaultDietaryEntryFiltering("quantity.specified=true", "quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllDietaryEntriesByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDietaryEntry = dietaryEntryRepository.saveAndFlush(dietaryEntry);

        // Get all the dietaryEntryList where quantity is greater than or equal to
        defaultDietaryEntryFiltering("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY, "quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllDietaryEntriesByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDietaryEntry = dietaryEntryRepository.saveAndFlush(dietaryEntry);

        // Get all the dietaryEntryList where quantity is less than or equal to
        defaultDietaryEntryFiltering("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY, "quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllDietaryEntriesByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDietaryEntry = dietaryEntryRepository.saveAndFlush(dietaryEntry);

        // Get all the dietaryEntryList where quantity is less than
        defaultDietaryEntryFiltering("quantity.lessThan=" + UPDATED_QUANTITY, "quantity.lessThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllDietaryEntriesByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDietaryEntry = dietaryEntryRepository.saveAndFlush(dietaryEntry);

        // Get all the dietaryEntryList where quantity is greater than
        defaultDietaryEntryFiltering("quantity.greaterThan=" + SMALLER_QUANTITY, "quantity.greaterThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllDietaryEntriesByUserProfileIsEqualToSomething() throws Exception {
        UserProfile userProfile;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            dietaryEntryRepository.saveAndFlush(dietaryEntry);
            userProfile = UserProfileResourceIT.createEntity();
        } else {
            userProfile = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        em.persist(userProfile);
        em.flush();
        dietaryEntry.setUserProfile(userProfile);
        dietaryEntryRepository.saveAndFlush(dietaryEntry);
        Long userProfileId = userProfile.getId();
        // Get all the dietaryEntryList where userProfile equals to userProfileId
        defaultDietaryEntryShouldBeFound("userProfileId.equals=" + userProfileId);

        // Get all the dietaryEntryList where userProfile equals to (userProfileId + 1)
        defaultDietaryEntryShouldNotBeFound("userProfileId.equals=" + (userProfileId + 1));
    }

    @Test
    @Transactional
    void getAllDietaryEntriesByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            dietaryEntryRepository.saveAndFlush(dietaryEntry);
            product = ProductResourceIT.createEntity(em);
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(product);
        em.flush();
        dietaryEntry.setProduct(product);
        dietaryEntryRepository.saveAndFlush(dietaryEntry);
        Long productId = product.getId();
        // Get all the dietaryEntryList where product equals to productId
        defaultDietaryEntryShouldBeFound("productId.equals=" + productId);

        // Get all the dietaryEntryList where product equals to (productId + 1)
        defaultDietaryEntryShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    private void defaultDietaryEntryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDietaryEntryShouldBeFound(shouldBeFound);
        defaultDietaryEntryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDietaryEntryShouldBeFound(String filter) throws Exception {
        restDietaryEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dietaryEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].consumptionDate").value(hasItem(DEFAULT_CONSUMPTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].consumptionTime").value(hasItem(DEFAULT_CONSUMPTION_TIME.format(LOCAL_DATE_TIME_FORMAT))))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));

        // Check, that the count call also returns 1
        restDietaryEntryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDietaryEntryShouldNotBeFound(String filter) throws Exception {
        restDietaryEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDietaryEntryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDietaryEntry() throws Exception {
        // Get the dietaryEntry
        restDietaryEntryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDietaryEntry() throws Exception {
        // Initialize the database
        insertedDietaryEntry = dietaryEntryRepository.saveAndFlush(dietaryEntry);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dietaryEntry
        DietaryEntry updatedDietaryEntry = dietaryEntryRepository.findById(dietaryEntry.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDietaryEntry are not directly saved in db
        em.detach(updatedDietaryEntry);
        updatedDietaryEntry.consumptionDate(UPDATED_CONSUMPTION_DATE).consumptionTime(UPDATED_CONSUMPTION_TIME).quantity(UPDATED_QUANTITY);
        DietaryEntryDTO dietaryEntryDTO = dietaryEntryMapper.toDto(updatedDietaryEntry);

        restDietaryEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dietaryEntryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dietaryEntryDTO))
            )
            .andExpect(status().isOk());

        // Validate the DietaryEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDietaryEntryToMatchAllProperties(updatedDietaryEntry);
    }

    @Test
    @Transactional
    void putNonExistingDietaryEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dietaryEntry.setId(longCount.incrementAndGet());

        // Create the DietaryEntry
        DietaryEntryDTO dietaryEntryDTO = dietaryEntryMapper.toDto(dietaryEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDietaryEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dietaryEntryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dietaryEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DietaryEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDietaryEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dietaryEntry.setId(longCount.incrementAndGet());

        // Create the DietaryEntry
        DietaryEntryDTO dietaryEntryDTO = dietaryEntryMapper.toDto(dietaryEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDietaryEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dietaryEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DietaryEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDietaryEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dietaryEntry.setId(longCount.incrementAndGet());

        // Create the DietaryEntry
        DietaryEntryDTO dietaryEntryDTO = dietaryEntryMapper.toDto(dietaryEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDietaryEntryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dietaryEntryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DietaryEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDietaryEntryWithPatch() throws Exception {
        // Initialize the database
        insertedDietaryEntry = dietaryEntryRepository.saveAndFlush(dietaryEntry);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dietaryEntry using partial update
        DietaryEntry partialUpdatedDietaryEntry = new DietaryEntry();
        partialUpdatedDietaryEntry.setId(dietaryEntry.getId());

        partialUpdatedDietaryEntry.quantity(UPDATED_QUANTITY);

        restDietaryEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDietaryEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDietaryEntry))
            )
            .andExpect(status().isOk());

        // Validate the DietaryEntry in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDietaryEntryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDietaryEntry, dietaryEntry),
            getPersistedDietaryEntry(dietaryEntry)
        );
    }

    @Test
    @Transactional
    void fullUpdateDietaryEntryWithPatch() throws Exception {
        // Initialize the database
        insertedDietaryEntry = dietaryEntryRepository.saveAndFlush(dietaryEntry);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dietaryEntry using partial update
        DietaryEntry partialUpdatedDietaryEntry = new DietaryEntry();
        partialUpdatedDietaryEntry.setId(dietaryEntry.getId());

        partialUpdatedDietaryEntry
            .consumptionDate(UPDATED_CONSUMPTION_DATE)
            .consumptionTime(UPDATED_CONSUMPTION_TIME)
            .quantity(UPDATED_QUANTITY);

        restDietaryEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDietaryEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDietaryEntry))
            )
            .andExpect(status().isOk());

        // Validate the DietaryEntry in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDietaryEntryUpdatableFieldsEquals(partialUpdatedDietaryEntry, getPersistedDietaryEntry(partialUpdatedDietaryEntry));
    }

    @Test
    @Transactional
    void patchNonExistingDietaryEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dietaryEntry.setId(longCount.incrementAndGet());

        // Create the DietaryEntry
        DietaryEntryDTO dietaryEntryDTO = dietaryEntryMapper.toDto(dietaryEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDietaryEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dietaryEntryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dietaryEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DietaryEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDietaryEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dietaryEntry.setId(longCount.incrementAndGet());

        // Create the DietaryEntry
        DietaryEntryDTO dietaryEntryDTO = dietaryEntryMapper.toDto(dietaryEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDietaryEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dietaryEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DietaryEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDietaryEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dietaryEntry.setId(longCount.incrementAndGet());

        // Create the DietaryEntry
        DietaryEntryDTO dietaryEntryDTO = dietaryEntryMapper.toDto(dietaryEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDietaryEntryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(dietaryEntryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DietaryEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDietaryEntry() throws Exception {
        // Initialize the database
        insertedDietaryEntry = dietaryEntryRepository.saveAndFlush(dietaryEntry);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the dietaryEntry
        restDietaryEntryMockMvc
            .perform(delete(ENTITY_API_URL_ID, dietaryEntry.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return dietaryEntryRepository.count();
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

    protected DietaryEntry getPersistedDietaryEntry(DietaryEntry dietaryEntry) {
        return dietaryEntryRepository.findById(dietaryEntry.getId()).orElseThrow();
    }

    protected void assertPersistedDietaryEntryToMatchAllProperties(DietaryEntry expectedDietaryEntry) {
        assertDietaryEntryAllPropertiesEquals(expectedDietaryEntry, getPersistedDietaryEntry(expectedDietaryEntry));
    }

    protected void assertPersistedDietaryEntryToMatchUpdatableProperties(DietaryEntry expectedDietaryEntry) {
        assertDietaryEntryAllUpdatablePropertiesEquals(expectedDietaryEntry, getPersistedDietaryEntry(expectedDietaryEntry));
    }
}
