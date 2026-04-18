package com.eco.food4us.domain;

import com.eco.food4us.domain.enumeration.UserType;
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
 * A UserProfile.
 */
@Entity
@Table(name = "user_profile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserProfile implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private UserType userType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User internalUser;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_user_profile__allergy_intolerance",
        joinColumns = @JoinColumn(name = "user_profile_id"),
        inverseJoinColumns = @JoinColumn(name = "allergy_intolerance_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "userProfiles" }, allowSetters = true)
    private Set<AllergyIntolerance> allergyIntolerances = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserProfile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserType getUserType() {
        return this.userType;
    }

    public UserProfile userType(UserType userType) {
        this.setUserType(userType);
        return this;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public User getInternalUser() {
        return this.internalUser;
    }

    public void setInternalUser(User user) {
        this.internalUser = user;
    }

    public UserProfile internalUser(User user) {
        this.setInternalUser(user);
        return this;
    }

    public Set<AllergyIntolerance> getAllergyIntolerances() {
        return this.allergyIntolerances;
    }

    public void setAllergyIntolerances(Set<AllergyIntolerance> allergyIntolerances) {
        this.allergyIntolerances = allergyIntolerances;
    }

    public UserProfile allergyIntolerances(Set<AllergyIntolerance> allergyIntolerances) {
        this.setAllergyIntolerances(allergyIntolerances);
        return this;
    }

    public UserProfile addAllergyIntolerance(AllergyIntolerance allergyIntolerance) {
        this.allergyIntolerances.add(allergyIntolerance);
        return this;
    }

    public UserProfile removeAllergyIntolerance(AllergyIntolerance allergyIntolerance) {
        this.allergyIntolerances.remove(allergyIntolerance);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserProfile)) {
            return false;
        }
        return getId() != null && getId().equals(((UserProfile) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserProfile{" +
            "id=" + getId() +
            ", userType='" + getUserType() + "'" +
            "}";
    }
}
