package com.cosmocats.market.web;

import com.cosmocats.market.domain.Product;
import com.cosmocats.market.dto.ProductDto;
import com.cosmocats.market.mapper.ProductMapper;
import com.cosmocats.market.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@WithMockUser
@Import(ErrorHandler.class)
class ProductControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    ProductService service;

    @MockBean
    ProductMapper mapper;

    private Product domain(UUID id) {
        return new Product(
                id,
                "Galaxy Snack",
                "Bar",
                new BigDecimal("4.20"),
                "USD",
                "CAT-001"
        );
    }

    private ProductDto dto(UUID id) {
        return new ProductDto(
                id,
                "Galaxy Snack",
                "Bar",
                new BigDecimal("4.20"),
                "USD",
                "CAT-001"
        );
    }

    @Test
    void list_200() throws Exception {
        var p = domain(UUID.randomUUID());
        when(service.list()).thenReturn(List.of(p));
        when(mapper.toDto(p)).thenReturn(dto(p.id()));

        mvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(p.id().toString()));
    }

    @Test
    void get_200() throws Exception {
        var id = UUID.randomUUID();
        var p = domain(id);
        when(service.get(id)).thenReturn(p);
        when(mapper.toDto(p)).thenReturn(dto(id));

        mvc.perform(get("/api/v1/products/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void get_404() throws Exception {
        var id = UUID.randomUUID();
        when(service.get(id)).thenThrow(
                new NoSuchElementException("Product %s not found".formatted(id))
        );

        mvc.perform(get("/api/v1/products/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Product %s not found".formatted(id)));
    }

    @Test
    void create_valid_201() throws Exception {
        var saved = domain(UUID.randomUUID());
        when(mapper.toDomain(any(ProductDto.class))).thenReturn(domain(null));
        when(service.create(any(Product.class))).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(dto(saved.id()));

        String body = """
        {
          "name":"Galaxy Snack",
          "description":"Bar",
          "price":4.20,
          "currency":"USD",
          "categoryId":"CAT-001"
        }
        """;

        mvc.perform(post("/api/v1/products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(saved.id().toString()));
    }

    @Test
    void create_invalid_400() throws Exception {
        String bad = """
        {
          "name":"",
          "description":"Bar",
          "price":0,
          "currency":"usd",
          "categoryId":""
        }
        """;

        mvc.perform(post("/api/v1/products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bad))
                .andExpect(status().isBadRequest())
                // это ProblemDetail, а не "message"
                .andExpect(jsonPath("$.type").value("https://api.cosmocats/errors/validation"))
                .andExpect(jsonPath("$.detail").value("Request body validation failed"))
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    void update_200() throws Exception {
        var id = UUID.randomUUID();
        var updated = new Product(
                id,
                "Star Item",
                "NewDesc",
                new BigDecimal("9.99"),
                "EUR",
                "CAT-777"
        );

        when(mapper.toDomain(any(ProductDto.class))).thenReturn(updated);
        when(service.update(id, updated)).thenReturn(updated);
        when(mapper.toDto(updated)).thenReturn(
                new ProductDto(
                        id,
                        updated.name(),
                        updated.description(),
                        updated.price(),
                        updated.currency(),
                        updated.categoryId()
                )
        );

        String body = """
        {
          "name":"Star Item",
          "description":"NewDesc",
          "price":9.99,
          "currency":"EUR",
          "categoryId":"CAT-777"
        }
        """;

        mvc.perform(put("/api/v1/products/{id}", id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void delete_204() throws Exception {
        var id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/products/{id}", id)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(service).delete(id);
    }
}
