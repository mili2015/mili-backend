package br.com.mili.milibackend.fornecedor.application.validation;

import br.com.mili.milibackend.shared.annotation.ValidaIntervaloData;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.time.LocalDate;

public class IntervaloDataValidator implements ConstraintValidator<ValidaIntervaloData, Object> {
    private String inicioFieldName;
    private String fimFieldName;
    private String message;

    @Override
    public void initialize(ValidaIntervaloData constraintAnnotation) {
        this.inicioFieldName = constraintAnnotation.inicio();
        this.fimFieldName = constraintAnnotation.fim();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        try {
            Field inicioField = obj.getClass().getDeclaredField(inicioFieldName);
            Field fimField = obj.getClass().getDeclaredField(fimFieldName);
            inicioField.setAccessible(true);
            fimField.setAccessible(true);

            Object dataInicio = inicioField.get(obj);
            Object dataFim = fimField.get(obj);

            if (dataInicio == null || dataFim == null) return true;

            if (dataInicio instanceof LocalDate && dataFim instanceof LocalDate) {
                var dataInicioMaior = ((LocalDate) dataInicio).isAfter((LocalDate) dataFim);

                if (dataInicioMaior) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(message)
                            .addPropertyNode(inicioFieldName)
                            .addConstraintViolation();

                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            return true;
        }
    }
}
