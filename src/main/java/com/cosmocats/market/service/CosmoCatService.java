package com.cosmocats.market.service;

import com.cosmocats.market.feature.FeatureToggle;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CosmoCatService {

    @FeatureToggle("cosmoCats")
    public List<String> getCosmoCats() {
        return List.of(
                "Luna Galaxy",
                "Comet Whiskers",
                "Nebula Paws"
        );
    }
}
