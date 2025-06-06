package br.com.mili.milibackend.shared.validation.annotation;


import br.com.mili.milibackend.shared.validation.IntervaloDataValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Repeatable(ValidaIntervaloData.List.class)
@Constraint(validatedBy = IntervaloDataValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidaIntervaloData {
    String message() default "A data inicial não pode ser posterior à data final.";
    String inicio();
    String fim();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        ValidaIntervaloData[] value();
    }
}
