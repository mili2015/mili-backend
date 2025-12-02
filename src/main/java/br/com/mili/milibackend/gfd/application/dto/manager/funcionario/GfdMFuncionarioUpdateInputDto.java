package br.com.mili.milibackend.gfd.application.dto.manager.funcionario;

import br.com.mili.milibackend.shared.validation.validaDataAntigasOuFuturas.ValidaDataAntigasOuFuturas;
import br.com.mili.milibackend.shared.validation.validaIntervaloData.annotation.ValidaIntervaloData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class GfdMFuncionarioUpdateInputDto {
    private Integer codUsuario;
    private boolean isAnalista;

    @Valid
    private GfdFuncionarioDto funcionario;

    @Getter
    @AllArgsConstructor
    @Setter
    @EqualsAndHashCode
    @NoArgsConstructor
    @ValidaIntervaloData(inicio = "periodoInicial", fim = "periodoFinal")
    public static class GfdFuncionarioDto {
        private Integer id;
        private FornecedorDto fornecedor;

        @NotEmpty
        private String nome;

        private String email;

        @NotEmpty
        private String cpf;

        @NotNull
        private LocalDate dataNascimento;

        @NotEmpty
        private String paisNacionalidade;

        @NotEmpty
        private String funcao;


        @NotNull
        @ValidaDataAntigasOuFuturas
        private LocalDate periodoInicial;

        @NotNull
        @ValidaDataAntigasOuFuturas
        private LocalDate periodoFinal;

        private String observacao;

        private List<@NotNull LocalTrabalhoDto> locaisTrabalho;

        @NotNull
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
        public static class LocalTrabalhoDto {
            private Integer ctempCodigo;
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
