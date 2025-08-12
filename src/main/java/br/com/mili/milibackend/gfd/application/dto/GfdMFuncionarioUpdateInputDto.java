package br.com.mili.milibackend.gfd.application.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class GfdMFuncionarioUpdateInputDto {
    private Integer codUsuario;
    private GfdFuncionarioDto funcionario;

    @Getter
    @AllArgsConstructor
    @Setter
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class GfdFuncionarioDto {
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
}
