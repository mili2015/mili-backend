package br.com.mili.milibackend.gfd.application.dto.gfdTipoFornecedor;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GfdTipoFornecedorGetByIdOutputDto {
    private Integer id;
    private String descricao;
}
