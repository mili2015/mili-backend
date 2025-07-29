package br.com.mili.milibackend.gfd.application.dto.gfdTipoDocumento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GfdTipoDocumentoCreateOutputDto {

        private Integer id;

        private String nome;

        private String tipo;

        private Integer diasValidade;

        private Boolean obrigatoriedade;

        private Boolean ativo;
}
