package br.com.mili.milibackend.trade.odometro.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de resposta para colaboradores")
public class TradeColaboradorGetAllOutputDto {
    private Integer id;
    private Integer idColaboradorSuperior;
    private Integer mcdaCodigo;
    private Integer ctusuCodigo;
    private String nomeCompleto;
    private String cpf;
    private String email;
    private String celular;
    private String uf;
    private String cidade;
    private String ativo;
}
