package com.eco.food4us.web.rest;

import com.eco.food4us.repository.IngredientRepository;
import com.eco.food4us.service.IngredientService;
import com.eco.food4us.service.dto.IngredientDTO;
import com.eco.food4us.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.eco.food4us.domain.Ingredient}.
 */
@RestController
@RequestMapping("/api/ingredients")
public class IngredientResource {

    private static final Logger LOG = LoggerFactory.getLogger(IngredientResource.class);

    private static final String ENTITY_NAME = "ingredient";

    @Value("${jhipster.clientApp.name:food4us}")
    private String applicationName;

    private final IngredientService ingredientService;

    private final IngredientRepository ingredientRepository;

    public IngredientResource(IngredientService ingredientService, IngredientRepository ingredientRepository) {
        this.ingredientService = ingredientService;
        this.ingredientRepository = ingredientRepository;
    }

    /**
     * {@code POST  /ingredients} : Create a new ingredient.
     *
     * @param ingredientDTO the ingredientDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ingredientDTO, or with status {@code 400 (Bad Request)} if the ingredient has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<IngredientDTO> createIngredient(@Valid @RequestBody IngredientDTO ingredientDTO) throws URISyntaxException {
        LOG.debug("REST request to save Ingredient : {}", ingredientDTO);
        if (ingredientDTO.getId() != null) {
            throw new BadRequestAlertException("A new ingredient cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ingredientDTO = ingredientService.save(ingredientDTO);
        return ResponseEntity.created(new URI("/api/ingredients/" + ingredientDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ingredientDTO.getId().toString()))
            .body(ingredientDTO);
    }

    /**
     * {@code PUT  /ingredients/:id} : Updates an existing ingredient.
     *
     * @param id the id of the ingredientDTO to save.
     * @param ingredientDTO the ingredientDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ingredientDTO,
     * or with status {@code 400 (Bad Request)} if the ingredientDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ingredientDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<IngredientDTO> updateIngredient(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IngredientDTO ingredientDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Ingredient : {}, {}", id, ingredientDTO);
        if (ingredientDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ingredientDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ingredientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ingredientDTO = ingredientService.update(ingredientDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ingredientDTO.getId().toString()))
            .body(ingredientDTO);
    }

    /**
     * {@code PATCH  /ingredients/:id} : Partial updates given fields of an existing ingredient, field will ignore if it is null
     *
     * @param id the id of the ingredientDTO to save.
     * @param ingredientDTO the ingredientDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ingredientDTO,
     * or with status {@code 400 (Bad Request)} if the ingredientDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ingredientDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ingredientDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IngredientDTO> partialUpdateIngredient(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IngredientDTO ingredientDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Ingredient partially : {}, {}", id, ingredientDTO);
        if (ingredientDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ingredientDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ingredientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IngredientDTO> result = ingredientService.partialUpdate(ingredientDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ingredientDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ingredients} : get all the Ingredients.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Ingredients in body.
     */
    @GetMapping("")
    public List<IngredientDTO> getAllIngredients(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all Ingredients");
        return ingredientService.findAll();
    }

    /**
     * {@code GET  /ingredients/:id} : get the "id" ingredient.
     *
     * @param id the id of the ingredientDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ingredientDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<IngredientDTO> getIngredient(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Ingredient : {}", id);
        Optional<IngredientDTO> ingredientDTO = ingredientService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ingredientDTO);
    }

    /**
     * {@code DELETE  /ingredients/:id} : delete the "id" ingredient.
     *
     * @param id the id of the ingredientDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Ingredient : {}", id);
        ingredientService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
