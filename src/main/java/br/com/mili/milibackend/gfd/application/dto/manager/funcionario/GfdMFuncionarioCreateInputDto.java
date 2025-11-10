package br.com.mili.milibackend.gfd.application.dto.manager.funcionario;

import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioCreateInputDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class GfdMFuncionarioCreateInputDto {
    private Integer codUsuario;
    @Valid
    private GfdFuncionarioDto funcionario;
    private boolean isAnalista;

    @Valid
    private GfdDocumentoPeriodoDto gfdDocumentoPeriodo;

    @Getter
    @AllArgsConstructor
    @Setter
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class GfdFuncionarioDto {
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
        private String email;

        @NotNull
        private LocalDate periodoInicial;

        @NotNull
        private LocalDate periodoFinal;

        private String observacao;

        private Integer ativo;

        @Size(min = 1, message = "É necessário informar pelo menos um local de trabalho")
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

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class GfdDocumentoPeriodoDto {

        private LocalDate periodo;
    }
}
