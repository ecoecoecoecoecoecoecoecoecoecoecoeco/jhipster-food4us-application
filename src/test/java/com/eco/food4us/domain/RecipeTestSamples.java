package com.eco.food4us.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class RecipeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Recipe getRecipeSample1() {
        return new Recipe().id(1L).name("name1").preparationTime(1).calories(1);
    }

    public static Recipe getRecipeSample2() {
        return new Recipe().id(2L).name("name2").preparationTime(2).calories(2);
    }

    public static Recipe getRecipeRandomSampleGenerator() {
        return new Recipe()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .preparationTime(intCount.incrementAndGet())
            .calories(intCount.incrementAndGet());
    }
}
