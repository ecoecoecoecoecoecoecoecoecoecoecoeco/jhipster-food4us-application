package com.eco.food4us.service;

import com.eco.food4us.domain.DietaryEntry;
import com.eco.food4us.repository.DietaryEntryRepository;
import com.eco.food4us.service.dto.DietaryEntryDTO;
import com.eco.food4us.service.mapper.DietaryEntryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.eco.food4us.domain.DietaryEntry}.
 */
@Service
@Transactional
public class DietaryEntryService {

    private static final Logger LOG = LoggerFactory.getLogger(DietaryEntryService.class);

    private final DietaryEntryRepository dietaryEntryRepository;

    private final DietaryEntryMapper dietaryEntryMapper;

    public DietaryEntryService(DietaryEntryRepository dietaryEntryRepository, DietaryEntryMapper dietaryEntryMapper) {
        this.dietaryEntryRepository = dietaryEntryRepository;
        this.dietaryEntryMapper = dietaryEntryMapper;
    }

    /**
     * Save a dietaryEntry.
     *
     * @param dietaryEntryDTO the entity to save.
     * @return the persisted entity.
     */
    public DietaryEntryDTO save(DietaryEntryDTO dietaryEntryDTO) {
        LOG.debug("Request to save DietaryEntry : {}", dietaryEntryDTO);
        DietaryEntry dietaryEntry = dietaryEntryMapper.toEntity(dietaryEntryDTO);
        dietaryEntry = dietaryEntryRepository.save(dietaryEntry);
        return dietaryEntryMapper.toDto(dietaryEntry);
    }

    /**
     * Update a dietaryEntry.
     *
     * @param dietaryEntryDTO the entity to save.
     * @return the persisted entity.
     */
    public DietaryEntryDTO update(DietaryEntryDTO dietaryEntryDTO) {
        LOG.debug("Request to update DietaryEntry : {}", dietaryEntryDTO);
        DietaryEntry dietaryEntry = dietaryEntryMapper.toEntity(dietaryEntryDTO);
        dietaryEntry = dietaryEntryRepository.save(dietaryEntry);
        return dietaryEntryMapper.toDto(dietaryEntry);
    }

    /**
     * Partially update a dietaryEntry.
     *
     * @param dietaryEntryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DietaryEntryDTO> partialUpdate(DietaryEntryDTO dietaryEntryDTO) {
        LOG.debug("Request to partially update DietaryEntry : {}", dietaryEntryDTO);

        return dietaryEntryRepository
            .findById(dietaryEntryDTO.getId())
            .map(existingDietaryEntry -> {
                dietaryEntryMapper.partialUpdate(existingDietaryEntry, dietaryEntryDTO);

                return existingDietaryEntry;
            })
            .map(dietaryEntryRepository::save)
            .map(dietaryEntryMapper::toDto);
    }

    /**
     * Get all the dietaryEntries with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<DietaryEntryDTO> findAllWithEagerRelationships(Pageable pageable) {
        return dietaryEntryRepository.findAllWithEagerRelationships(pageable).map(dietaryEntryMapper::toDto);
    }

    /**
     * Get one dietaryEntry by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DietaryEntryDTO> findOne(Long id) {
        LOG.debug("Request to get DietaryEntry : {}", id);
        return dietaryEntryRepository.findOneWithEagerRelationships(id).map(dietaryEntryMapper::toDto);
    }

    /**
     * Delete the dietaryEntry by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete DietaryEntry : {}", id);
        dietaryEntryRepository.deleteById(id);
    }
}
