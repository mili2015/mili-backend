package br.com.mili.milibackend.gfd.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "GFD_DOCUMENTO_PERIODO")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GfdDocumentoPeriodo {
    @Id
    @SequenceGenerator(name = "SEQ_GFD_DOCUMENTO_PERIODO", sequenceName = "SEQ_GFD_DOCUMENTO_PERIODO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GFD_DOCUMENTO_PERIODO")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "ID_DOCUMENTO")
    private GfdDocumento gfdDocumento;

    @Column(name = "PERIODO_INICIAL")
    private LocalDate periodoInicial;

    @Column(name = "PERIODO_FINAL")
    private LocalDate periodoFinal;
}