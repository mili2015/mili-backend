package br.com.mili.milibackend.gfd.application.dto.gfdFuncionario;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class GfdFuncionarioCreateOutputDto {
    private Integer id;
    private FornecedorDto fornecedor;
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private String paisNacionalidade;
    private String funcao;
    private LocalDate periodoInicial;
    private LocalDate periodoFinal;
    private String observacao;
    private String ativo;
    private Integer liberado;
    private List<LocalTrabalhoDto> locaisTrabalho;
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

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class LocalTrabalhoDto {
        private Integer ctempCodigo;
    }
}
