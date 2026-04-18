package com.eco.food4us.service.dto;

import com.eco.food4us.domain.enumeration.AllergyIntoleranceType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.eco.food4us.domain.AllergyIntolerance} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AllergyIntoleranceDTO implements Serializable {

    private Long id;

    @NotNull
    private AllergyIntoleranceType type;

    @Size(max = 300)
    private String description;

    private Set<UserProfileDTO> userProfiles = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AllergyIntoleranceType getType() {
        return type;
    }

    public void setType(AllergyIntoleranceType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<UserProfileDTO> getUserProfiles() {
        return userProfiles;
    }

    public void setUserProfiles(Set<UserProfileDTO> userProfiles) {
        this.userProfiles = userProfiles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AllergyIntoleranceDTO)) {
            return false;
        }

        AllergyIntoleranceDTO allergyIntoleranceDTO = (AllergyIntoleranceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, allergyIntoleranceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AllergyIntoleranceDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", description='" + getDescription() + "'" +
            ", userProfiles=" + getUserProfiles() +
            "}";
    }
}
