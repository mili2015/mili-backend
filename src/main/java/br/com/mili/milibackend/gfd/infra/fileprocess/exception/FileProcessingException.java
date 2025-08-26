package br.com.mili.milibackend.gfd.infra.fileprocess.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
@Getter
public enum FileProcessingException {
    FILE_PROCESSING_TYPE_NOT_PERMITED("FILE_PROCESSING_TYPE_NOT_PERMITED", "Tipo de arquivo não permitido. Arquivos permitidos: ");

    private final String code;
    private final String mensagem;

}

