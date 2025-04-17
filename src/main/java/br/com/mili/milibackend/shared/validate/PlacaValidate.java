package br.com.mili.milibackend.shared.validate;

import java.util.regex.Pattern;

public class PlacaValidate implements IValidate {
    private Pattern patternMercosul = Pattern.compile("[a-zA-Z]{3}-[0-9]{1}[a-zA-Z]{1}[0-9]{2}");
    private Pattern patternBrasil = Pattern.compile("[a-zA-Z]{3}-[0-9]{4}");

    private PlacaValidate() {
    }

    public static IValidate getInstance(){
        return new PlacaValidate();
    }

    @Override
    public boolean isValido(String valor) {
        if (valor == null)
            return false;
        return isValidoPadraoMercosul(valor) || isValidoPadraoBrasil(valor);
    }

    private boolean isValidoPadraoMercosul(String valor) {
        return patternMercosul.matcher(valor).matches();
    }

    private boolean isValidoPadraoBrasil(String valor) {
        return patternBrasil.matcher(valor).matches();
    }
}
