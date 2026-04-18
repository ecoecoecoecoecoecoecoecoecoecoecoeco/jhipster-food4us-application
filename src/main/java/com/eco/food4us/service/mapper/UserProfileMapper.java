package com.eco.food4us.service.mapper;

import com.eco.food4us.domain.AllergyIntolerance;
import com.eco.food4us.domain.User;
import com.eco.food4us.domain.UserProfile;
import com.eco.food4us.service.dto.AllergyIntoleranceDTO;
import com.eco.food4us.service.dto.UserDTO;
import com.eco.food4us.service.dto.UserProfileDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserProfile} and its DTO {@link UserProfileDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserProfileMapper extends EntityMapper<UserProfileDTO, UserProfile> {
    @Mapping(target = "internalUser", source = "internalUser", qualifiedByName = "userLogin")
    @Mapping(target = "allergyIntolerances", source = "allergyIntolerances", qualifiedByName = "allergyIntoleranceIdSet")
    UserProfileDTO toDto(UserProfile s);

    @Mapping(target = "removeAllergyIntolerance", ignore = true)
    UserProfile toEntity(UserProfileDTO userProfileDTO);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("allergyIntoleranceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AllergyIntoleranceDTO toDtoAllergyIntoleranceId(AllergyIntolerance allergyIntolerance);

    @Named("allergyIntoleranceIdSet")
    default Set<AllergyIntoleranceDTO> toDtoAllergyIntoleranceIdSet(Set<AllergyIntolerance> allergyIntolerance) {
        return allergyIntolerance.stream().map(this::toDtoAllergyIntoleranceId).collect(Collectors.toSet());
    }
}
