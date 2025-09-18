package br.com.mili.milibackend.gfd.application.dto.manager.funcionario;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class GfdMFuncionarioUpdateOutputDto {
    public GfdFuncionarioDto funcionario;

    @Getter
    @AllArgsConstructor
    @Setter
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class GfdFuncionarioDto {
        private Integer id;
        private String nome;
        private String cpf;
        private LocalDate dataNascimento;
        private String paisNacionalidade;
        private String funcao;
        private LocalDate periodoInicial;
        private LocalDate periodoFinal;
        private String observacao;
        private Integer ativo;
        private Integer liberado;
        private FornecedorDto fornecedor;

        private GfdTipoContratacaoDto tipoContratacao;

        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @Setter
        public static class GfdTipoContratacaoDto {
            private Integer id;
        }

        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @Setter
        public static class FornecedorDto {
            private Integer codigo;
        }
    }
}
