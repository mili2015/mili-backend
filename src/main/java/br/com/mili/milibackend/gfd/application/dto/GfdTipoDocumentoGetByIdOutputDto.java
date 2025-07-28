package br.com.mili.milibackend.gfd.application.dto;

import br.com.mili.milibackend.gfd.domain.entity.GfdTipoDocumentoTipoEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GfdTipoDocumentoGetByIdOutputDto {
    private Integer id;

    private String nome;

    private GfdTipoDocumentoTipoEnum tipo;

    private Integer diasValidade;

    private Boolean obrigatoriedade;

    private Boolean ativo;
}
