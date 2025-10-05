package com.cosmocats.market.repository;

import com.cosmocats.market.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

@Repository
public class ProductRepository {
  private final Map<UUID, Product> db = new ConcurrentHashMap<>();

  public List<Product> findAll() { return new ArrayList<>(db.values()); }
  public Optional<Product> findById(UUID id) { return Optional.ofNullable(db.get(id)); }
  public Product save(Product p) {
    UUID id = p.id() != null ? p.id() : UUID.randomUUID();
    Product withId = new Product(id, p.name(), p.description(), p.price(), p.currency(), p.categoryId());
    db.put(id, withId);
    return withId;
  }
  public boolean deleteById(UUID id) { return db.remove(id) != null; }
}
