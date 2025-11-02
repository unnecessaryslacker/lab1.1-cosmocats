package com.cosmocats.market.service;

import com.cosmocats.market.domain.Product;
import com.cosmocats.market.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private ProductRepository repo;
    private ProductService service;

    @BeforeEach
    void setup() {
        repo = mock(ProductRepository.class);
        service = new ProductService(repo);
    }

    private UUID id() {
        return UUID.randomUUID();
    }

    private Product p(UUID id) {
        return new Product(
                id,
                "Mars",
                "Bar",
                new BigDecimal("4.20"),
                "USD",
                "CAT-001"
        );
    }

    @Test
    void list_returns_all() {
        when(repo.findAll()).thenReturn(List.of(p(id())));
        assertThat(service.list()).hasSize(1);
        verify(repo).findAll();
    }

    @Test
    void get_found() {
        var pid = id();
        when(repo.findById(pid)).thenReturn(Optional.of(p(pid)));
        var result = service.get(pid);
        assertThat(result.id()).isEqualTo(pid);
    }

    @Test
    void get_not_found_throws() {
        var pid = id();
        when(repo.findById(pid)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.get(pid))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void create_delegates_save() {
        var in = p(null);
        when(repo.save(in)).thenReturn(p(id()));
        var saved = service.create(in);
        assertThat(saved.id()).isNotNull();
        verify(repo).save(in);
    }

    @Test
    void update_existing_keeps_id_and_applies_fields() {
        var pid = id();
        when(repo.findById(pid)).thenReturn(Optional.of(p(pid)));
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var incoming = new Product(
                null,
                "NewName",
                "NewDesc",
                new BigDecimal("9.99"),
                "EUR",
                "CAT-777"
        );

        var updated = service.update(pid, incoming);

        assertThat(updated.id()).isEqualTo(pid);
        assertThat(updated.name()).isEqualTo("NewName");
        assertThat(updated.price()).isEqualByComparingTo("9.99");
        verify(repo).save(updated);
    }

    @Test
    void update_missing_throws() {
        var pid = id();
        when(repo.findById(pid)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.update(pid, p(null)))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void delete_calls_repo() {
        var pid = id();
        service.delete(pid);
        verify(repo).deleteById(pid);
    }
}
