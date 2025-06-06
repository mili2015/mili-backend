package br.com.mili.milibackend.fornecedor.adapter.exception;

import lombok.Getter;

@Getter
public enum GfdDocumentoCodeException {

    GFD_DOCUMENTO_NAO_ENCONTRADO("GFD_DOCUMENTO_NAO_ENCONTRADO", "O documento não foi encontrado"),
    GFD_DOCUMENTO_DELETE_PERMISSAO_INVALIDA("GFD_DOCUMENTO_DELETE_PERMISSAO_INVALIDA", "Apenas é possível apagar documentos com status ENVIADO");

    private final String code;
    private final String mensagem;

    GfdDocumentoCodeException(String code, String mensagem) {
        this.code = code;
        this.mensagem = mensagem;
    }

}