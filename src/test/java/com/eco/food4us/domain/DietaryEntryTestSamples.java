package com.eco.food4us.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DietaryEntryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static DietaryEntry getDietaryEntrySample1() {
        return new DietaryEntry().id(1L).quantity(1);
    }

    public static DietaryEntry getDietaryEntrySample2() {
        return new DietaryEntry().id(2L).quantity(2);
    }

    public static DietaryEntry getDietaryEntryRandomSampleGenerator() {
        return new DietaryEntry().id(longCount.incrementAndGet()).quantity(intCount.incrementAndGet());
    }
}
