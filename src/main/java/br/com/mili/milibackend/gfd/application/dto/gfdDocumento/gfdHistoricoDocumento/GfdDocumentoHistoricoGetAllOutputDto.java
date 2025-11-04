package br.com.mili.milibackend.gfd.application.dto.gfdDocumento.gfdHistoricoDocumento;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class GfdDocumentoHistoricoGetAllOutputDto {
    private Integer id;
    private Integer funcionarioId ;
    private Integer documentoId ;
    private Integer ctforCodigo ;
    private UserDto usuario;
    private LocalDateTime data;
    private String status;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class UserDto {
        @Id
        @Column(name = "CTUSU_CODIGO")
        private Integer id;

        @Column(name = "CTUSU_NOME")
        private String username;
    }
}
