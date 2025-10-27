package com.cosmocats.market.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Set;

public class CosmicWordCheckValidator implements ConstraintValidator<CosmicWordCheck, String> {
  private static final Set<String> TERMS = Set.of("star","galaxy","comet","nebula","asteroid");
  @Override public boolean isValid(String value, ConstraintValidatorContext ctx) {
    if (value == null) return true; // handled by @NotBlank if required
    String lower = value.toLowerCase();
    return TERMS.stream().anyMatch(lower::contains);
  }
}
