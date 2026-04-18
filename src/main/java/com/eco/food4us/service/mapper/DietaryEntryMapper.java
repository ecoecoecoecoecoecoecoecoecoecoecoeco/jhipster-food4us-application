package com.eco.food4us.service.mapper;

import com.eco.food4us.domain.DietaryEntry;
import com.eco.food4us.domain.Product;
import com.eco.food4us.domain.UserProfile;
import com.eco.food4us.service.dto.DietaryEntryDTO;
import com.eco.food4us.service.dto.ProductDTO;
import com.eco.food4us.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DietaryEntry} and its DTO {@link DietaryEntryDTO}.
 */
@Mapper(componentModel = "spring")
public interface DietaryEntryMapper extends EntityMapper<DietaryEntryDTO, DietaryEntry> {
    @Mapping(target = "userProfile", source = "userProfile", qualifiedByName = "userProfileId")
    @Mapping(target = "product", source = "product", qualifiedByName = "productName")
    DietaryEntryDTO toDto(DietaryEntry s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);

    @Named("productName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductDTO toDtoProductName(Product product);
}
