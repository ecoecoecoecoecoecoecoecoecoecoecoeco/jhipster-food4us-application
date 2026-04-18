package com.eco.food4us.domain;

import static com.eco.food4us.domain.SupplierTestSamples.*;
import static com.eco.food4us.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.eco.food4us.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SupplierTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Supplier.class);
        Supplier supplier1 = getSupplierSample1();
        Supplier supplier2 = new Supplier();
        assertThat(supplier1).isNotEqualTo(supplier2);

        supplier2.setId(supplier1.getId());
        assertThat(supplier1).isEqualTo(supplier2);

        supplier2 = getSupplierSample2();
        assertThat(supplier1).isNotEqualTo(supplier2);
    }

    @Test
    void userProfileTest() {
        Supplier supplier = getSupplierRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        supplier.setUserProfile(userProfileBack);
        assertThat(supplier.getUserProfile()).isEqualTo(userProfileBack);

        supplier.userProfile(null);
        assertThat(supplier.getUserProfile()).isNull();
    }
}
