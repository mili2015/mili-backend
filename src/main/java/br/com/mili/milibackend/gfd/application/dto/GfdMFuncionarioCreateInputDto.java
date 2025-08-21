package br.com.mili.milibackend.gfd.application.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class GfdMFuncionarioCreateInputDto {
    private Integer codUsuario;
    private GfdFuncionarioDto funcionario;
    private GfdDocumentoPeriodoDto gfdDocumentoPeriodo;

    @Getter
    @AllArgsConstructor
    @Setter
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class GfdFuncionarioDto {
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
        private Integer ativo;

        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @Setter
        public static class FornecedorDto {
            private Integer codigo;
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class GfdDocumentoPeriodoDto {
        private LocalDate periodo;
    }
}
