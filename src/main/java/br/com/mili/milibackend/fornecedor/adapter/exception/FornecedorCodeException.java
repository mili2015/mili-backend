package br.com.mili.milibackend.fornecedor.adapter.exception;

import lombok.Getter;

@Getter
public enum FornecedorCodeException {

    FORNECEDOR_NAO_ENCONTRADO("FORNECEDOR_NAO_ENCONTRADO", "O fornecedor não foi encontrado");

    private final String code;
    private final String mensagem;

    FornecedorCodeException(String code, String mensagem) {
        this.code = code;
        this.mensagem = mensagem;
    }

}