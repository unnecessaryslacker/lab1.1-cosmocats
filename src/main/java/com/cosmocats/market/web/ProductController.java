package com.cosmocats.market.web;

import com.cosmocats.market.domain.Product;
import com.cosmocats.market.dto.ProductDto;
import com.cosmocats.market.mapper.ProductMapper;
import com.cosmocats.market.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

  private final ProductService service;
  private final ProductMapper mapper;

  public ProductController(ProductService service, ProductMapper mapper) {
    this.service = service;
    this.mapper = mapper;
  }

  @GetMapping
  public List<ProductDto> list() {
    return service.list().stream()
            .map(mapper::toDto)
            .toList();
  }

  @GetMapping("/{id}")
  public ProductDto get(@PathVariable("id") UUID id) {
    return mapper.toDto(service.get(id));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ProductDto create(@Valid @RequestBody ProductDto dto) {
    Product domain = mapper.toDomain(dto);
    Product saved = service.create(domain);
    return mapper.toDto(saved);
  }

  @PutMapping("/{id}")
  public ProductDto update(@PathVariable("id") UUID id,
                           @Valid @RequestBody ProductDto dto) {
    Product domain = mapper.toDomain(dto);
    Product updated = service.update(id, domain);
    return mapper.toDto(updated);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable("id") UUID id) {
    service.delete(id);
  }
}
