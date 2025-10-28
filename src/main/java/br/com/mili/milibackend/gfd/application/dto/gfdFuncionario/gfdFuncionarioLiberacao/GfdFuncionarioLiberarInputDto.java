package br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.gfdFuncionarioLiberacao;


import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@AllArgsConstructor
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class GfdFuncionarioLiberarInputDto {
    @NotNull
    private Integer id;

    @NotNull
    private Integer liberado;

    private Integer usuario;

    private String justificativa;
}
