package com.eco.food4us.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.eco.food4us.domain.DietaryEntry} entity. This class is used
 * in {@link com.eco.food4us.web.rest.DietaryEntryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /dietary-entries?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DietaryEntryCriteria implements Serializable, Criteria {

    /**
     * Class for filtering LocalTime
     */
    public static class LocalTimeFilter extends RangeFilter<LocalTime> {

        public LocalTimeFilter() {}

        public LocalTimeFilter(LocalTimeFilter filter) {
            super(filter);
        }

        @Override
        public LocalTimeFilter copy() {
            return new LocalTimeFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter consumptionDate;

    private LocalTimeFilter consumptionTime;

    private IntegerFilter quantity;

    private LongFilter userProfileId;

    private LongFilter productId;

    private Boolean distinct;

    public DietaryEntryCriteria() {}

    public DietaryEntryCriteria(DietaryEntryCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.consumptionDate = other.optionalConsumptionDate().map(LocalDateFilter::copy).orElse(null);
        this.consumptionTime = other.optionalConsumptionTime().map(LocalTimeFilter::copy).orElse(null);
        this.quantity = other.optionalQuantity().map(IntegerFilter::copy).orElse(null);
        this.userProfileId = other.optionalUserProfileId().map(LongFilter::copy).orElse(null);
        this.productId = other.optionalProductId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DietaryEntryCriteria copy() {
        return new DietaryEntryCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getConsumptionDate() {
        return consumptionDate;
    }

    public Optional<LocalDateFilter> optionalConsumptionDate() {
        return Optional.ofNullable(consumptionDate);
    }

    public LocalDateFilter consumptionDate() {
        if (consumptionDate == null) {
            setConsumptionDate(new LocalDateFilter());
        }
        return consumptionDate;
    }

    public void setConsumptionDate(LocalDateFilter consumptionDate) {
        this.consumptionDate = consumptionDate;
    }

    public LocalTimeFilter getConsumptionTime() {
        return consumptionTime;
    }

    public Optional<LocalTimeFilter> optionalConsumptionTime() {
        return Optional.ofNullable(consumptionTime);
    }

    public LocalTimeFilter consumptionTime() {
        if (consumptionTime == null) {
            setConsumptionTime(new LocalTimeFilter());
        }
        return consumptionTime;
    }

    public void setConsumptionTime(LocalTimeFilter consumptionTime) {
        this.consumptionTime = consumptionTime;
    }

    public IntegerFilter getQuantity() {
        return quantity;
    }

    public Optional<IntegerFilter> optionalQuantity() {
        return Optional.ofNullable(quantity);
    }

    public IntegerFilter quantity() {
        if (quantity == null) {
            setQuantity(new IntegerFilter());
        }
        return quantity;
    }

    public void setQuantity(IntegerFilter quantity) {
        this.quantity = quantity;
    }

    public LongFilter getUserProfileId() {
        return userProfileId;
    }

    public Optional<LongFilter> optionalUserProfileId() {
        return Optional.ofNullable(userProfileId);
    }

    public LongFilter userProfileId() {
        if (userProfileId == null) {
            setUserProfileId(new LongFilter());
        }
        return userProfileId;
    }

    public void setUserProfileId(LongFilter userProfileId) {
        this.userProfileId = userProfileId;
    }

    public LongFilter getProductId() {
        return productId;
    }

    public Optional<LongFilter> optionalProductId() {
        return Optional.ofNullable(productId);
    }

    public LongFilter productId() {
        if (productId == null) {
            setProductId(new LongFilter());
        }
        return productId;
    }

    public void setProductId(LongFilter productId) {
        this.productId = productId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DietaryEntryCriteria that = (DietaryEntryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(consumptionDate, that.consumptionDate) &&
            Objects.equals(consumptionTime, that.consumptionTime) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(userProfileId, that.userProfileId) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, consumptionDate, consumptionTime, quantity, userProfileId, productId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DietaryEntryCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalConsumptionDate().map(f -> "consumptionDate=" + f + ", ").orElse("") +
            optionalConsumptionTime().map(f -> "consumptionTime=" + f + ", ").orElse("") +
            optionalQuantity().map(f -> "quantity=" + f + ", ").orElse("") +
            optionalUserProfileId().map(f -> "userProfileId=" + f + ", ").orElse("") +
            optionalProductId().map(f -> "productId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
