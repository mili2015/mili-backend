package br.com.mili.milibackend.gfd.application.usecases.GfdTipoDocumento;

import br.com.mili.milibackend.gfd.application.dto.gfdTipoDocumento.GfdTipoDocumentoGetAllOutputDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GfdTipoDocumentoWithRescisaoGetAllOutputDto {
    private List<GfdTipoDocumentoDto> gfdTipoDocumento;
    private List<TiposRescisaoDto> tiposRescisao;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class GfdTipoDocumentoDto {

        private Integer id;

        private String nome;

        private Integer diasValidade;

        private Boolean obrigatoriedade;

        private Boolean ativo;

        private String setor;

        private String classificacao;

        private GfdTipoDocumentoGetAllOutputDto.GfdCategoriaDocumentoDto categoriaDocumento;

        @NoArgsConstructor
        @AllArgsConstructor
        @Getter
        @Setter
        public static class GfdCategoriaDocumentoDto {
            private Integer id;
            private String nome;
        }

    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class TiposRescisaoDto {
        private String nome;
        private Boolean possuiRescisao;
    }
}
