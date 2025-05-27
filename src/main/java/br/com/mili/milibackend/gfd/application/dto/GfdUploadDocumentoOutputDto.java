package br.com.mili.milibackend.gfd.application.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GfdUploadDocumentoOutputDto {
    private List<GfdTipoDocumentoDto> GfdTipoDocumento;


    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class GfdTipoDocumentoDto {
        private Integer id;

        private String nome;

        private String tipo;

        private Integer diasValidade;

        private Boolean obrigatoriedade;

        private Boolean ativo;
    }
}
