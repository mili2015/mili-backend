package br.com.mili.milibackend.gfd.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "GFD_RESPONSAVEL_INTEGRACAO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GfdResponsavelIntegracao {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GFD_RESPONSAVEL_INTEGRACAO")
    @SequenceGenerator(name = "SEQ_GFD_RESPONSAVEL_INTEGRACAO", sequenceName = "SEQ_GFD_RESPONSAVEL_INTEGRACAO", allocationSize = 1)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "CTEMP_CODIGO")
    private Integer ctempCodigo;
}
