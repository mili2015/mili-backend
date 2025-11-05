package br.com.mili.milibackend.gfd.infra.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class GfdDocumentoHistoricoResumoDto {
    private Integer id;
    private Integer funcionarioId;
    private Integer documentoId;
    private Integer ctforCodigo;
    private UserDto usuario;
    private LocalDateTime data;
    private String status;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class UserDto {
        private Integer id;
        private String username;
    }
}
