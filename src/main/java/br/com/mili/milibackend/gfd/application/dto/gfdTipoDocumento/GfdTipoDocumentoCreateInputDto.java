package br.com.mili.milibackend.gfd.application.dto.gfdTipoDocumento;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GfdTipoDocumentoCreateInputDto {

    @NotBlank
    private String nome;

    private Integer diasValidade;

    @NotNull
    private Boolean obrigatoriedade;

    @NotBlank
    private String classificacao;

    @NotBlank
    private String setor;

    @NotNull
    @Valid
    private GfdCategoriaDocumentoDto categoriaDocumento;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class GfdCategoriaDocumentoDto {
        @NotNull
        private Integer id;
    }

}
