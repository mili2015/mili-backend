package br.com.mili.milibackend.gfd.application.dto.gfdCategoriaDocumento;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GfdCategoriaDocumentoCreateOutputDto {
    private Integer id;

    private String nome;

    private String tipo;

    private String nomeContratacao;
}
