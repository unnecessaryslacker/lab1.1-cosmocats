package com.cosmocats.market.feature;

import com.cosmocats.market.service.CosmoCatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@TestPropertySource(properties = "feature.cosmoCats.enabled=false")
class CosmoCatServiceFeatureOffTest {

    @MockBean
    private JwtDecoder jwtDecoder;



    @Autowired
    CosmoCatService service;

    @Test
    void getCosmoCats_disabled_throwsException() {
        assertThatThrownBy(() -> service.getCosmoCats())
                .isInstanceOf(FeatureNotAvailableException.class)
                .hasMessageContaining("cosmoCats");
    }
}
