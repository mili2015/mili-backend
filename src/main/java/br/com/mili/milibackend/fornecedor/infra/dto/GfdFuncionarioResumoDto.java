package br.com.mili.milibackend.fornecedor.infra.dto;

import br.com.mili.milibackend.fornecedor.domain.entity.Fornecedor;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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
