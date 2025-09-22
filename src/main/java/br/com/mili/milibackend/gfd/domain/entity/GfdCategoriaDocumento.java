package br.com.mili.milibackend.gfd.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
import br.com.mili.milibackend.gfd.domain.entity.GfdTipoDocumento;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "GFD_CATEGORIA_DOCUMENTO")
public class GfdCategoriaDocumento {

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GFD_CATEGORIA_DOCUMENTO")
    @SequenceGenerator(name="SEQ_GFD_CATEGORIA_DOCUMENTO", sequenceName="SEQ_GFD_CATEGORIA_DOCUMENTO", allocationSize = 1)
    private Integer id;

    @Column(name = "NOME")
    private String nome;

    @Column(name = "TIPO")
    private String tipo;

    @OneToMany(mappedBy = "categoriaDocumento")
    @Builder.Default
    private List<GfdTipoDocumento> tiposDocumento = new ArrayList<>();
}