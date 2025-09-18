package br.com.mili.milibackend.gfd.shared;

import lombok.Getter;

@Getter
public enum TiposGfdCategoriaDocumentoEnum {
    FORNECEDOR("FORNECEDOR"),
    FUNCIONARIO("FUNCIONARIO");

    private String descricao;

    TiposGfdCategoriaDocumentoEnum(String descricao) {
        this.descricao = descricao;
    }
}
