package br.com.mili.milibackend.gfd.application.dto.gfdFuncionario;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class GfdFuncionarioUpdateInputDto {
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
    private List<LocalTrabalhoDto> locaisTrabalho;

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
