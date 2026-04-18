package com.eco.food4us.domain;

import static com.eco.food4us.domain.AllergyIntoleranceTestSamples.*;
import static com.eco.food4us.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.eco.food4us.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AllergyIntoleranceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AllergyIntolerance.class);
        AllergyIntolerance allergyIntolerance1 = getAllergyIntoleranceSample1();
        AllergyIntolerance allergyIntolerance2 = new AllergyIntolerance();
        assertThat(allergyIntolerance1).isNotEqualTo(allergyIntolerance2);

        allergyIntolerance2.setId(allergyIntolerance1.getId());
        assertThat(allergyIntolerance1).isEqualTo(allergyIntolerance2);

        allergyIntolerance2 = getAllergyIntoleranceSample2();
        assertThat(allergyIntolerance1).isNotEqualTo(allergyIntolerance2);
    }

    @Test
    void userProfileTest() {
        AllergyIntolerance allergyIntolerance = getAllergyIntoleranceRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        allergyIntolerance.addUserProfile(userProfileBack);
        assertThat(allergyIntolerance.getUserProfiles()).containsOnly(userProfileBack);
        assertThat(userProfileBack.getAllergyIntolerances()).containsOnly(allergyIntolerance);

        allergyIntolerance.removeUserProfile(userProfileBack);
        assertThat(allergyIntolerance.getUserProfiles()).doesNotContain(userProfileBack);
        assertThat(userProfileBack.getAllergyIntolerances()).doesNotContain(allergyIntolerance);

        allergyIntolerance.userProfiles(new HashSet<>(Set.of(userProfileBack)));
        assertThat(allergyIntolerance.getUserProfiles()).containsOnly(userProfileBack);
        assertThat(userProfileBack.getAllergyIntolerances()).containsOnly(allergyIntolerance);

        allergyIntolerance.setUserProfiles(new HashSet<>());
        assertThat(allergyIntolerance.getUserProfiles()).doesNotContain(userProfileBack);
        assertThat(userProfileBack.getAllergyIntolerances()).doesNotContain(allergyIntolerance);
    }
}
