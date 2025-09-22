package br.com.mili.milibackend.gfd.domain.entity;

import lombok.Getter;

@Getter
public enum GfdFuncionarioTipoContratacaoEnum {
    SUBCONTRATADOS("SUBCONTRATADOS"),
    CLT("CLT"),
    CLT_SEGURANCA("CLT_SEGURANCA"),
    PROPRIETARIO("PROPRIETARIO");

    private String descricao;

    GfdFuncionarioTipoContratacaoEnum(String descricao) {
        this.descricao = descricao;
    }
}
