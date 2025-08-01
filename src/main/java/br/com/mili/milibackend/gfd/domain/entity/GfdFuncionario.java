package br.com.mili.milibackend.gfd.domain.entity;

import br.com.mili.milibackend.fornecedor.domain.entity.Fornecedor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "GFD_FORNECEDOR_FUNCIONARIO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GfdFuncionario {
    @Id
    @Column(name = "ID_FUNCIONARIO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GFD_FORNECEDOR_FUNCIONARIO")
    @SequenceGenerator(name = "SEQ_GFD_FORNECEDOR_FUNCIONARIO", sequenceName = "SEQ_GFD_FORNECEDOR_FUNCIONARIO", allocationSize = 1)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "CTFOR_CODIGO")
    private Fornecedor fornecedor;

    @Column(name = "NOME")
    private String nome;

    @Column(name = "CPF")
    private String cpf;

    @Column(name = "DATA_NASCIMENTO")
    private LocalDate dataNascimento;

    @Column(name = "PAIS_NACIONALIDADE")
    private String paisNacionalidade;

    @Column(name = "FUNCAO")
    private String funcao;

    @Column(name = "TIPO_CONTRATACAO")
    private String tipoContratacao;

    @Column(name = "PERIODO_INICIAL")
    private LocalDate periodoInicial;

    @Column(name = "PERIODO_FINAL")
    private LocalDate periodoFinal;

    @Column(name = "OBSERVACAO")
    private String observacao;

    @Column(name = "ATIVO")
    private Integer ativo;

    @Column(name = "LIBERADO")
    private Integer liberado;

    @OneToMany(mappedBy = "id")
    private List<GfdDocumento> documentos;
}
