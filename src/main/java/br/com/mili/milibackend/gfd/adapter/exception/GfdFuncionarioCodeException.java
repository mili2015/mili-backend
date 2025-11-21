package br.com.mili.milibackend.gfd.adapter.exception;

import lombok.Getter;

@Getter
public enum GfdFuncionarioCodeException {

    GFD_FUNCIONARIO_NAO_ENCONTRADO("GFD_FUNCIONARIO_NAO_ENCONTRADO", "O funcionário não foi encontrado."),
    GFD_FUNCIONARIO_SEM_EMAIL("GFD_FUNCIONARIO_SEM_EMAIL", "O funcionário não possuí email cadastrado"),
    GFD_FUNCIONARIO_JA_SENDO_USADO("GFD_FUNCIONARIO_JA_SENDO_USADO", "O email já esta sendo utilizado"),
    GFD_FUNCIONARIO_JA_DESLIGADO("GFD_FUNCIONARIO_JA_DESLIGADO", "O funcionário já esta desligado."),
    GFD_FORNECEDOR_NULL("GFD_FORNECEDOR_NULL", "É necessário informar o fornecedor"),
    GFD_FUNCIONARIO_LIBERACAO_COM_DOCUMENTO_PENDENTE("GFD_FUNCIONARIO_LIBERACAO_COM_DOCUMENTO_PENDENTE", "Existes documentos pendentes, caso deseje liberar o funcionário, informe a justificativa");

    private final String code;
    private final String mensagem;

    GfdFuncionarioCodeException(String code, String mensagem) {
        this.code = code;
        this.mensagem = mensagem;
    }

}