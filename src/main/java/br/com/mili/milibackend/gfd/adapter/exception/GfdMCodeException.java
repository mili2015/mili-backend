package br.com.mili.milibackend.gfd.adapter.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GfdMCodeException {
    GFD_DOCUMENTO_NAO_ENCONTRADO("GFD_DOCUMENTO_NAO_ENCONTRADO", "O documento não foi encontrado"),
    GFD_FORNECEDOR_NAO_ENCONTRADO("GFD_FORNECEDOR_NAO_ENCONTRADO", "Fornecedor não encontrado"),
    GFD_LEI_LGPD_NAO_ACEITA("GFD_LEI_LGPD_NAO_ACEITA", "Identificamos que você ainda não aceitou os termos da LGPD. Por favor, acesse Meus Dados e aceite os termos para continuar."),
    GFD_TIPO_DOCUMENTO_FUNCIONARIO_BAD_REQUEST("GFD_TIPO_DOCUMENTO_FUNCIONARIO_BAD_REQUEST", "Tipo de documento não pode ser de fornecedor, quando enviar o funcionário id"),
    GFD_TIPO_DOCUMENTO_NAO_ENCONTRADO("GFD_TIPO_DOCUMENTO__NAO_ENCONTRADO", "Tipo de documento não encontrado"),
    GFD_CATEGORIA_DOCUMENTO_NAO_ENCONTRADO("GFD_CATEGORIA_DOCUMENTO_NAO_ENCONTRADO", "Categoria de documento não encontrado"),
    GFD_TIPO_DOCUMENTO_REGISTROS_ENCONTRADOS_VALIDADE("GFD_TIPO_DOCUMENTO_REGISTROS_ENCONTRADOS_VALIDADE", "Existes registros que usa esse tipo de documento, impossibilitando a alteração da validade"),
    GFD_FUNCIONARIO_SEM_PERMISSAO("GFD_FUNCIONARIO_SEM_PERMISSAO", "Você não tem permissão para realizar essa operação"),
    GFD_TIPO_FORNECEDOR_NAO_ENCONTRADO("GFD_TIPO_FORNECEDOR_NAO_ENCONTRADO", "O tipo não foi encontrado"),
    GFD_PERIODO_VAZIO("GFD_PERIODO_VAZIO", "O campo GfdDocumentoPeriodo deve estar preenchido");

    private final String code;
    private final String mensagem;


}
