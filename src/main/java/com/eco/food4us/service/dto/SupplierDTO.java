package com.eco.food4us.service.dto;

import com.eco.food4us.domain.enumeration.SupplierType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.eco.food4us.domain.Supplier} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SupplierDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 150)
    private String name;

    @NotNull
    @Size(max = 300)
    private String address;

    @Size(max = 100)
    private String contact;

    @Size(max = 200)
    private String website;

    private SupplierType supplierType;

    private UserProfileDTO userProfile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public SupplierType getSupplierType() {
        return supplierType;
    }

    public void setSupplierType(SupplierType supplierType) {
        this.supplierType = supplierType;
    }

    public UserProfileDTO getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfileDTO userProfile) {
        this.userProfile = userProfile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SupplierDTO)) {
            return false;
        }

        SupplierDTO supplierDTO = (SupplierDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, supplierDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SupplierDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            ", contact='" + getContact() + "'" +
            ", website='" + getWebsite() + "'" +
            ", supplierType='" + getSupplierType() + "'" +
            ", userProfile=" + getUserProfile() +
            "}";
    }
}
