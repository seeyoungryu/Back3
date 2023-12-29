package com.example.withdogandcat.domain.Image.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileExtensionValidator.class)
public @interface FileExtension {
    String message() default "파일 확장자가 유효하지 않습니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] ext();
}
