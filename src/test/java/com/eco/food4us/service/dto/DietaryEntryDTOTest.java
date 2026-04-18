package com.eco.food4us.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.eco.food4us.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DietaryEntryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DietaryEntryDTO.class);
        DietaryEntryDTO dietaryEntryDTO1 = new DietaryEntryDTO();
        dietaryEntryDTO1.setId(1L);
        DietaryEntryDTO dietaryEntryDTO2 = new DietaryEntryDTO();
        assertThat(dietaryEntryDTO1).isNotEqualTo(dietaryEntryDTO2);
        dietaryEntryDTO2.setId(dietaryEntryDTO1.getId());
        assertThat(dietaryEntryDTO1).isEqualTo(dietaryEntryDTO2);
        dietaryEntryDTO2.setId(2L);
        assertThat(dietaryEntryDTO1).isNotEqualTo(dietaryEntryDTO2);
        dietaryEntryDTO1.setId(null);
        assertThat(dietaryEntryDTO1).isNotEqualTo(dietaryEntryDTO2);
    }
}
