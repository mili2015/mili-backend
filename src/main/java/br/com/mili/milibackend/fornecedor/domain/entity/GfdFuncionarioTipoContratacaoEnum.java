package br.com.mili.milibackend.fornecedor.domain.entity;

import lombok.Getter;

@Getter
public enum GfdFuncionarioTipoContratacaoEnum {
    PJ("PJ"),
    CLT("CLT");

    private String descricao;

    GfdFuncionarioTipoContratacaoEnum(String descricao) {
        this.descricao = descricao;
    }
}
