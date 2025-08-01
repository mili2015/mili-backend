package br.com.mili.milibackend.gfd.application.dto;

import br.com.mili.milibackend.shared.page.pagination.MyPage;
import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class GfdMFuncionarioGetAllOutputDto {
    MyPage<GfdFuncionarioDto> funcionario;

    @Getter
    @AllArgsConstructor
    @Setter
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class GfdFuncionarioDto {
        private Integer id;
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
        private Integer liberado;
        private FornecedorDto fornecedor;

        private Integer totalEnviado;
        private Integer totalConforme;
        private Integer totalNaoConforme;
        private Integer totalEmAnalise;
        private Integer naoEnviado;

        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @Setter
        public static class FornecedorDto {
            private Integer codigo;
        }
    }

}
