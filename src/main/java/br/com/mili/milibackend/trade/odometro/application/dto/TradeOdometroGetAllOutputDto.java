package br.com.mili.milibackend.trade.odometro.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "DTO de resposta para leituras de odómetro")
public class TradeOdometroGetAllOutputDto {
    private Integer id;

    private Integer idColaborador;

    private LocalDateTime dataInicio;

    private LocalDateTime dataFim;

    private BigDecimal kmInicio;

    private BigDecimal kmFim;

    private String pathImgInicio;

    private String pathImgFim;

    private String tipoVeiculo;

    private ColaboradorDto colaborador;


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class ColaboradorDto {
        private Integer id;

        private Integer idColaboradorSuperior;

        private Integer mcdaCodigo;

        private Integer ctusuCodigo;

        private String nome;

        private String sobrenome;
    }

    private BigDecimal diferencaKm;
    private BigDecimal totalDiferencaKm;
}
