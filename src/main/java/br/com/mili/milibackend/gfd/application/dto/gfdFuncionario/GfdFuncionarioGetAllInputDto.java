package br.com.mili.milibackend.gfd.application.dto.gfdFuncionario;

import br.com.mili.milibackend.shared.page.Filtro;
import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Setter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Builder
public class GfdFuncionarioGetAllInputDto extends Filtro {
    private Integer id;
    private String nome;
    private String funcao;
    private Integer tipoContratacao;
    private LocalDate periodoInicio;
    private LocalDate periodoFim;
    private Integer ativo;

    private FornecedorDto fornecedor;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class FornecedorDto {
        private Integer codigo;

    }
}
