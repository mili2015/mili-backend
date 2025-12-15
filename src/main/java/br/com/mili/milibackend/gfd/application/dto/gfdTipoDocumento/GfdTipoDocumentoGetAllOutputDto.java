package br.com.mili.milibackend.gfd.application.dto.gfdTipoDocumento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class GfdTipoDocumentoGetAllOutputDto {
    private Integer id;

    private String nome;

    private Integer diasValidade;

    private Boolean obrigatoriedade;

    private Boolean ativo;

    private Boolean automatizarBloqueio;

    private String classificacao;

    private String setor;

    private GfdCategoriaDocumentoDto categoriaDocumento;


    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class GfdCategoriaDocumentoDto {
        private Integer id;
        private String nome;
    }
}
