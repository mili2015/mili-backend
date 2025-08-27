package br.com.mili.milibackend.gfd.domain.entity;

import lombok.Getter;

@Getter
public enum GfdFuncionarioTipoContratacaoEnum {
    SUB_CONTRATADOS("SUB_CONTRATADOS"),
    CLT("CLT"),
    CLT_SEGURANCA("CLT_SEGURANCA");

    private String descricao;

    GfdFuncionarioTipoContratacaoEnum(String descricao) {
        this.descricao = descricao;
    }
}
