package br.com.mili.milibackend.gfd.application.dto.manager.funcionario;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class GfdMFuncionarioGetOutputDto {
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
        private String tipoContratacao;
        private LocalDate periodoInicial;
        private LocalDate periodoFinal;
        private String observacao;
        private Integer ativo;
        private Integer liberado;
        private FornecedorDto fornecedor;
        private List<LocalTrabalhoDto> locaisTrabalho = new ArrayList<>();

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


        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @Setter
        public static class LocalTrabalhoDto {
            private Integer ctempCodigo;
        }
    }
}
