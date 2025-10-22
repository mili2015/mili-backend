package br.com.mili.milibackend.gfd.application.dto.gfdTipoDocumento;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class GfdTipoDocumentoGetAllInputDto {
    private Integer id;

    private String nome;

    private Integer diasValidade;

    private Boolean obrigatoriedade;

    private Boolean ativo;

    private GfdCategoriaDocumentoDto categoriaDocumento;

    private String classificacao;

    private String setor;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GfdCategoriaDocumentoDto {
        private Integer id;
    }
}
