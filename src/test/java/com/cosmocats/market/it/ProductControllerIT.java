package com.cosmocats.market.it;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerIT {

    @Autowired
    MockMvc mvc;

    @MockBean
    JwtDecoder jwtDecoder;


    @Test
    void create_works() throws Exception {
        String body = """
      {
        "name": "Galaxy Snack",
        "description": "Bar",
        "price": 3.30,
        "currency": "USD",
        "categoryId": "CAT-001"
      }
      """;

        mvc.perform(post("/api/v1/products")
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "cosmo.write")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }
}
