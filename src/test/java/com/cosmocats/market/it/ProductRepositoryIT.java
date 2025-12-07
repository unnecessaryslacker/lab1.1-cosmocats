package com.cosmocats.market.it;

import com.cosmocats.market.domain.CategoryEntity;
import com.cosmocats.market.domain.ProductEntity;
import com.cosmocats.market.repository.CategoryRepository;
import com.cosmocats.market.repository.ProductEntityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Testcontainers
class ProductRepositoryIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("cosmocats")
            .withUsername("cosmocats")
            .withPassword("cosmocats");

    @DynamicPropertySource
    static void configureDatasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    ProductEntityRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    void saveAndLoadProduct() {
        CategoryEntity category = new CategoryEntity();
        category.setName("Space Food");
        category = categoryRepository.save(category);

        ProductEntity product = new ProductEntity();
        product.setName("Galactic Milk");
        product.setPrice(new BigDecimal("9.99"));
        product.setCurrency("USD");
        product.setCategory(category);

        productRepository.save(product);

        assertThat(productRepository.findAll()).hasSize(1);
    }
}
