package br.com.mili.milibackend.trade.odometro.application.dto;

import br.com.mili.milibackend.shared.page.Filtro;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springdoc.core.annotations.ParameterObject;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ParameterObject
public class TradeOdometroGetAllInputDto extends Filtro {

    @Schema(description = "ID do registro")
    private Integer id;

    @Schema(description = "ID do colaborador")
    private Integer idColaborador;
    
    @Schema(description = "Data de início para filtro (formato: yyyy-MM-dd'T'HH:mm:ss)")
    private LocalDateTime dataInicio;
    
    @Schema(description = "Data de fim para filtro (formato: yyyy-MM-dd'T'HH:mm:ss)")
    private LocalDateTime dataFim;
    
    @Schema(description = "Tipo de veículo")
    private String tipoVeiculo;

}
