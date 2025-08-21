package br.com.mili.milibackend.gfd.application.dto.gfdTipoDocumento;

import br.com.mili.milibackend.gfd.domain.entity.GfdTipoDocumentoTipoEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class GfdTipoDocumentoGetByIdOutputDto {
    private Integer id;

    private String nome;

    private GfdTipoDocumentoTipoEnum tipo;

    private Integer diasValidade;

    private Boolean obrigatoriedade;

    private Boolean ativo;

    private String classificacao;
}
