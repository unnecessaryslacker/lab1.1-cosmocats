package com.cosmocats.market.feature;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class FeatureToggleConfiguration {

    @Bean("featureToggles")
    public Map<String, Boolean> featureToggles(Environment env) {
        Map<String, Boolean> toggles = new HashMap<>();
        toggles.put("cosmoCats",
                env.getProperty("feature.cosmoCats.enabled", Boolean.class, false));
        toggles.put("kittyProducts",
                env.getProperty("feature.kittyProducts.enabled", Boolean.class, false));
        return toggles;
    }
}
