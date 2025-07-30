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
public class GfdMUploadDocumentoOutputDto {
    private Integer id;

    private String nome;

    private String tipo;

    private Integer diasValidade;

    private Boolean obrigatoriedade;

    private Boolean ativo;
}
