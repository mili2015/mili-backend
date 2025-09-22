package br.com.mili.milibackend.gfd.application.usecases.GfdTipoDocumento;

import br.com.mili.milibackend.gfd.application.dto.gfdTipoDocumento.GfdTipoDocumentoGetAllInputDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GfdTipoDocumentoWithRescisaoGetAllInputDto {
    private Integer id;

    private String nome;

    private Integer diasValidade;

    private Boolean obrigatoriedade;

    private Boolean ativo;

    private GfdTipoDocumentoGetAllInputDto.GfdCategoriaDocumentoDto categoriaDocumento;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GfdCategoriaDocumentoDto {
        private Integer id;
    }
}
