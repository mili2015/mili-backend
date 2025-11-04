package br.com.mili.milibackend.gfd.infra.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class GfdFuncionarioLiberacaoResumoDto {
    private Integer id;
    private Integer funcionarioId;
    private UserDto usuario;
    private LocalDateTime data;
    private Integer statusLiberado;
    private String justificativa;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class UserDto {
        private Integer id;
        private String username;
    }
}
