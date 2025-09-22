package br.com.mili.milibackend.trade.odometro.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradeOdometroUpdateOutputDto {
    private Integer id;

    private BigDecimal kmFim;

    private LocalDateTime dataFim;
}
