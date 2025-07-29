package br.com.mili.milibackend.gfd.application.dto.gfdTipoDocumento;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GfdTipoDocumentoUpdateInputDto {
    @NotNull
    private Integer id;
    @NotNull
    private String nome;
    @NotNull
    private String tipo;

    private Integer diasValidade;
    @NotNull
    private Boolean obrigatoriedade;
}
