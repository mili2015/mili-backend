package br.com.mili.milibackend.gfd.adapter.exception;

import lombok.Getter;

@Getter
public enum GfdFuncionarioCodeException {

    GFD_FUNCIONARIO_NAO_ENCONTRADO("GFD_FUNCIONARIO_NAO_ENCONTRADO", "O funcionário não foi encontrado");

    private final String code;
    private final String mensagem;

    GfdFuncionarioCodeException(String code, String mensagem) {
        this.code = code;
        this.mensagem = mensagem;
    }

}