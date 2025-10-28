package br.com.mili.milibackend.gfd.application.dto.gfdTipoDocumento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GfdTipoDocumentoUpdateOutputDto {

    private Integer id;

    private String nome;

    private Integer diasValidade;

    private Boolean obrigatoriedade;

    private Boolean ativo;

    private String setor;

    private String classificacao;

    private GfdCategoriaDocumentoDto categoriaDocumento;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class GfdCategoriaDocumentoDto {
        private Integer id;
    }

}
