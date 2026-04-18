package com.eco.food4us;

import com.eco.food4us.config.AsyncSyncConfiguration;
import com.eco.food4us.config.EmbeddedSQL;
import com.eco.food4us.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = {
        Food4UsApp.class,
        JacksonConfiguration.class,
        AsyncSyncConfiguration.class,
        com.eco.food4us.config.JacksonHibernateConfiguration.class,
    }
)
@EmbeddedSQL
public @interface IntegrationTest {}
