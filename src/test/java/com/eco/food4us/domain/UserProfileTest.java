package com.eco.food4us.domain;

import static com.eco.food4us.domain.AllergyIntoleranceTestSamples.*;
import static com.eco.food4us.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.eco.food4us.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UserProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserProfile.class);
        UserProfile userProfile1 = getUserProfileSample1();
        UserProfile userProfile2 = new UserProfile();
        assertThat(userProfile1).isNotEqualTo(userProfile2);

        userProfile2.setId(userProfile1.getId());
        assertThat(userProfile1).isEqualTo(userProfile2);

        userProfile2 = getUserProfileSample2();
        assertThat(userProfile1).isNotEqualTo(userProfile2);
    }

    @Test
    void allergyIntoleranceTest() {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        AllergyIntolerance allergyIntoleranceBack = getAllergyIntoleranceRandomSampleGenerator();

        userProfile.addAllergyIntolerance(allergyIntoleranceBack);
        assertThat(userProfile.getAllergyIntolerances()).containsOnly(allergyIntoleranceBack);

        userProfile.removeAllergyIntolerance(allergyIntoleranceBack);
        assertThat(userProfile.getAllergyIntolerances()).doesNotContain(allergyIntoleranceBack);

        userProfile.allergyIntolerances(new HashSet<>(Set.of(allergyIntoleranceBack)));
        assertThat(userProfile.getAllergyIntolerances()).containsOnly(allergyIntoleranceBack);

        userProfile.setAllergyIntolerances(new HashSet<>());
        assertThat(userProfile.getAllergyIntolerances()).doesNotContain(allergyIntoleranceBack);
    }
}
