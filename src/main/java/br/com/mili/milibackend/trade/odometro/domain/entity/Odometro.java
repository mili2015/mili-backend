package br.com.mili.milibackend.trade.odometro.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MCD_ODOMETRO")
public class Odometro {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MCD_ODOMETRO")
    @SequenceGenerator(name = "SEQ_MCD_ODOMETRO", sequenceName = "SEQ_MCD_ODOMETRO", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Integer id;
    
    @Column(name = "ID_COLABORADOR")
    private Integer idColaborador;
    
    @Column(name = "DATA_INICIO")
    private LocalDateTime dataInicio;
    
    @Column(name = "DATA_FIM")
    private LocalDateTime dataFim;
    
    @Column(name = "KM_INICIO")
    private BigDecimal kmInicio;
    
    @Column(name = "KM_FIM")
    private BigDecimal kmFim;
    
    @Column(name = "PATH_IMG_INICIO")
    private String pathImgInicio;
    
    @Column(name = "PATH_IMG_FIM")
    private String pathImgFim;
    
    @Column(name = "TIPO_VEICULO")
    private String tipoVeiculo;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_COLABORADOR", referencedColumnName = "ID", insertable = false, updatable = false)
    private Colaborador colaborador;
}
