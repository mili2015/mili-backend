package br.com.mili.milibackend.fornecedor.application.dto.gfdTipoDocumento;

import br.com.mili.milibackend.fornecedor.domain.entity.GfdTipoDocumentoTipoEnum;
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

    private GfdTipoDocumentoTipoEnum tipo;

    private Integer diasValidade;

    private Boolean obrigatoriedade;

    private Boolean ativo;
}
