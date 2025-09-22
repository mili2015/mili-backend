package br.com.mili.milibackend.trade.odometro.infra.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OdometroResumoDto {
    private Integer id;
    private Integer idColaborador;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private BigDecimal kmInicio;
    private BigDecimal kmFim;
    private String pathImgInicio;
    private String pathImgFim;
    private String tipoVeiculo;

    private Colaborador colaborador;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Colaborador {
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
