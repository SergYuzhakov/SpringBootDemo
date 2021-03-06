package com.sbapp.starters;

import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.boot.diagnostics.FailureAnalyzer;

/*
Добавим большей информативности при аварийном завершении приложения
 */

public class ProfileNeededFailureAnalyse implements FailureAnalyzer {
    @Override
    public FailureAnalysis analyze(Throwable failure) {
        if(failure.getMessage().contains("Should have production profile")){
            return new FailureAnalysis("Should have production profile", "ADD --spring.profile.active=production", failure);
        }
        return null;
    }
}
