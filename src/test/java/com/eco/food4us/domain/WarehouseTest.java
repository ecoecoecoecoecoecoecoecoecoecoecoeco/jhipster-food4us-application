package com.eco.food4us.domain;

import static com.eco.food4us.domain.UserProfileTestSamples.*;
import static com.eco.food4us.domain.WarehouseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.eco.food4us.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WarehouseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Warehouse.class);
        Warehouse warehouse1 = getWarehouseSample1();
        Warehouse warehouse2 = new Warehouse();
        assertThat(warehouse1).isNotEqualTo(warehouse2);

        warehouse2.setId(warehouse1.getId());
        assertThat(warehouse1).isEqualTo(warehouse2);

        warehouse2 = getWarehouseSample2();
        assertThat(warehouse1).isNotEqualTo(warehouse2);
    }

    @Test
    void userProfileTest() {
        Warehouse warehouse = getWarehouseRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        warehouse.setUserProfile(userProfileBack);
        assertThat(warehouse.getUserProfile()).isEqualTo(userProfileBack);

        warehouse.userProfile(null);
        assertThat(warehouse.getUserProfile()).isNull();
    }
}
