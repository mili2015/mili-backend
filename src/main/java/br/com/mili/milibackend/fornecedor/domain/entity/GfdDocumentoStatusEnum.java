package br.com.mili.milibackend.fornecedor.domain.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum GfdDocumentoStatusEnum {
    ENVIADO("ENVIADO"),
    EM_ANALISE("EM ANALISE"),
    NAO_CONFORME("NAO CONFORME"),
    CONFORME("CONFORME"),
    EXPIRADO("EXPIRADO");

    private String descricao;

    GfdDocumentoStatusEnum(String descricao) {
        this.descricao = descricao;
    }

    public static GfdDocumentoStatusEnum fromDescricao(String descricao) {
        return Arrays.stream(values())
                .filter(e -> e.descricao.equalsIgnoreCase(descricao))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Status desconhecido: " + descricao));
    }

    @JsonValue
    public String toJson() {
        return descricao;
    }
}
