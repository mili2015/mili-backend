package br.com.mili.milibackend.gfd.application.dto.gfdCategoriaDocumento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GfdCategoriaDocumentoGetAllInputDto {
    private Integer id;

    private String nome;

    private String tipo;
}
