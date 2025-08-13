package br.com.mili.milibackend.gfd.application.dto.gfdFuncionario;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GfdFuncionarioUpdateObservacaoOutputDto {
    private Integer id;
    private FornecedorDto fornecedor;
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private String paisNacionalidade;
    private String funcao;
    private String tipoContratacao;
    private LocalDate periodoInicial;
    private LocalDate periodoFinal;
    private String observacao;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class FornecedorDto {
        private Integer codigo;
    }
}
