package com.cosmocats.market.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    @Autowired
    MockMvc mvc;

    @Value("${security.api-key.value}")
    String apiKey;

    @Value("${security.api-key.header-name}")
    String apiKeyHeaderName;

    @Test
    void products_withoutAuth_returns401() throws Exception {
        mvc.perform(get("/api/v1/products"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void products_withValidApiKey_returns200() throws Exception {
        mvc.perform(get("/api/v1/products")
                        .header(apiKeyHeaderName, apiKey))
                .andExpect(status().isOk());
    }

    @Test
    void products_withValidJwt_returns200() throws Exception {
        mvc.perform(get("/api/v1/products")
                        .with(jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isOk());
    }
}
