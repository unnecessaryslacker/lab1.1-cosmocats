package com.cosmocats.market.domain;

import java.math.BigDecimal;
import java.util.UUID;

public record Product(
    UUID id,
    String name,
    String description,
    BigDecimal price,
    String currency,
    String categoryId
) {}
