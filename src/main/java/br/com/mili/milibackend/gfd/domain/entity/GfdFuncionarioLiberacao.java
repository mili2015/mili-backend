package br.com.mili.milibackend.gfd.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "GFD_FUNC_LIBERACAO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GfdFuncionarioLiberacao {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GFD_FUNCIONARIO_LIBERACAO")
    @SequenceGenerator(name = "SEQ_GFD_FUNCIONARIO_LIBERACAO", sequenceName = "SEQ_GFD_FUNCIONARIO_LIBERACAO", allocationSize = 1)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_FUNCIONARIO")
    private GfdFuncionario funcionario;

    @Column(name = "CTUSU_CODIGO")
    private Integer usuario;

    @Column(name = "DATA")
    private LocalDateTime data;

    @Column(name = "STATUS_LIBERADO")
    private Integer statusLiberado;

    @Column(name = "JUSTIFICATIVA")
    private String justificativa;
}