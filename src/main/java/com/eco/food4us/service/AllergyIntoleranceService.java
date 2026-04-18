package com.eco.food4us.service;

import com.eco.food4us.domain.AllergyIntolerance;
import com.eco.food4us.repository.AllergyIntoleranceRepository;
import com.eco.food4us.service.dto.AllergyIntoleranceDTO;
import com.eco.food4us.service.mapper.AllergyIntoleranceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.eco.food4us.domain.AllergyIntolerance}.
 */
@Service
@Transactional
public class AllergyIntoleranceService {

    private static final Logger LOG = LoggerFactory.getLogger(AllergyIntoleranceService.class);

    private final AllergyIntoleranceRepository allergyIntoleranceRepository;

    private final AllergyIntoleranceMapper allergyIntoleranceMapper;

    public AllergyIntoleranceService(
        AllergyIntoleranceRepository allergyIntoleranceRepository,
        AllergyIntoleranceMapper allergyIntoleranceMapper
    ) {
        this.allergyIntoleranceRepository = allergyIntoleranceRepository;
        this.allergyIntoleranceMapper = allergyIntoleranceMapper;
    }

    /**
     * Save a allergyIntolerance.
     *
     * @param allergyIntoleranceDTO the entity to save.
     * @return the persisted entity.
     */
    public AllergyIntoleranceDTO save(AllergyIntoleranceDTO allergyIntoleranceDTO) {
        LOG.debug("Request to save AllergyIntolerance : {}", allergyIntoleranceDTO);
        AllergyIntolerance allergyIntolerance = allergyIntoleranceMapper.toEntity(allergyIntoleranceDTO);
        allergyIntolerance = allergyIntoleranceRepository.save(allergyIntolerance);
        return allergyIntoleranceMapper.toDto(allergyIntolerance);
    }

    /**
     * Update a allergyIntolerance.
     *
     * @param allergyIntoleranceDTO the entity to save.
     * @return the persisted entity.
     */
    public AllergyIntoleranceDTO update(AllergyIntoleranceDTO allergyIntoleranceDTO) {
        LOG.debug("Request to update AllergyIntolerance : {}", allergyIntoleranceDTO);
        AllergyIntolerance allergyIntolerance = allergyIntoleranceMapper.toEntity(allergyIntoleranceDTO);
        allergyIntolerance = allergyIntoleranceRepository.save(allergyIntolerance);
        return allergyIntoleranceMapper.toDto(allergyIntolerance);
    }

    /**
     * Partially update a allergyIntolerance.
     *
     * @param allergyIntoleranceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AllergyIntoleranceDTO> partialUpdate(AllergyIntoleranceDTO allergyIntoleranceDTO) {
        LOG.debug("Request to partially update AllergyIntolerance : {}", allergyIntoleranceDTO);

        return allergyIntoleranceRepository
            .findById(allergyIntoleranceDTO.getId())
            .map(existingAllergyIntolerance -> {
                allergyIntoleranceMapper.partialUpdate(existingAllergyIntolerance, allergyIntoleranceDTO);

                return existingAllergyIntolerance;
            })
            .map(allergyIntoleranceRepository::save)
            .map(allergyIntoleranceMapper::toDto);
    }

    /**
     * Get all the allergyIntolerances.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AllergyIntoleranceDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all AllergyIntolerances");
        return allergyIntoleranceRepository.findAll(pageable).map(allergyIntoleranceMapper::toDto);
    }

    /**
     * Get one allergyIntolerance by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AllergyIntoleranceDTO> findOne(Long id) {
        LOG.debug("Request to get AllergyIntolerance : {}", id);
        return allergyIntoleranceRepository.findById(id).map(allergyIntoleranceMapper::toDto);
    }

    /**
     * Delete the allergyIntolerance by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AllergyIntolerance : {}", id);
        allergyIntoleranceRepository.deleteById(id);
    }
}
