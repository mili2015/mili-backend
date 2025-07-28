package br.com.mili.milibackend.gfd.infra.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GfdFuncionarioResumoDto {
    private Integer id;
    private String nome;
    private FornecedorResumoDto fornecedor;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class FornecedorResumoDto {
        private Integer codigo;
        private String cgcCpf;
    }

}
