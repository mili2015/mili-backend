package br.com.mili.milibackend.gfd.adapter.exception;

import lombok.Getter;

@Getter
public enum GfdFuncionarioCodeException {

    GFD_FUNCIONARIO_NAO_ENCONTRADO("GFD_FUNCIONARIO_NAO_ENCONTRADO", "O funcionário não foi encontrado"),
    GFD_FUNCIONARIO_LIBERACAO_COM_DOCUMENTO_PENDENTE("GFD_FUNCIONARIO_LIBERACAO_COM_DOCUMENTO_PENDENTE", "Existes documentos pendentes, caso deseje liberar o funcionário, informe a justificativa");

    private final String code;
    private final String mensagem;

    GfdFuncionarioCodeException(String code, String mensagem) {
        this.code = code;
        this.mensagem = mensagem;
    }

}