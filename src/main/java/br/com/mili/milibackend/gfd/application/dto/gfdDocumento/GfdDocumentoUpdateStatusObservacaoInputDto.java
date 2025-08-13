package br.com.mili.milibackend.gfd.application.dto.gfdDocumento;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GfdDocumentoUpdateStatusObservacaoInputDto {
    @NotNull
    private Integer id;

    @NotNull
    private String status;

    private String observacao;
}
