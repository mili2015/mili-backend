package br.com.mili.milibackend.gfd.adapter.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GfdCodeException {
    GFD_DOCUMENTO_NAO_ENCONTRADO("GFD_DOCUMENTO_NAO_ENCONTRADO", "O documento não foi encontrado"),
    GFD_FORNECEDOR_NAO_ENCONTRADO("GFD_FORNECEDOR_NAO_ENCONTRADO", "Fornecedor não encontrado");

    private final String code;
    private final String mensagem;


}
