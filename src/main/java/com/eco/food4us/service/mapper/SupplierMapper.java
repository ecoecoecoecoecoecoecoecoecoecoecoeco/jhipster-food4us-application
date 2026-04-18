package com.eco.food4us.service.mapper;

import com.eco.food4us.domain.Supplier;
import com.eco.food4us.domain.UserProfile;
import com.eco.food4us.service.dto.SupplierDTO;
import com.eco.food4us.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Supplier} and its DTO {@link SupplierDTO}.
 */
@Mapper(componentModel = "spring")
public interface SupplierMapper extends EntityMapper<SupplierDTO, Supplier> {
    @Mapping(target = "userProfile", source = "userProfile", qualifiedByName = "userProfileId")
    SupplierDTO toDto(Supplier s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
