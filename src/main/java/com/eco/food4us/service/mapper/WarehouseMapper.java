package com.eco.food4us.service.mapper;

import com.eco.food4us.domain.UserProfile;
import com.eco.food4us.domain.Warehouse;
import com.eco.food4us.service.dto.UserProfileDTO;
import com.eco.food4us.service.dto.WarehouseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Warehouse} and its DTO {@link WarehouseDTO}.
 */
@Mapper(componentModel = "spring")
public interface WarehouseMapper extends EntityMapper<WarehouseDTO, Warehouse> {
    @Mapping(target = "userProfile", source = "userProfile", qualifiedByName = "userProfileId")
    WarehouseDTO toDto(Warehouse s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
