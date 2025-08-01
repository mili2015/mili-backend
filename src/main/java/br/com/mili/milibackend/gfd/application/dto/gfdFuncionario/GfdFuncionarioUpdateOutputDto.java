package br.com.mili.milibackend.gfd.application.dto.gfdFuncionario;

import br.com.mili.milibackend.fornecedor.domain.entity.Fornecedor;
import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class GfdFuncionarioUpdateOutputDto {
    private Integer id;
    private Fornecedor fornecedor;
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private String paisNacionalidade;
    private String funcao;
    private String tipoContratacao;
    private LocalDate periodoInicial;
    private LocalDate periodoFinal;
    private String observacao;
    private String ativo;
    private Integer liberado;
}
