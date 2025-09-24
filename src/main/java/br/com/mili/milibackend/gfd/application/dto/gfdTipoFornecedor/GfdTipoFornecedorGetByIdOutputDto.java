package br.com.mili.milibackend.gfd.application.dto.gfdTipoFornecedor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GfdTipoFornecedorGetByIdOutputDto {
    private Integer id;
    private String descricao;
}
