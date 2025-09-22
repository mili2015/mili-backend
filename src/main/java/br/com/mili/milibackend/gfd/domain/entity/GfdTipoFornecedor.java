package br.com.mili.milibackend.gfd.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "GFD_TIPO_FORNECEDOR")
public class GfdTipoFornecedor {

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GFD_TIPO_FORNECEDOR")
    @SequenceGenerator(name="SEQ_GFD_TIPO_FORNECEDOR", sequenceName="SEQ_GFD_TIPO_FORNECEDOR", allocationSize = 1)
    private Integer id;
    
    @Column(name = "DESCRICAO")
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CATEGORIA_DOC", referencedColumnName = "ID")
    private GfdCategoriaDocumento categoriaDocumento;
}
