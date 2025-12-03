package br.com.mili.milibackend.shared.validation.validaDataAntigasOuFuturas;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DataAntigaOuFuturaValidator.class)
public @interface ValidaDataAntigasOuFuturas {
    String message() default "Data fora do intervalo permitido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /** quantos anos para trás pode aceitar */
    int pastYears() default 100;

    /** quantos anos para frente pode aceitar */
    int futureYears() default 100;
}
