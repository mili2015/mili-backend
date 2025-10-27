package br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.gfdFuncionarioLiberacao;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class GfdFuncionarioLiberacaoGetAllOutputDto {
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
        @Id
        @Column(name = "CTUSU_CODIGO")
        private Integer id;

        @Column(name = "CTUSU_NOME")
        private String username;
    }
}
