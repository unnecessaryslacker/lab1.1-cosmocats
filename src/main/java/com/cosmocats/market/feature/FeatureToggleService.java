package com.cosmocats.market.feature;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FeatureToggleService {

    private final Map<String, Boolean> toggles;

    public FeatureToggleService(@Qualifier("featureToggles") Map<String, Boolean> toggles) {
        this.toggles = toggles;
    }

    public boolean isEnabled(String featureKey) {
        return Boolean.TRUE.equals(toggles.get(featureKey));
    }
}
