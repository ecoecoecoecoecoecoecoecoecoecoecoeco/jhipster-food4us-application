package com.eco.food4us.repository;

import com.eco.food4us.domain.AllergyIntolerance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AllergyIntolerance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AllergyIntoleranceRepository extends JpaRepository<AllergyIntolerance, Long> {}
