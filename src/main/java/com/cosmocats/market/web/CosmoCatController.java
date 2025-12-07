package com.cosmocats.market.web;

import com.cosmocats.market.service.CosmoCatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1") //API versioning
public class CosmoCatController {

    private final CosmoCatService service;

    public CosmoCatController(CosmoCatService service) {
        this.service = service;
    }

    @GetMapping("/cosmo-cats")
    public List<String> getCosmoCats() {
        return service.getCosmoCats();
    }
}
