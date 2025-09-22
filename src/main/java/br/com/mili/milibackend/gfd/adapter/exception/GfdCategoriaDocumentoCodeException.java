package br.com.mili.milibackend.gfd.adapter.exception;

import lombok.Getter;

@Getter
public enum GfdCategoriaDocumentoCodeException {

    GFD_CATEGORIA_DOCUMENTO_NOME_DA_CONTRATACAO("GFD_CATEGORIA_DOCUMENTO_NOME_DA_CONTRATACAO", "Para categoria com o tipo como fornecedor, o campo tipo de contratação é obrigatório.");

    private final String code;
    private final String mensagem;

    GfdCategoriaDocumentoCodeException(String code, String mensagem) {
        this.code = code;
        this.mensagem = mensagem;
    }

}