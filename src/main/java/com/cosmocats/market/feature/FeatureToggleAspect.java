package com.cosmocats.market.feature;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class FeatureToggleAspect {

    private final FeatureToggleService featureToggleService;

    public FeatureToggleAspect(FeatureToggleService featureToggleService) {
        this.featureToggleService = featureToggleService;
    }

    @Around("@annotation(featureToggle)")
    public Object checkFeature(ProceedingJoinPoint pjp, FeatureToggle featureToggle) throws Throwable {
        String key = featureToggle.value();
        if (featureToggleService.isEnabled(key)) {
            return pjp.proceed();
        }
        throw new FeatureNotAvailableException("Feature '" + key + "' is disabled");
    }
}
