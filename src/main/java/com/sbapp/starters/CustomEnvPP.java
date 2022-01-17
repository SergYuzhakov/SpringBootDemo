package com.sbapp.starters;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

public class CustomEnvPP implements EnvironmentPostProcessor {
    /*
    Для установки Profile приложения до поднятия всего контекста можно воспользоваться EPP - например, получив название ОС установить соответствующий профиль
     */
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        System.out.println("EnvironmentPostProcessor worked...");
        String os = System.getProperty("os.name");
        if(os.equalsIgnoreCase("windows 10")){
            environment.addActiveProfile("prod");
        }

    }
}
