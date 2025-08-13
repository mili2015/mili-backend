package br.com.mili.milibackend.gfd.application.dto.gfdFuncionario;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GfdFuncionarioUpdateObservacaoInputDto {
    @NotNull
    private Integer id;

    @NotNull
    private String observacao;
}
