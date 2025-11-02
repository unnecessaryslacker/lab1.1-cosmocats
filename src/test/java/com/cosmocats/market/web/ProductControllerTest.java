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
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@Import(ErrorHandler.class)
class ProductControllerTest {

    @Autowired MockMvc mvc;
    @MockBean ProductService service;
    @MockBean ProductMapper mapper;

    private Product domain(UUID id) {
        return new Product(id, "Galaxy Snack", "Bar", new BigDecimal("4.20"), "USD", "CAT-001");
    }

    private ProductDto dto(UUID id) {
        return new ProductDto(id, "Galaxy Snack", "Bar", new BigDecimal("4.20"), "USD", "CAT-001");
    }

    @Test
    void list_200() throws Exception {
        var p = domain(UUID.randomUUID());
        when(service.list()).thenReturn(List.of(p));
        when(mapper.toDto(p)).thenReturn(dto(p.id()));

        mvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(p.id().toString()));
    }

    @Test
    void get_found_200() throws Exception {
        var id = UUID.randomUUID();
        var p = domain(id);
        when(service.get(id)).thenReturn(p);
        when(mapper.toDto(p)).thenReturn(dto(id));

        mvc.perform(get("/api/products/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void get_missing_404() throws Exception {
        var id = UUID.randomUUID();
        when(service.get(id)).thenThrow(new NoSuchElementException());

        mvc.perform(get("/api/products/{id}", id))
                .andExpect(status().isNotFound());
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

        mvc.perform(post("/api/products")
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

        mvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bad))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_200() throws Exception {
        var id = UUID.randomUUID();
        var updated = domain(id);
        when(mapper.toDomain(any(ProductDto.class))).thenReturn(domain(null));
        when(service.update(any(UUID.class), any(Product.class))).thenReturn(updated);
        when(mapper.toDto(updated)).thenReturn(dto(id));

        String body = """
        {
          "name":"Star Item",
          "description":"NewDesc",
          "price":9.99,
          "currency":"EUR",
          "categoryId":"CAT-777"
        }
        """;

        mvc.perform(put("/api/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void delete_204() throws Exception {
        var id = UUID.randomUUID();
        mvc.perform(delete("/api/products/{id}", id))
                .andExpect(status().isNoContent());
    }
}
