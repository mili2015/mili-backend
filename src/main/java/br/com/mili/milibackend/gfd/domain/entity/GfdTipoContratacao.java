package br.com.mili.milibackend.gfd.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "GFD_TIPO_CONTRATACAO")
public class GfdTipoContratacao {

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GFD_TIPO_CONTRATACAO")
    @SequenceGenerator(name="SEQ_GFD_TIPO_CONTRATACAO", sequenceName="SEQ_GFD_TIPO_CONTRATACAO", allocationSize = 1)
    private Integer id;
    
    @Column(name = "DESCRICAO")
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "ID_CATEGORIA_DOC")
    private GfdCategoriaDocumento categoriaDocumento;

}
