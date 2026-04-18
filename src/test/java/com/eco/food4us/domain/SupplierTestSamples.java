package com.eco.food4us.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SupplierTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Supplier getSupplierSample1() {
        return new Supplier().id(1L).name("name1").address("address1").contact("contact1").website("website1");
    }

    public static Supplier getSupplierSample2() {
        return new Supplier().id(2L).name("name2").address("address2").contact("contact2").website("website2");
    }

    public static Supplier getSupplierRandomSampleGenerator() {
        return new Supplier()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .contact(UUID.randomUUID().toString())
            .website(UUID.randomUUID().toString());
    }
}
