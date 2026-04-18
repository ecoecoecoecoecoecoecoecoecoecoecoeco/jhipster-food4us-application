package com.eco.food4us.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.eco.food4us.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AllergyIntoleranceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AllergyIntoleranceDTO.class);
        AllergyIntoleranceDTO allergyIntoleranceDTO1 = new AllergyIntoleranceDTO();
        allergyIntoleranceDTO1.setId(1L);
        AllergyIntoleranceDTO allergyIntoleranceDTO2 = new AllergyIntoleranceDTO();
        assertThat(allergyIntoleranceDTO1).isNotEqualTo(allergyIntoleranceDTO2);
        allergyIntoleranceDTO2.setId(allergyIntoleranceDTO1.getId());
        assertThat(allergyIntoleranceDTO1).isEqualTo(allergyIntoleranceDTO2);
        allergyIntoleranceDTO2.setId(2L);
        assertThat(allergyIntoleranceDTO1).isNotEqualTo(allergyIntoleranceDTO2);
        allergyIntoleranceDTO1.setId(null);
        assertThat(allergyIntoleranceDTO1).isNotEqualTo(allergyIntoleranceDTO2);
    }
}
