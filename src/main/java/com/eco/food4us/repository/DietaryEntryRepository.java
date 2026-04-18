package com.eco.food4us.repository;

import com.eco.food4us.domain.DietaryEntry;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DietaryEntry entity.
 */
@Repository
public interface DietaryEntryRepository extends JpaRepository<DietaryEntry, Long>, JpaSpecificationExecutor<DietaryEntry> {
    default Optional<DietaryEntry> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<DietaryEntry> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<DietaryEntry> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select dietaryEntry from DietaryEntry dietaryEntry left join fetch dietaryEntry.product",
        countQuery = "select count(dietaryEntry) from DietaryEntry dietaryEntry"
    )
    Page<DietaryEntry> findAllWithToOneRelationships(Pageable pageable);

    @Query("select dietaryEntry from DietaryEntry dietaryEntry left join fetch dietaryEntry.product")
    List<DietaryEntry> findAllWithToOneRelationships();

    @Query("select dietaryEntry from DietaryEntry dietaryEntry left join fetch dietaryEntry.product where dietaryEntry.id =:id")
    Optional<DietaryEntry> findOneWithToOneRelationships(@Param("id") Long id);
}
