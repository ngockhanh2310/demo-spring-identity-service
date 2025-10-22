package com.khanh.demo.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = {DobValidator.class}
)
public @interface DobConstraint {
    String message() default "{dob.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int min() default 10;
}
