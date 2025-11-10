package com.cosmocats.market.it;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "feature.cosmoCats.enabled=true")
class CosmoCatControllerIT {

    @Autowired
    MockMvc mvc;

    @Test
    void getCosmoCats_enabled_returns200() throws Exception {
        mvc.perform(get("/api/cosmo-cats"))
                .andExpect(status().isOk());
    }
}
