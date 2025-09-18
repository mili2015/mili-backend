package br.com.mili.milibackend.gfd.application.dto.tipoContratacao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GfdTipoContratacaoGetAllOutputDto {
    private Integer id;
    private String descricao;
}