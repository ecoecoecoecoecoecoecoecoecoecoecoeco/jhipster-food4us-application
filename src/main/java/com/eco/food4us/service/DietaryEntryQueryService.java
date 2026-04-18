package com.eco.food4us.service;

import com.eco.food4us.domain.*; // for static metamodels
import com.eco.food4us.domain.DietaryEntry;
import com.eco.food4us.repository.DietaryEntryRepository;
import com.eco.food4us.service.criteria.DietaryEntryCriteria;
import com.eco.food4us.service.dto.DietaryEntryDTO;
import com.eco.food4us.service.mapper.DietaryEntryMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link DietaryEntry} entities in the database.
 * The main input is a {@link DietaryEntryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DietaryEntryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DietaryEntryQueryService extends QueryService<DietaryEntry> {

    private static final Logger LOG = LoggerFactory.getLogger(DietaryEntryQueryService.class);

    private final DietaryEntryRepository dietaryEntryRepository;

    private final DietaryEntryMapper dietaryEntryMapper;

    public DietaryEntryQueryService(DietaryEntryRepository dietaryEntryRepository, DietaryEntryMapper dietaryEntryMapper) {
        this.dietaryEntryRepository = dietaryEntryRepository;
        this.dietaryEntryMapper = dietaryEntryMapper;
    }

    /**
     * Return a {@link Page} of {@link DietaryEntryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DietaryEntryDTO> findByCriteria(DietaryEntryCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DietaryEntry> specification = createSpecification(criteria);
        return dietaryEntryRepository.findAll(specification, page).map(dietaryEntryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DietaryEntryCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<DietaryEntry> specification = createSpecification(criteria);
        return dietaryEntryRepository.count(specification);
    }

    /**
     * Function to convert {@link DietaryEntryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DietaryEntry> createSpecification(DietaryEntryCriteria criteria) {
        Specification<DietaryEntry> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                buildRangeSpecification(criteria.getId(), DietaryEntry_.id),
                buildRangeSpecification(criteria.getConsumptionDate(), DietaryEntry_.consumptionDate),
                buildRangeSpecification(criteria.getConsumptionTime(), DietaryEntry_.consumptionTime),
                buildRangeSpecification(criteria.getQuantity(), DietaryEntry_.quantity),
                buildSpecification(criteria.getUserProfileId(), root ->
                    root.join(DietaryEntry_.userProfile, JoinType.LEFT).get(UserProfile_.id)
                ),
                buildSpecification(criteria.getProductId(), root -> root.join(DietaryEntry_.product, JoinType.LEFT).get(Product_.id))
            );
        }
        return specification;
    }
}
