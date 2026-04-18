package com.eco.food4us.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AllergyIntoleranceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static AllergyIntolerance getAllergyIntoleranceSample1() {
        return new AllergyIntolerance().id(1L).description("description1");
    }

    public static AllergyIntolerance getAllergyIntoleranceSample2() {
        return new AllergyIntolerance().id(2L).description("description2");
    }

    public static AllergyIntolerance getAllergyIntoleranceRandomSampleGenerator() {
        return new AllergyIntolerance().id(longCount.incrementAndGet()).description(UUID.randomUUID().toString());
    }
}
