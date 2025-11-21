package br.com.mili.milibackend.academia.adapter.external.exceptions;

import lombok.Getter;

@Getter
public enum AcademiaExternalCodeException {

    ACADEMIA_EXTERNAL_ERROR("ACADEMIA_EXTERNAL_ERROR", "Houve uma falha ao consumir o serviço externo ");

    private final String code;
    private final String mensagem;

    AcademiaExternalCodeException(String code, String mensagem) {
        this.code = code;
        this.mensagem = mensagem;
    }

}