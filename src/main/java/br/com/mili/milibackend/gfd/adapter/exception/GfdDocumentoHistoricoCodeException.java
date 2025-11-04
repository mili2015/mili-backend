package br.com.mili.milibackend.gfd.adapter.exception;

import lombok.Getter;

@Getter
public enum GfdDocumentoHistoricoCodeException {

    GFD_FORNECEDOR_NAO_ENCONTRADO("GFD_DOC_HISTORICO_FORNECEDOR_NAO_ENCONTRADO", "O Fornecedor não foi encontrado."),
    GFD_FORNECEDOR_NULL("GFD_DOC_HISTORICO_FORNECEDOR_NULL", "É necessário informar o fornecedor");

    private final String code;
    private final String mensagem;

    GfdDocumentoHistoricoCodeException(String code, String mensagem) {
        this.code = code;
        this.mensagem = mensagem;
    }

}