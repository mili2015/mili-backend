package br.com.mili.milibackend.envioEmail.shared;

public enum RemetenteEnum {
    MILI("naoresponder@mili.com.br");

    private final String endereco;

    RemetenteEnum(String endereco) {
        this.endereco = endereco;
    }

    public String getEndereco() {
        return endereco;
    }
}