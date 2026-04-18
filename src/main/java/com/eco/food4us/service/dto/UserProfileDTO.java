package com.eco.food4us.service.dto;

import com.eco.food4us.domain.enumeration.UserType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.eco.food4us.domain.UserProfile} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserProfileDTO implements Serializable {

    private Long id;

    @NotNull
    private UserType userType;

    private UserDTO internalUser;

    private Set<AllergyIntoleranceDTO> allergyIntolerances = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public UserDTO getInternalUser() {
        return internalUser;
    }

    public void setInternalUser(UserDTO internalUser) {
        this.internalUser = internalUser;
    }

    public Set<AllergyIntoleranceDTO> getAllergyIntolerances() {
        return allergyIntolerances;
    }

    public void setAllergyIntolerances(Set<AllergyIntoleranceDTO> allergyIntolerances) {
        this.allergyIntolerances = allergyIntolerances;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserProfileDTO)) {
            return false;
        }

        UserProfileDTO userProfileDTO = (UserProfileDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userProfileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserProfileDTO{" +
            "id=" + getId() +
            ", userType='" + getUserType() + "'" +
            ", internalUser=" + getInternalUser() +
            ", allergyIntolerances=" + getAllergyIntolerances() +
            "}";
    }
}
