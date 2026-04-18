package com.eco.food4us.service.mapper;

import com.eco.food4us.domain.AllergyIntolerance;
import com.eco.food4us.domain.UserProfile;
import com.eco.food4us.service.dto.AllergyIntoleranceDTO;
import com.eco.food4us.service.dto.UserProfileDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AllergyIntolerance} and its DTO {@link AllergyIntoleranceDTO}.
 */
@Mapper(componentModel = "spring")
public interface AllergyIntoleranceMapper extends EntityMapper<AllergyIntoleranceDTO, AllergyIntolerance> {
    @Mapping(target = "userProfiles", source = "userProfiles", qualifiedByName = "userProfileIdSet")
    AllergyIntoleranceDTO toDto(AllergyIntolerance s);

    @Mapping(target = "userProfiles", ignore = true)
    @Mapping(target = "removeUserProfile", ignore = true)
    AllergyIntolerance toEntity(AllergyIntoleranceDTO allergyIntoleranceDTO);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);

    @Named("userProfileIdSet")
    default Set<UserProfileDTO> toDtoUserProfileIdSet(Set<UserProfile> userProfile) {
        return userProfile.stream().map(this::toDtoUserProfileId).collect(Collectors.toSet());
    }
}
