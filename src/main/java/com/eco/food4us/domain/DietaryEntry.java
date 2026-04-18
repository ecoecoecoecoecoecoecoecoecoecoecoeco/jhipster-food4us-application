package com.eco.food4us.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DietaryEntry.
 */
@Entity
@Table(name = "dietary_entry")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DietaryEntry implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "consumption_date", nullable = false)
    private LocalDate consumptionDate;

    @NotNull
    @Column(name = "consumption_time", nullable = false)
    private LocalTime consumptionTime;

    @NotNull
    @Min(value = 1)
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "internalUser", "allergyIntolerances" }, allowSetters = true)
    private UserProfile userProfile;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "warehouse", "supplier" }, allowSetters = true)
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DietaryEntry id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getConsumptionDate() {
        return this.consumptionDate;
    }

    public DietaryEntry consumptionDate(LocalDate consumptionDate) {
        this.setConsumptionDate(consumptionDate);
        return this;
    }

    public void setConsumptionDate(LocalDate consumptionDate) {
        this.consumptionDate = consumptionDate;
    }

    public LocalTime getConsumptionTime() {
        return this.consumptionTime;
    }

    public DietaryEntry consumptionTime(LocalTime consumptionTime) {
        this.setConsumptionTime(consumptionTime);
        return this;
    }

    public void setConsumptionTime(LocalTime consumptionTime) {
        this.consumptionTime = consumptionTime;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public DietaryEntry quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public UserProfile getUserProfile() {
        return this.userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public DietaryEntry userProfile(UserProfile userProfile) {
        this.setUserProfile(userProfile);
        return this;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public DietaryEntry product(Product product) {
        this.setProduct(product);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DietaryEntry)) {
            return false;
        }
        return getId() != null && getId().equals(((DietaryEntry) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DietaryEntry{" +
            "id=" + getId() +
            ", consumptionDate='" + getConsumptionDate() + "'" +
            ", consumptionTime='" + getConsumptionTime() + "'" +
            ", quantity=" + getQuantity() +
            "}";
    }
}
