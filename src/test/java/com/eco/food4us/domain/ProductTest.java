package com.eco.food4us.domain;

import static com.eco.food4us.domain.ProductTestSamples.*;
import static com.eco.food4us.domain.SupplierTestSamples.*;
import static com.eco.food4us.domain.WarehouseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.eco.food4us.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = getProductSample1();
        Product product2 = new Product();
        assertThat(product1).isNotEqualTo(product2);

        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);

        product2 = getProductSample2();
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void warehouseTest() {
        Product product = getProductRandomSampleGenerator();
        Warehouse warehouseBack = getWarehouseRandomSampleGenerator();

        product.setWarehouse(warehouseBack);
        assertThat(product.getWarehouse()).isEqualTo(warehouseBack);

        product.warehouse(null);
        assertThat(product.getWarehouse()).isNull();
    }

    @Test
    void supplierTest() {
        Product product = getProductRandomSampleGenerator();
        Supplier supplierBack = getSupplierRandomSampleGenerator();

        product.setSupplier(supplierBack);
        assertThat(product.getSupplier()).isEqualTo(supplierBack);

        product.supplier(null);
        assertThat(product.getSupplier()).isNull();
    }
}
