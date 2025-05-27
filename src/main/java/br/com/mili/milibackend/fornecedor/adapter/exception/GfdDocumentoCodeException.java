package br.com.mili.milibackend.fornecedor.adapter.exception;

import lombok.Getter;

@Getter
public enum GfdDocumentoCodeException {

    FORNECEDOR_DOCUMENTO_NAO_ENCONTRADO("FORNECEDOR_DOCUMENTO_NAO_ENCONTRADO", "O documento não foi encontrado");

    private final String code;
    private final String mensagem;

    GfdDocumentoCodeException(String code, String mensagem) {
        this.code = code;
        this.mensagem = mensagem;
    }

}