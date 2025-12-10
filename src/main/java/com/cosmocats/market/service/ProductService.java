package com.cosmocats.market.service;

import com.cosmocats.market.domain.Product;
import com.cosmocats.market.repository.ProductRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductService {

  private final ProductRepository repo;

  public ProductService(ProductRepository repo) {
    this.repo = repo;
  }

  @PreAuthorize("hasAnyAuthority('SCOPE_cosmo.read', 'ROLE_API_CLIENT')")
  public List<Product> list() {
    return repo.findAll();
  }

  @PreAuthorize("hasAnyAuthority('SCOPE_cosmo.read', 'ROLE_API_CLIENT')")
  public Product get(UUID id) {
    return repo.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Product %s not found".formatted(id)));
  }

  @PreAuthorize("hasAnyAuthority('SCOPE_cosmo.write', 'ROLE_API_CLIENT')")
  public Product create(Product p) {
    return repo.save(p);
  }

  @PreAuthorize("hasAnyAuthority('SCOPE_cosmo.write', 'ROLE_API_CLIENT')")
  public Product update(UUID id, Product p) {
    repo.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Product %s not found".formatted(id)));

    Product updated = new Product(
            id,
            p.name(),
            p.description(),
            p.price(),
            p.currency(),
            p.categoryId()
    );
    return repo.save(updated);
  }

  @PreAuthorize("hasAnyAuthority('SCOPE_cosmo.write', 'ROLE_API_CLIENT')")
  public void delete(UUID id) {
    repo.deleteById(id);
  }
}
