package com.cosmocats.market.dto;

import com.cosmocats.market.validation.CosmicWordCheck;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

public record ProductDto(
    UUID id,
    @NotBlank @Size(min = 3, max = 80) @CosmicWordCheck
    String name,
    @Size(max = 500)
    String description,
    @NotNull @DecimalMin(value = "0.01")
    BigDecimal price,
    @NotBlank @Pattern(regexp = "[A-Z]{3}")
    String currency,
    @NotBlank
    String categoryId
) {}
