package com.eco.food4us.service.mapper;

import static com.eco.food4us.domain.UserProfileAsserts.*;
import static com.eco.food4us.domain.UserProfileTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserProfileMapperTest {

    private UserProfileMapper userProfileMapper;

    @BeforeEach
    void setUp() {
        userProfileMapper = new UserProfileMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUserProfileSample1();
        var actual = userProfileMapper.toEntity(userProfileMapper.toDto(expected));
        assertUserProfileAllPropertiesEquals(expected, actual);
    }
}
