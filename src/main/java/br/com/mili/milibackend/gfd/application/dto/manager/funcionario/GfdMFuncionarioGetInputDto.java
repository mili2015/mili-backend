package br.com.mili.milibackend.gfd.application.dto.manager.funcionario;

import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioGetAllOutputDto;
import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class GfdMFuncionarioGetInputDto {
    private Integer codUsuario;
    private GfdFuncionarioDto funcionario;

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
