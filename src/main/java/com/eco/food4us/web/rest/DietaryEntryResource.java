package com.eco.food4us.web.rest;

import com.eco.food4us.repository.DietaryEntryRepository;
import com.eco.food4us.service.DietaryEntryQueryService;
import com.eco.food4us.service.DietaryEntryService;
import com.eco.food4us.service.criteria.DietaryEntryCriteria;
import com.eco.food4us.service.dto.DietaryEntryDTO;
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
 * REST controller for managing {@link com.eco.food4us.domain.DietaryEntry}.
 */
@RestController
@RequestMapping("/api/dietary-entries")
public class DietaryEntryResource {

    private static final Logger LOG = LoggerFactory.getLogger(DietaryEntryResource.class);

    private static final String ENTITY_NAME = "dietaryEntry";

    @Value("${jhipster.clientApp.name:food4us}")
    private String applicationName;

    private final DietaryEntryService dietaryEntryService;

    private final DietaryEntryRepository dietaryEntryRepository;

    private final DietaryEntryQueryService dietaryEntryQueryService;

    public DietaryEntryResource(
        DietaryEntryService dietaryEntryService,
        DietaryEntryRepository dietaryEntryRepository,
        DietaryEntryQueryService dietaryEntryQueryService
    ) {
        this.dietaryEntryService = dietaryEntryService;
        this.dietaryEntryRepository = dietaryEntryRepository;
        this.dietaryEntryQueryService = dietaryEntryQueryService;
    }

    /**
     * {@code POST  /dietary-entries} : Create a new dietaryEntry.
     *
     * @param dietaryEntryDTO the dietaryEntryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dietaryEntryDTO, or with status {@code 400 (Bad Request)} if the dietaryEntry has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DietaryEntryDTO> createDietaryEntry(@Valid @RequestBody DietaryEntryDTO dietaryEntryDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save DietaryEntry : {}", dietaryEntryDTO);
        if (dietaryEntryDTO.getId() != null) {
            throw new BadRequestAlertException("A new dietaryEntry cannot already have an ID", ENTITY_NAME, "idexists");
        }
        dietaryEntryDTO = dietaryEntryService.save(dietaryEntryDTO);
        return ResponseEntity.created(new URI("/api/dietary-entries/" + dietaryEntryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, dietaryEntryDTO.getId().toString()))
            .body(dietaryEntryDTO);
    }

    /**
     * {@code PUT  /dietary-entries/:id} : Updates an existing dietaryEntry.
     *
     * @param id the id of the dietaryEntryDTO to save.
     * @param dietaryEntryDTO the dietaryEntryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dietaryEntryDTO,
     * or with status {@code 400 (Bad Request)} if the dietaryEntryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dietaryEntryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DietaryEntryDTO> updateDietaryEntry(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DietaryEntryDTO dietaryEntryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DietaryEntry : {}, {}", id, dietaryEntryDTO);
        if (dietaryEntryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dietaryEntryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dietaryEntryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        dietaryEntryDTO = dietaryEntryService.update(dietaryEntryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dietaryEntryDTO.getId().toString()))
            .body(dietaryEntryDTO);
    }

    /**
     * {@code PATCH  /dietary-entries/:id} : Partial updates given fields of an existing dietaryEntry, field will ignore if it is null
     *
     * @param id the id of the dietaryEntryDTO to save.
     * @param dietaryEntryDTO the dietaryEntryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dietaryEntryDTO,
     * or with status {@code 400 (Bad Request)} if the dietaryEntryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the dietaryEntryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the dietaryEntryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DietaryEntryDTO> partialUpdateDietaryEntry(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DietaryEntryDTO dietaryEntryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DietaryEntry partially : {}, {}", id, dietaryEntryDTO);
        if (dietaryEntryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dietaryEntryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dietaryEntryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DietaryEntryDTO> result = dietaryEntryService.partialUpdate(dietaryEntryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dietaryEntryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /dietary-entries} : get all the Dietary Entries.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Dietary Entries in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DietaryEntryDTO>> getAllDietaryEntries(
        DietaryEntryCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get DietaryEntries by criteria: {}", criteria);

        Page<DietaryEntryDTO> page = dietaryEntryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /dietary-entries/count} : count all the dietaryEntries.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDietaryEntries(DietaryEntryCriteria criteria) {
        LOG.debug("REST request to count DietaryEntries by criteria: {}", criteria);
        return ResponseEntity.ok().body(dietaryEntryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /dietary-entries/:id} : get the "id" dietaryEntry.
     *
     * @param id the id of the dietaryEntryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dietaryEntryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DietaryEntryDTO> getDietaryEntry(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DietaryEntry : {}", id);
        Optional<DietaryEntryDTO> dietaryEntryDTO = dietaryEntryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dietaryEntryDTO);
    }

    /**
     * {@code DELETE  /dietary-entries/:id} : delete the "id" dietaryEntry.
     *
     * @param id the id of the dietaryEntryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDietaryEntry(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DietaryEntry : {}", id);
        dietaryEntryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
