package com.example.common.annotation;

import com.example.common.validator.XssValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = { ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = XssValidator.class)
public @interface Xss {
    String message() default "非法输入, 检测到潜在的XSS";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}