package com.eco.food4us.domain;

import static com.eco.food4us.domain.DietaryEntryTestSamples.*;
import static com.eco.food4us.domain.ProductTestSamples.*;
import static com.eco.food4us.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.eco.food4us.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DietaryEntryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DietaryEntry.class);
        DietaryEntry dietaryEntry1 = getDietaryEntrySample1();
        DietaryEntry dietaryEntry2 = new DietaryEntry();
        assertThat(dietaryEntry1).isNotEqualTo(dietaryEntry2);

        dietaryEntry2.setId(dietaryEntry1.getId());
        assertThat(dietaryEntry1).isEqualTo(dietaryEntry2);

        dietaryEntry2 = getDietaryEntrySample2();
        assertThat(dietaryEntry1).isNotEqualTo(dietaryEntry2);
    }

    @Test
    void userProfileTest() {
        DietaryEntry dietaryEntry = getDietaryEntryRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        dietaryEntry.setUserProfile(userProfileBack);
        assertThat(dietaryEntry.getUserProfile()).isEqualTo(userProfileBack);

        dietaryEntry.userProfile(null);
        assertThat(dietaryEntry.getUserProfile()).isNull();
    }

    @Test
    void productTest() {
        DietaryEntry dietaryEntry = getDietaryEntryRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        dietaryEntry.setProduct(productBack);
        assertThat(dietaryEntry.getProduct()).isEqualTo(productBack);

        dietaryEntry.product(null);
        assertThat(dietaryEntry.getProduct()).isNull();
    }
}
