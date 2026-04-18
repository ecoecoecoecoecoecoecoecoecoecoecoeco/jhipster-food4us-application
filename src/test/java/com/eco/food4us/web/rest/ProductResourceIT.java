package com.eco.food4us.web.rest;

import static com.eco.food4us.domain.ProductAsserts.*;
import static com.eco.food4us.web.rest.TestUtil.createUpdateProxyForBean;
import static com.eco.food4us.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.eco.food4us.IntegrationTest;
import com.eco.food4us.domain.Product;
import com.eco.food4us.domain.Supplier;
import com.eco.food4us.domain.Warehouse;
import com.eco.food4us.repository.ProductRepository;
import com.eco.food4us.service.ProductService;
import com.eco.food4us.service.dto.ProductDTO;
import com.eco.food4us.service.mapper.ProductMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link ProductResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_QR_CODE = "AAAAAAAAAA";
    private static final String UPDATED_QR_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_BAR_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BAR_CODE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_EXPIRATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_EXPIRATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_QUANTITY = 0;
    private static final Integer UPDATED_QUANTITY = 1;
    private static final Integer SMALLER_QUANTITY = 0 - 1;

    private static final BigDecimal DEFAULT_PURCHASE_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_PURCHASE_PRICE = new BigDecimal(1);
    private static final BigDecimal SMALLER_PURCHASE_PRICE = new BigDecimal(0 - 1);

    private static final LocalDate DEFAULT_PURCHASE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PURCHASE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PURCHASE_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductRepository productRepository;

    @Mock
    private ProductRepository productRepositoryMock;

    @Autowired
    private ProductMapper productMapper;

    @Mock
    private ProductService productServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductMockMvc;

    private Product product;

    private Product insertedProduct;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createEntity(EntityManager em) {
        Product product = new Product()
            .name(DEFAULT_NAME)
            .qrCode(DEFAULT_QR_CODE)
            .barCode(DEFAULT_BAR_CODE)
            .expirationDate(DEFAULT_EXPIRATION_DATE)
            .quantity(DEFAULT_QUANTITY)
            .purchasePrice(DEFAULT_PURCHASE_PRICE)
            .purchaseDate(DEFAULT_PURCHASE_DATE);
        // Add required entity
        Warehouse warehouse;
        if (TestUtil.findAll(em, Warehouse.class).isEmpty()) {
            warehouse = WarehouseResourceIT.createEntity();
            em.persist(warehouse);
            em.flush();
        } else {
            warehouse = TestUtil.findAll(em, Warehouse.class).get(0);
        }
        product.setWarehouse(warehouse);
        return product;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createUpdatedEntity(EntityManager em) {
        Product updatedProduct = new Product()
            .name(UPDATED_NAME)
            .qrCode(UPDATED_QR_CODE)
            .barCode(UPDATED_BAR_CODE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .quantity(UPDATED_QUANTITY)
            .purchasePrice(UPDATED_PURCHASE_PRICE)
            .purchaseDate(UPDATED_PURCHASE_DATE);
        // Add required entity
        Warehouse warehouse;
        if (TestUtil.findAll(em, Warehouse.class).isEmpty()) {
            warehouse = WarehouseResourceIT.createUpdatedEntity();
            em.persist(warehouse);
            em.flush();
        } else {
            warehouse = TestUtil.findAll(em, Warehouse.class).get(0);
        }
        updatedProduct.setWarehouse(warehouse);
        return updatedProduct;
    }

    @BeforeEach
    void initTest() {
        product = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedProduct != null) {
            productRepository.delete(insertedProduct);
            insertedProduct = null;
        }
    }

    @Test
    @Transactional
    void createProduct() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);
        var returnedProductDTO = om.readValue(
            restProductMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductDTO.class
        );

        // Validate the Product in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProduct = productMapper.toEntity(returnedProductDTO);
        assertProductUpdatableFieldsEquals(returnedProduct, getPersistedProduct(returnedProduct));

        insertedProduct = returnedProduct;
    }

    @Test
    @Transactional
    void createProductWithExistingId() throws Exception {
        // Create the Product with an existing ID
        product.setId(1L);
        ProductDTO productDTO = productMapper.toDto(product);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        product.setName(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        product.setQuantity(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProducts() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].qrCode").value(hasItem(DEFAULT_QR_CODE)))
            .andExpect(jsonPath("$.[*].barCode").value(hasItem(DEFAULT_BAR_CODE)))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].purchasePrice").value(hasItem(sameNumber(DEFAULT_PURCHASE_PRICE))))
            .andExpect(jsonPath("$.[*].purchaseDate").value(hasItem(DEFAULT_PURCHASE_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductsWithEagerRelationshipsIsEnabled() throws Exception {
        when(productServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(productRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProduct() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get the product
        restProductMockMvc
            .perform(get(ENTITY_API_URL_ID, product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(product.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.qrCode").value(DEFAULT_QR_CODE))
            .andExpect(jsonPath("$.barCode").value(DEFAULT_BAR_CODE))
            .andExpect(jsonPath("$.expirationDate").value(DEFAULT_EXPIRATION_DATE.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.purchasePrice").value(sameNumber(DEFAULT_PURCHASE_PRICE)))
            .andExpect(jsonPath("$.purchaseDate").value(DEFAULT_PURCHASE_DATE.toString()));
    }

    @Test
    @Transactional
    void getProductsByIdFiltering() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        Long id = product.getId();

        defaultProductFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where name equals to
        defaultProductFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where name in
        defaultProductFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where name is not null
        defaultProductFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where name contains
        defaultProductFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where name does not contain
        defaultProductFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByQrCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where qrCode equals to
        defaultProductFiltering("qrCode.equals=" + DEFAULT_QR_CODE, "qrCode.equals=" + UPDATED_QR_CODE);
    }

    @Test
    @Transactional
    void getAllProductsByQrCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where qrCode in
        defaultProductFiltering("qrCode.in=" + DEFAULT_QR_CODE + "," + UPDATED_QR_CODE, "qrCode.in=" + UPDATED_QR_CODE);
    }

    @Test
    @Transactional
    void getAllProductsByQrCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where qrCode is not null
        defaultProductFiltering("qrCode.specified=true", "qrCode.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByQrCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where qrCode contains
        defaultProductFiltering("qrCode.contains=" + DEFAULT_QR_CODE, "qrCode.contains=" + UPDATED_QR_CODE);
    }

    @Test
    @Transactional
    void getAllProductsByQrCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where qrCode does not contain
        defaultProductFiltering("qrCode.doesNotContain=" + UPDATED_QR_CODE, "qrCode.doesNotContain=" + DEFAULT_QR_CODE);
    }

    @Test
    @Transactional
    void getAllProductsByBarCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where barCode equals to
        defaultProductFiltering("barCode.equals=" + DEFAULT_BAR_CODE, "barCode.equals=" + UPDATED_BAR_CODE);
    }

    @Test
    @Transactional
    void getAllProductsByBarCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where barCode in
        defaultProductFiltering("barCode.in=" + DEFAULT_BAR_CODE + "," + UPDATED_BAR_CODE, "barCode.in=" + UPDATED_BAR_CODE);
    }

    @Test
    @Transactional
    void getAllProductsByBarCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where barCode is not null
        defaultProductFiltering("barCode.specified=true", "barCode.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByBarCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where barCode contains
        defaultProductFiltering("barCode.contains=" + DEFAULT_BAR_CODE, "barCode.contains=" + UPDATED_BAR_CODE);
    }

    @Test
    @Transactional
    void getAllProductsByBarCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where barCode does not contain
        defaultProductFiltering("barCode.doesNotContain=" + UPDATED_BAR_CODE, "barCode.doesNotContain=" + DEFAULT_BAR_CODE);
    }

    @Test
    @Transactional
    void getAllProductsByExpirationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where expirationDate equals to
        defaultProductFiltering("expirationDate.equals=" + DEFAULT_EXPIRATION_DATE, "expirationDate.equals=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllProductsByExpirationDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where expirationDate in
        defaultProductFiltering(
            "expirationDate.in=" + DEFAULT_EXPIRATION_DATE + "," + UPDATED_EXPIRATION_DATE,
            "expirationDate.in=" + UPDATED_EXPIRATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllProductsByExpirationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where expirationDate is not null
        defaultProductFiltering("expirationDate.specified=true", "expirationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByExpirationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where expirationDate is greater than or equal to
        defaultProductFiltering(
            "expirationDate.greaterThanOrEqual=" + DEFAULT_EXPIRATION_DATE,
            "expirationDate.greaterThanOrEqual=" + UPDATED_EXPIRATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllProductsByExpirationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where expirationDate is less than or equal to
        defaultProductFiltering(
            "expirationDate.lessThanOrEqual=" + DEFAULT_EXPIRATION_DATE,
            "expirationDate.lessThanOrEqual=" + SMALLER_EXPIRATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllProductsByExpirationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where expirationDate is less than
        defaultProductFiltering("expirationDate.lessThan=" + UPDATED_EXPIRATION_DATE, "expirationDate.lessThan=" + DEFAULT_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllProductsByExpirationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where expirationDate is greater than
        defaultProductFiltering(
            "expirationDate.greaterThan=" + SMALLER_EXPIRATION_DATE,
            "expirationDate.greaterThan=" + DEFAULT_EXPIRATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllProductsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where quantity equals to
        defaultProductFiltering("quantity.equals=" + DEFAULT_QUANTITY, "quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where quantity in
        defaultProductFiltering("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY, "quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where quantity is not null
        defaultProductFiltering("quantity.specified=true", "quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where quantity is greater than or equal to
        defaultProductFiltering("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY, "quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where quantity is less than or equal to
        defaultProductFiltering("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY, "quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where quantity is less than
        defaultProductFiltering("quantity.lessThan=" + UPDATED_QUANTITY, "quantity.lessThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where quantity is greater than
        defaultProductFiltering("quantity.greaterThan=" + SMALLER_QUANTITY, "quantity.greaterThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductsByPurchasePriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where purchasePrice equals to
        defaultProductFiltering("purchasePrice.equals=" + DEFAULT_PURCHASE_PRICE, "purchasePrice.equals=" + UPDATED_PURCHASE_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPurchasePriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where purchasePrice in
        defaultProductFiltering(
            "purchasePrice.in=" + DEFAULT_PURCHASE_PRICE + "," + UPDATED_PURCHASE_PRICE,
            "purchasePrice.in=" + UPDATED_PURCHASE_PRICE
        );
    }

    @Test
    @Transactional
    void getAllProductsByPurchasePriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where purchasePrice is not null
        defaultProductFiltering("purchasePrice.specified=true", "purchasePrice.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByPurchasePriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where purchasePrice is greater than or equal to
        defaultProductFiltering(
            "purchasePrice.greaterThanOrEqual=" + DEFAULT_PURCHASE_PRICE,
            "purchasePrice.greaterThanOrEqual=" + UPDATED_PURCHASE_PRICE
        );
    }

    @Test
    @Transactional
    void getAllProductsByPurchasePriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where purchasePrice is less than or equal to
        defaultProductFiltering(
            "purchasePrice.lessThanOrEqual=" + DEFAULT_PURCHASE_PRICE,
            "purchasePrice.lessThanOrEqual=" + SMALLER_PURCHASE_PRICE
        );
    }

    @Test
    @Transactional
    void getAllProductsByPurchasePriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where purchasePrice is less than
        defaultProductFiltering("purchasePrice.lessThan=" + UPDATED_PURCHASE_PRICE, "purchasePrice.lessThan=" + DEFAULT_PURCHASE_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPurchasePriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where purchasePrice is greater than
        defaultProductFiltering(
            "purchasePrice.greaterThan=" + SMALLER_PURCHASE_PRICE,
            "purchasePrice.greaterThan=" + DEFAULT_PURCHASE_PRICE
        );
    }

    @Test
    @Transactional
    void getAllProductsByPurchaseDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where purchaseDate equals to
        defaultProductFiltering("purchaseDate.equals=" + DEFAULT_PURCHASE_DATE, "purchaseDate.equals=" + UPDATED_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void getAllProductsByPurchaseDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where purchaseDate in
        defaultProductFiltering(
            "purchaseDate.in=" + DEFAULT_PURCHASE_DATE + "," + UPDATED_PURCHASE_DATE,
            "purchaseDate.in=" + UPDATED_PURCHASE_DATE
        );
    }

    @Test
    @Transactional
    void getAllProductsByPurchaseDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where purchaseDate is not null
        defaultProductFiltering("purchaseDate.specified=true", "purchaseDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByPurchaseDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where purchaseDate is greater than or equal to
        defaultProductFiltering(
            "purchaseDate.greaterThanOrEqual=" + DEFAULT_PURCHASE_DATE,
            "purchaseDate.greaterThanOrEqual=" + UPDATED_PURCHASE_DATE
        );
    }

    @Test
    @Transactional
    void getAllProductsByPurchaseDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where purchaseDate is less than or equal to
        defaultProductFiltering(
            "purchaseDate.lessThanOrEqual=" + DEFAULT_PURCHASE_DATE,
            "purchaseDate.lessThanOrEqual=" + SMALLER_PURCHASE_DATE
        );
    }

    @Test
    @Transactional
    void getAllProductsByPurchaseDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where purchaseDate is less than
        defaultProductFiltering("purchaseDate.lessThan=" + UPDATED_PURCHASE_DATE, "purchaseDate.lessThan=" + DEFAULT_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void getAllProductsByPurchaseDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        // Get all the productList where purchaseDate is greater than
        defaultProductFiltering("purchaseDate.greaterThan=" + SMALLER_PURCHASE_DATE, "purchaseDate.greaterThan=" + DEFAULT_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void getAllProductsByWarehouseIsEqualToSomething() throws Exception {
        Warehouse warehouse;
        if (TestUtil.findAll(em, Warehouse.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            warehouse = WarehouseResourceIT.createEntity();
        } else {
            warehouse = TestUtil.findAll(em, Warehouse.class).get(0);
        }
        em.persist(warehouse);
        em.flush();
        product.setWarehouse(warehouse);
        productRepository.saveAndFlush(product);
        Long warehouseId = warehouse.getId();
        // Get all the productList where warehouse equals to warehouseId
        defaultProductShouldBeFound("warehouseId.equals=" + warehouseId);

        // Get all the productList where warehouse equals to (warehouseId + 1)
        defaultProductShouldNotBeFound("warehouseId.equals=" + (warehouseId + 1));
    }

    @Test
    @Transactional
    void getAllProductsBySupplierIsEqualToSomething() throws Exception {
        Supplier supplier;
        if (TestUtil.findAll(em, Supplier.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            supplier = SupplierResourceIT.createEntity();
        } else {
            supplier = TestUtil.findAll(em, Supplier.class).get(0);
        }
        em.persist(supplier);
        em.flush();
        product.setSupplier(supplier);
        productRepository.saveAndFlush(product);
        Long supplierId = supplier.getId();
        // Get all the productList where supplier equals to supplierId
        defaultProductShouldBeFound("supplierId.equals=" + supplierId);

        // Get all the productList where supplier equals to (supplierId + 1)
        defaultProductShouldNotBeFound("supplierId.equals=" + (supplierId + 1));
    }

    private void defaultProductFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductShouldBeFound(shouldBeFound);
        defaultProductShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductShouldBeFound(String filter) throws Exception {
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].qrCode").value(hasItem(DEFAULT_QR_CODE)))
            .andExpect(jsonPath("$.[*].barCode").value(hasItem(DEFAULT_BAR_CODE)))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].purchasePrice").value(hasItem(sameNumber(DEFAULT_PURCHASE_PRICE))))
            .andExpect(jsonPath("$.[*].purchaseDate").value(hasItem(DEFAULT_PURCHASE_DATE.toString())));

        // Check, that the count call also returns 1
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductShouldNotBeFound(String filter) throws Exception {
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProduct() throws Exception {
        // Get the product
        restProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProduct() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the product
        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProduct are not directly saved in db
        em.detach(updatedProduct);
        updatedProduct
            .name(UPDATED_NAME)
            .qrCode(UPDATED_QR_CODE)
            .barCode(UPDATED_BAR_CODE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .quantity(UPDATED_QUANTITY)
            .purchasePrice(UPDATED_PURCHASE_PRICE)
            .purchaseDate(UPDATED_PURCHASE_DATE);
        ProductDTO productDTO = productMapper.toDto(updatedProduct);

        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductToMatchAllProperties(updatedProduct);
    }

    @Test
    @Transactional
    void putNonExistingProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductWithPatch() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct
            .name(UPDATED_NAME)
            .qrCode(UPDATED_QR_CODE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .quantity(UPDATED_QUANTITY)
            .purchasePrice(UPDATED_PURCHASE_PRICE)
            .purchaseDate(UPDATED_PURCHASE_DATE);

        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedProduct, product), getPersistedProduct(product));
    }

    @Test
    @Transactional
    void fullUpdateProductWithPatch() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct
            .name(UPDATED_NAME)
            .qrCode(UPDATED_QR_CODE)
            .barCode(UPDATED_BAR_CODE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .quantity(UPDATED_QUANTITY)
            .purchasePrice(UPDATED_PURCHASE_PRICE)
            .purchaseDate(UPDATED_PURCHASE_DATE);

        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductUpdatableFieldsEquals(partialUpdatedProduct, getPersistedProduct(partialUpdatedProduct));
    }

    @Test
    @Transactional
    void patchNonExistingProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        product.setId(longCount.incrementAndGet());

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Product in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProduct() throws Exception {
        // Initialize the database
        insertedProduct = productRepository.saveAndFlush(product);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the product
        restProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, product.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productRepository.count();
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

    protected Product getPersistedProduct(Product product) {
        return productRepository.findById(product.getId()).orElseThrow();
    }

    protected void assertPersistedProductToMatchAllProperties(Product expectedProduct) {
        assertProductAllPropertiesEquals(expectedProduct, getPersistedProduct(expectedProduct));
    }

    protected void assertPersistedProductToMatchUpdatableProperties(Product expectedProduct) {
        assertProductAllUpdatablePropertiesEquals(expectedProduct, getPersistedProduct(expectedProduct));
    }
}
