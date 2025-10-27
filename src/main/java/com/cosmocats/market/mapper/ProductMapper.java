package com.cosmocats.market.mapper;

import com.cosmocats.market.domain.Product;
import com.cosmocats.market.dto.ProductDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {
  Product toDomain(ProductDto dto);
  ProductDto toDto(Product product);
}
