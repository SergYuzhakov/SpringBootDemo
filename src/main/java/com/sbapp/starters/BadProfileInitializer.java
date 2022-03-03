package com.sbapp.starters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/*
На этапе инициализации еще не заполненного контекста можно прекратить запуск приложения если не установлены соответсвующие условия
 */

public class BadProfileInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final Logger log = LoggerFactory.getLogger(BadProfileInitializer.class);

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        log.info("BadProfileInitializer working ...");
        if (applicationContext.getEnvironment().getActiveProfiles().length == 0) {
            throw new RuntimeException("Should have production profile");
        }
    }
}
