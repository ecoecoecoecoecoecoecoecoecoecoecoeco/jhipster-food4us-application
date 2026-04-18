package com.eco.food4us.web.rest;

import com.eco.food4us.repository.AllergyIntoleranceRepository;
import com.eco.food4us.service.AllergyIntoleranceService;
import com.eco.food4us.service.dto.AllergyIntoleranceDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.eco.food4us.domain.AllergyIntolerance}.
 */
@RestController
@RequestMapping("/api/allergy-intolerances")
public class AllergyIntoleranceResource {

    private static final Logger LOG = LoggerFactory.getLogger(AllergyIntoleranceResource.class);

    private static final String ENTITY_NAME = "allergyIntolerance";

    @Value("${jhipster.clientApp.name:food4us}")
    private String applicationName;

    private final AllergyIntoleranceService allergyIntoleranceService;

    private final AllergyIntoleranceRepository allergyIntoleranceRepository;

    public AllergyIntoleranceResource(
        AllergyIntoleranceService allergyIntoleranceService,
        AllergyIntoleranceRepository allergyIntoleranceRepository
    ) {
        this.allergyIntoleranceService = allergyIntoleranceService;
        this.allergyIntoleranceRepository = allergyIntoleranceRepository;
    }

    /**
     * {@code POST  /allergy-intolerances} : Create a new allergyIntolerance.
     *
     * @param allergyIntoleranceDTO the allergyIntoleranceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new allergyIntoleranceDTO, or with status {@code 400 (Bad Request)} if the allergyIntolerance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AllergyIntoleranceDTO> createAllergyIntolerance(@Valid @RequestBody AllergyIntoleranceDTO allergyIntoleranceDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AllergyIntolerance : {}", allergyIntoleranceDTO);
        if (allergyIntoleranceDTO.getId() != null) {
            throw new BadRequestAlertException("A new allergyIntolerance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        allergyIntoleranceDTO = allergyIntoleranceService.save(allergyIntoleranceDTO);
        return ResponseEntity.created(new URI("/api/allergy-intolerances/" + allergyIntoleranceDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, allergyIntoleranceDTO.getId().toString()))
            .body(allergyIntoleranceDTO);
    }

    /**
     * {@code PUT  /allergy-intolerances/:id} : Updates an existing allergyIntolerance.
     *
     * @param id the id of the allergyIntoleranceDTO to save.
     * @param allergyIntoleranceDTO the allergyIntoleranceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated allergyIntoleranceDTO,
     * or with status {@code 400 (Bad Request)} if the allergyIntoleranceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the allergyIntoleranceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AllergyIntoleranceDTO> updateAllergyIntolerance(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AllergyIntoleranceDTO allergyIntoleranceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AllergyIntolerance : {}, {}", id, allergyIntoleranceDTO);
        if (allergyIntoleranceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, allergyIntoleranceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!allergyIntoleranceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        allergyIntoleranceDTO = allergyIntoleranceService.update(allergyIntoleranceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, allergyIntoleranceDTO.getId().toString()))
            .body(allergyIntoleranceDTO);
    }

    /**
     * {@code PATCH  /allergy-intolerances/:id} : Partial updates given fields of an existing allergyIntolerance, field will ignore if it is null
     *
     * @param id the id of the allergyIntoleranceDTO to save.
     * @param allergyIntoleranceDTO the allergyIntoleranceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated allergyIntoleranceDTO,
     * or with status {@code 400 (Bad Request)} if the allergyIntoleranceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the allergyIntoleranceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the allergyIntoleranceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AllergyIntoleranceDTO> partialUpdateAllergyIntolerance(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AllergyIntoleranceDTO allergyIntoleranceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AllergyIntolerance partially : {}, {}", id, allergyIntoleranceDTO);
        if (allergyIntoleranceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, allergyIntoleranceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!allergyIntoleranceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AllergyIntoleranceDTO> result = allergyIntoleranceService.partialUpdate(allergyIntoleranceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, allergyIntoleranceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /allergy-intolerances} : get all the Allergy Intolerances.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Allergy Intolerances in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AllergyIntoleranceDTO>> getAllAllergyIntolerances(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of AllergyIntolerances");
        Page<AllergyIntoleranceDTO> page = allergyIntoleranceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /allergy-intolerances/:id} : get the "id" allergyIntolerance.
     *
     * @param id the id of the allergyIntoleranceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the allergyIntoleranceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AllergyIntoleranceDTO> getAllergyIntolerance(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AllergyIntolerance : {}", id);
        Optional<AllergyIntoleranceDTO> allergyIntoleranceDTO = allergyIntoleranceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(allergyIntoleranceDTO);
    }

    /**
     * {@code DELETE  /allergy-intolerances/:id} : delete the "id" allergyIntolerance.
     *
     * @param id the id of the allergyIntoleranceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAllergyIntolerance(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AllergyIntolerance : {}", id);
        allergyIntoleranceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
