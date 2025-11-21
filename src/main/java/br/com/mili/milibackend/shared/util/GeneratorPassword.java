package br.com.mili.milibackend.shared.util;

import java.security.SecureRandom;

public class GeneratorPassword {
    private static final String CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();

    public GeneratorPassword() {
    }

    public static String senhaAlphaNumerica(int size) {
        StringBuilder senha = new StringBuilder();

        for (int i = 0; i < size; i++) {
            int index = random.nextInt(CHARSET.length());
            senha.append(CHARSET.charAt(index));
        }

        return senha.toString();
    }
}
