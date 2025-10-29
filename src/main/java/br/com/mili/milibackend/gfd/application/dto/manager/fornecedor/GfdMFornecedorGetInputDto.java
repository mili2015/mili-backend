package br.com.mili.milibackend.gfd.application.dto.manager.fornecedor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GfdMFornecedorGetInputDto {
    private Integer codUsuario;
    private Integer id;
    private boolean isAnalista;
}
