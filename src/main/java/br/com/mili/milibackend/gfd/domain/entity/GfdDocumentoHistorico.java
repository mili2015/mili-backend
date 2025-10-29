package br.com.mili.milibackend.gfd.domain.entity;

import br.com.mili.milibackend.fornecedor.domain.entity.Fornecedor;
import br.com.mili.milibackend.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "GFD_DOCUMENTO_HISTORICO")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"funcionario", "usuario", "fornecedor", "documento"})
public class GfdDocumentoHistorico {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GFD_DOCUMENTO_HISTORICO")
    @SequenceGenerator(name="SEQ_GFD_DOCUMENTO_HISTORICO", sequenceName="SEQ_GFD_DOCUMENTO_HISTORICO", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "ID_FUNCIONARIO")
    private Integer funcionarioId ;

    @Column(name = "ID_DOCUMENTO")
    private Integer documentoId ;

    @Column(name = "CTFOR_CODIGO")
    private Integer ctforCodigo ;

    @Column(name = "CTUSU_CODIGO")
    private Integer ctusuCodigo ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_FUNCIONARIO", insertable = false, updatable = false)
    private GfdFuncionario funcionario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_DOCUMENTO", insertable = false, updatable = false)
    private GfdDocumento documento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CTFOR_CODIGO", referencedColumnName = "CTFOR_CODIGO", nullable = false, insertable = false, updatable = false)
    private Fornecedor fornecedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CTUSU_CODIGO", referencedColumnName = "CTUSU_CODIGO", nullable = false, insertable = false, updatable = false)
    private User usuario;

    @Column(name = "DATA", nullable = false)
    private LocalDateTime data;

    @Column(name = "STATUS", nullable = false)
    private String status;
}
