package br.com.mili.milibackend.trade.odometro.application.dto;

import br.com.mili.milibackend.shared.page.Filtro;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springdoc.core.annotations.ParameterObject;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ParameterObject
public class TradeColaboradorGetAllInputDto extends Filtro {

    @Schema(description = "ID do colaborador")
    private Integer id;

    @Schema(description = "Filtro por nome completo (nome + sobrenome)")
    private String nome;

    @Schema(description = "Filtro por e-mail")
    private String email;

    @Schema(description = "CPF do colaborador")
    private String cpf;

    @Schema(description = "Status de ativo: 'S' ou 'N'")
    private String ativo;

    @Schema(description = "ID do colaborador superior")
    private Integer idColaboradorSuperior;
}
