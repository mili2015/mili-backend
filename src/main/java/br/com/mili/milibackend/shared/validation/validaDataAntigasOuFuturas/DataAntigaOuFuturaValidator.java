package br.com.mili.milibackend.shared.validation.validaDataAntigasOuFuturas;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DataAntigaOuFuturaValidator implements ConstraintValidator<ValidaDataAntigasOuFuturas, Object> {
    private int pastYears;
    private int futureYears;

    @Override
    public void initialize(ValidaDataAntigasOuFuturas constraint) {
        this.pastYears = constraint.pastYears();
        this.futureYears = constraint.futureYears();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) return true;

        LocalDateTime dateTime = normalize(value);
        if (dateTime == null) return false;

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime min = now.minusYears(pastYears);
        LocalDateTime max = now.plusYears(futureYears);

        return !dateTime.isBefore(min) && !dateTime.isAfter(max);
    }

    private LocalDateTime normalize(Object value) {
        if (value instanceof LocalDateTime ldt) return ldt;
        if (value instanceof LocalDate ld) return ld.atStartOfDay();
        return null;
    }
}
