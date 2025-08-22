package br.com.mili.milibackend.gfd.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class GfdMFuncionarioUpdateInputDto {
    private Integer codUsuario;

    @Valid
    private GfdFuncionarioDto funcionario;

    @Getter
    @AllArgsConstructor
    @Setter
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class GfdFuncionarioDto {
        private Integer id;
        private FornecedorDto fornecedor;

        @NotEmpty
        private String nome;

        @NotEmpty
        private String cpf;

        @NotNull
        private LocalDate dataNascimento;

        @NotEmpty
        private String paisNacionalidade;

        @NotEmpty
        private String funcao;

        @NotEmpty
        private String tipoContratacao;

        @NotNull
        private LocalDate periodoInicial;

        @NotNull
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
