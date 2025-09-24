package br.com.mili.milibackend.trade.odometro.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TradeOdometroUpdateInputDto {

    @NotNull(message = "A quilometragem final é obrigatória")
    private Integer id;

    @Schema(description = "Quilometragem inicial do veículo",example = "100.50")
    @NotNull(message = "A quilometragem final é obrigatória")
    @DecimalMin(value = "0.0", message = "A quilometragem final deve ser maior ou igual a zero")
    private BigDecimal kmInicio;

    @NotNull(message = "A data inicial é obrigatória")
    private LocalDateTime dataInicio;

    @Schema(description = "Quilometragem final do veículo",example = "150.50")
    @NotNull(message = "A quilometragem final é obrigatória")
    @DecimalMin(value = "0.0", message = "A quilometragem final deve ser maior ou igual a zero")
    private BigDecimal kmFim;

    @NotNull(message = "A data final é obrigatória")
    private LocalDateTime dataFim;

}
