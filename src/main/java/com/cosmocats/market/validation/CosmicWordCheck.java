package com.cosmocats.market.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CosmicWordCheckValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CosmicWordCheck {
  String message() default "name must contain a cosmic term (star, galaxy, comet, nebula, asteroid)";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
