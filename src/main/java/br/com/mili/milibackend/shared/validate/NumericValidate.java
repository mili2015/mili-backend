package br.com.mili.milibackend.shared.validate;

import java.util.regex.Pattern;

public class NumericValidate implements IValidate{

    private Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    @Override
    public boolean isValido(String valor) {
        if (valor == null)
            return false;
        return pattern.matcher(valor).matches();
    }
}
