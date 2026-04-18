package com.eco.food4us.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.eco.food4us.domain.DietaryEntry} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DietaryEntryDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate consumptionDate;

    @NotNull
    private LocalTime consumptionTime;

    @NotNull
    @Min(value = 1)
    private Integer quantity;

    @NotNull
    private UserProfileDTO userProfile;

    @NotNull
    private ProductDTO product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getConsumptionDate() {
        return consumptionDate;
    }

    public void setConsumptionDate(LocalDate consumptionDate) {
        this.consumptionDate = consumptionDate;
    }

    public LocalTime getConsumptionTime() {
        return consumptionTime;
    }

    public void setConsumptionTime(LocalTime consumptionTime) {
        this.consumptionTime = consumptionTime;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public UserProfileDTO getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfileDTO userProfile) {
        this.userProfile = userProfile;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DietaryEntryDTO)) {
            return false;
        }

        DietaryEntryDTO dietaryEntryDTO = (DietaryEntryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, dietaryEntryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DietaryEntryDTO{" +
            "id=" + getId() +
            ", consumptionDate='" + getConsumptionDate() + "'" +
            ", consumptionTime='" + getConsumptionTime() + "'" +
            ", quantity=" + getQuantity() +
            ", userProfile=" + getUserProfile() +
            ", product=" + getProduct() +
            "}";
    }
}
