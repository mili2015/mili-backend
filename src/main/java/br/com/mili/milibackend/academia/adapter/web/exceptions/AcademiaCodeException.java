package br.com.mili.milibackend.academia.adapter.web.exceptions;

import lombok.Getter;

@Getter
public enum AcademiaCodeException {

    ACADEMIA_USUARIO_MATRICULADO("ACADEMIA_USUARIO_MATRICULADO", "O usuário ja possuí matrícula"),
    ACADEMIA_FALHA_AO_MATRICULAR("ACADEMIA_FALHA_AO_MATRICULAR", "Houve uma falha ao matricular o usuário: "),
    ACADEMIA_INTEGRACAO_INVALIDA("ACADEMIA_INTEGRACAO_INVALIDA", "A integração passada é inválida "),
    ACADEMIA_USUARIO_NAO_ENCONTRADO("ACADEMIA_USUARIO_NAO_ENCONTRADO", "Não foi possível encontrar o usuário"),
    ACADEMIA_EMAIL_INVALIDO("ACADEMIA_EMAIL_INVALIDO", "O email é inválido");

    private final String code;
    private final String mensagem;

    AcademiaCodeException(String code, String mensagem) {
        this.code = code;
        this.mensagem = mensagem;
    }

}