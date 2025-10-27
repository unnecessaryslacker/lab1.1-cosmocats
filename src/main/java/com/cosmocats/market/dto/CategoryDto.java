package com.cosmocats.market.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryDto(
    @NotBlank @Size(min = 2, max = 40)
    String id,
    @NotBlank @Size(min = 2, max = 60)
    String name
) {}
