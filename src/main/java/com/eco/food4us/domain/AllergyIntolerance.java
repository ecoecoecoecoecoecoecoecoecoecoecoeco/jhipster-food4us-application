package com.eco.food4us.domain;

import com.eco.food4us.domain.enumeration.AllergyIntoleranceType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AllergyIntolerance.
 */
@Entity
@Table(name = "allergy_intolerance")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AllergyIntolerance implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private AllergyIntoleranceType type;

    @Size(max = 300)
    @Column(name = "description", length = 300)
    private String description;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "allergyIntolerances")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "internalUser", "allergyIntolerances" }, allowSetters = true)
    private Set<UserProfile> userProfiles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AllergyIntolerance id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AllergyIntoleranceType getType() {
        return this.type;
    }

    public AllergyIntolerance type(AllergyIntoleranceType type) {
        this.setType(type);
        return this;
    }

    public void setType(AllergyIntoleranceType type) {
        this.type = type;
    }

    public String getDescription() {
        return this.description;
    }

    public AllergyIntolerance description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<UserProfile> getUserProfiles() {
        return this.userProfiles;
    }

    public void setUserProfiles(Set<UserProfile> userProfiles) {
        if (this.userProfiles != null) {
            this.userProfiles.forEach(i -> i.removeAllergyIntolerance(this));
        }
        if (userProfiles != null) {
            userProfiles.forEach(i -> i.addAllergyIntolerance(this));
        }
        this.userProfiles = userProfiles;
    }

    public AllergyIntolerance userProfiles(Set<UserProfile> userProfiles) {
        this.setUserProfiles(userProfiles);
        return this;
    }

    public AllergyIntolerance addUserProfile(UserProfile userProfile) {
        this.userProfiles.add(userProfile);
        userProfile.getAllergyIntolerances().add(this);
        return this;
    }

    public AllergyIntolerance removeUserProfile(UserProfile userProfile) {
        this.userProfiles.remove(userProfile);
        userProfile.getAllergyIntolerances().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AllergyIntolerance)) {
            return false;
        }
        return getId() != null && getId().equals(((AllergyIntolerance) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AllergyIntolerance{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
