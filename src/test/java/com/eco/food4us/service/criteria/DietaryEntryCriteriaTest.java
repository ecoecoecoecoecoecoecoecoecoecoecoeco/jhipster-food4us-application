package com.eco.food4us.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DietaryEntryCriteriaTest {

    @Test
    void newDietaryEntryCriteriaHasAllFiltersNullTest() {
        var dietaryEntryCriteria = new DietaryEntryCriteria();
        assertThat(dietaryEntryCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void dietaryEntryCriteriaFluentMethodsCreatesFiltersTest() {
        var dietaryEntryCriteria = new DietaryEntryCriteria();

        setAllFilters(dietaryEntryCriteria);

        assertThat(dietaryEntryCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void dietaryEntryCriteriaCopyCreatesNullFilterTest() {
        var dietaryEntryCriteria = new DietaryEntryCriteria();
        var copy = dietaryEntryCriteria.copy();

        assertThat(dietaryEntryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(dietaryEntryCriteria)
        );
    }

    @Test
    void dietaryEntryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var dietaryEntryCriteria = new DietaryEntryCriteria();
        setAllFilters(dietaryEntryCriteria);

        var copy = dietaryEntryCriteria.copy();

        assertThat(dietaryEntryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(dietaryEntryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var dietaryEntryCriteria = new DietaryEntryCriteria();

        assertThat(dietaryEntryCriteria).hasToString("DietaryEntryCriteria{}");
    }

    private static void setAllFilters(DietaryEntryCriteria dietaryEntryCriteria) {
        dietaryEntryCriteria.id();
        dietaryEntryCriteria.consumptionDate();
        dietaryEntryCriteria.consumptionTime();
        dietaryEntryCriteria.quantity();
        dietaryEntryCriteria.userProfileId();
        dietaryEntryCriteria.productId();
        dietaryEntryCriteria.distinct();
    }

    private static Condition<DietaryEntryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getConsumptionDate()) &&
                condition.apply(criteria.getConsumptionTime()) &&
                condition.apply(criteria.getQuantity()) &&
                condition.apply(criteria.getUserProfileId()) &&
                condition.apply(criteria.getProductId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DietaryEntryCriteria> copyFiltersAre(
        DietaryEntryCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getConsumptionDate(), copy.getConsumptionDate()) &&
                condition.apply(criteria.getConsumptionTime(), copy.getConsumptionTime()) &&
                condition.apply(criteria.getQuantity(), copy.getQuantity()) &&
                condition.apply(criteria.getUserProfileId(), copy.getUserProfileId()) &&
                condition.apply(criteria.getProductId(), copy.getProductId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
