package com.eco.food4us.repository;

import com.eco.food4us.domain.Ingredient;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Ingredient entity.
 */
@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    default Optional<Ingredient> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Ingredient> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Ingredient> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select ingredient from Ingredient ingredient left join fetch ingredient.recipe",
        countQuery = "select count(ingredient) from Ingredient ingredient"
    )
    Page<Ingredient> findAllWithToOneRelationships(Pageable pageable);

    @Query("select ingredient from Ingredient ingredient left join fetch ingredient.recipe")
    List<Ingredient> findAllWithToOneRelationships();

    @Query("select ingredient from Ingredient ingredient left join fetch ingredient.recipe where ingredient.id =:id")
    Optional<Ingredient> findOneWithToOneRelationships(@Param("id") Long id);
}
