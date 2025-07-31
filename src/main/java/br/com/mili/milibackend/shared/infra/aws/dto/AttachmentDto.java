package br.com.mili.milibackend.shared.infra.aws.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AttachmentDto(
        @Size(max = 15 * 1024 * 1024, message = "Arquivo excede o tamanho máximo permitido de 15 MB.")
        @NotNull(message = "Arquivo é obrigatório")
        String file,

        @NotNull(message = "Nome do arquivo é obrigatório")
        String fileName
) {

    @JsonIgnore
    public String getExtension() {
        if (fileName == null)
            return "jpeg";

        String[] fileNames = fileName.split("\\.");

        if (fileNames.length == 1)
            return "jpeg";

        return fileNames[fileNames.length - 1];
    }
}

