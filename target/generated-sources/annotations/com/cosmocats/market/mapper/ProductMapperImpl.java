package com.cosmocats.market.mapper;

import com.cosmocats.market.domain.Product;
import com.cosmocats.market.dto.ProductDto;
import java.math.BigDecimal;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-02T19:54:32+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public Product toDomain(ProductDto dto) {
        if ( dto == null ) {
            return null;
        }

        UUID id = null;
        String name = null;
        String description = null;
        BigDecimal price = null;
        String currency = null;
        String categoryId = null;

        id = dto.id();
        name = dto.name();
        description = dto.description();
        price = dto.price();
        currency = dto.currency();
        categoryId = dto.categoryId();

        Product product = new Product( id, name, description, price, currency, categoryId );

        return product;
    }

    @Override
    public ProductDto toDto(Product product) {
        if ( product == null ) {
            return null;
        }

        UUID id = null;
        String name = null;
        String description = null;
        BigDecimal price = null;
        String currency = null;
        String categoryId = null;

        id = product.id();
        name = product.name();
        description = product.description();
        price = product.price();
        currency = product.currency();
        categoryId = product.categoryId();

        ProductDto productDto = new ProductDto( id, name, description, price, currency, categoryId );

        return productDto;
    }
}
