package br.com.mili.milibackend.shared.infra.aws.exception;

import lombok.Getter;

@Getter
public enum S3ExceptionCode {
    S3_FALHA("S3_FALHA", "Houve uma falha ao consumir o serviço externo: ");

    S3ExceptionCode(String code, String mensagem) {
        this.code = code;
        this.mensagem = mensagem;
    }

    private final String code;
    private final String mensagem;
}
