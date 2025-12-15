package br.com.mili.milibackend.gfd.application.dto.gfdTipoDocumento;

import jakarta.validation.Valid;
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

    private Integer diasValidade;
    @NotNull
    private Boolean obrigatoriedade;

    private String classificacao;

    private String setor;

    @NotNull
    private Boolean automatizarBloqueio;

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
