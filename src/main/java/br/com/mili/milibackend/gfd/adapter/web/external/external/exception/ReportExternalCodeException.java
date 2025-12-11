package br.com.mili.milibackend.gfd.adapter.web.external.external.exception;

import lombok.Getter;

@Getter
public enum ReportExternalCodeException {

    REPORT_EXTERNAL_ERROR("REPORT_EXTERNAL_ERROR", "Houve uma falha ao consumir o serviço externo ");

    private final String code;
    private final String mensagem;

    ReportExternalCodeException(String code, String mensagem) {
        this.code = code;
        this.mensagem = mensagem;
    }

}