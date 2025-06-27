package br.com.mili.milibackend.fornecedor.domain.entity;

import br.com.mili.milibackend.fornecedor.infra.converter.GfdDocumentoStatusEnumConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "CT_FORNECEDOR_DOCUMENTOS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GfdDocumento {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CT_FORNECEDOR_DOCUMENTOS")
    @SequenceGenerator(name="SEQ_CT_FORNECEDOR_DOCUMENTOS", sequenceName="SEQ_CT_FORNECEDOR_DOCUMENTOS", allocationSize = 1)
    private Integer id;

    @Column(name = "CTFOR_CODIGO")
    private Integer ctforCodigo;

    @Column(name = "TIPO_DOCUMENTO")
    private String tipoDocumento;

    @Column(name = "NOME_ARQUIVO")
    private String nomeArquivo;

    @Column(name = "NOME_ARQUIVO_PATH")
    private String nomeArquivoPath;

    @Column(name = "TAMANHO_ARQUIVO")
    private Integer tamanhoArquivo;

    @Lob
    @Column(name = "ARQUIVO")
    private byte[] arquivo;

    @Column(name = "DATA_CADASTRO")
    private LocalDate dataCadastro;

    @Column(name = "DATA_VALIDADE")
    private LocalDate dataValidade;

    @Column(name = "DATA_EMISSAO")
    private LocalDate dataEmissao;

    @Column(name = "TIPO_ARQUIVO")
    private String tipoArquivo;

    @Column(name = "USUARIO")
    private String usuario;

    @Column(name = "MAQUINA")
    private String maquina;

    @Column(name = "OBSERVACAO")
    private String observacao;

    @Convert(converter = GfdDocumentoStatusEnumConverter.class)
    @Column(name = "STATUS")
    private GfdDocumentoStatusEnum status;

    @ManyToOne
    @JoinColumn(name = "ID_TIPO_DOCUMENTO")
    private GfdTipoDocumento gfdTipoDocumento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_FUNCIONARIO")
    private GfdFuncionario gfdFuncionario;
}
