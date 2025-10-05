package com.cosmocats.market.service;

import com.cosmocats.market.domain.Product;
import com.cosmocats.market.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductService {
  private final ProductRepository repo;
  public ProductService(ProductRepository repo) { this.repo = repo; }

  public List<Product> list() { return repo.findAll(); }
  public Product get(UUID id) { return repo.findById(id).orElseThrow(() -> new NoSuchElementException("Product %s not found".formatted(id))); }
  public Product create(Product p) { return repo.save(p); }
  public Product update(UUID id, Product p) {
    get(id); // throws if absent
    return repo.save(new Product(id, p.name(), p.description(), p.price(), p.currency(), p.categoryId()));
  }
  public void delete(UUID id) { if (!repo.deleteById(id)) throw new NoSuchElementException("Product %s not found".formatted(id)); }
}
