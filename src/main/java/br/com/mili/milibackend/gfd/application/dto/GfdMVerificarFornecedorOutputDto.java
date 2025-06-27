package br.com.mili.milibackend.gfd.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GfdMVerificarFornecedorOutputDto {
    private Boolean representanteCadastrado;
    private String razaoSocial;
}
