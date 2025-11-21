package br.com.mili.milibackend.gfd.application.dto.gfdFuncionario;

import lombok.*;

@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@Builder
@Data
public class GfdFuncionarioResendEmailAcademiaInputDto {
    private Integer idFuncionario;
    private Integer ctempCodigo;
}
