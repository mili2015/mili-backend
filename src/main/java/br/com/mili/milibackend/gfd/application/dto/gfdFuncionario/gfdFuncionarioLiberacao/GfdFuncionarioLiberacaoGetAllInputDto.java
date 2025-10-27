package br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.gfdFuncionarioLiberacao;

import br.com.mili.milibackend.shared.page.Filtro;
import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Setter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Builder
public class GfdFuncionarioLiberacaoGetAllInputDto {
    private Integer funcionarioId;
    private Integer statusLiberado;
    private LocalDate periodoInicio;
    private LocalDate periodoFim;
}
