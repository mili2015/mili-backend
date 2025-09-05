package br.com.mili.milibackend.gfd.application.dto.gfdFuncionario;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class GfdFuncionarioGetAllOutputDto {
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
    private Character ativo;
    private Character liberado;
    private FornecedorDto fornecedor;

    private Integer totalEnviado;
    private Integer totalConforme;
    private Integer totalNaoConforme;
    private Integer totalEmAnalise;
    private Integer naoEnviado;

    @Builder.Default
    private List<LocalTrabalhoDto> locaisTrabalho = new ArrayList<>();

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
