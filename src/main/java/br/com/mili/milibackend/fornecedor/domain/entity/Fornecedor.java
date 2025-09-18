package br.com.mili.milibackend.fornecedor.domain.entity;

import br.com.mili.milibackend.gfd.domain.entity.GfdCategoriaDocumento;
import br.com.mili.milibackend.gfd.domain.entity.GfdDocumento;
import br.com.mili.milibackend.gfd.domain.entity.GfdTipoFornecedor;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Builder
@Table(name = "CT_FORNECEDOR")
@EqualsAndHashCode()
public class Fornecedor {

    @Id
    @Column(name = "CTFOR_CODIGO", nullable = false)
    private Integer codigo;

    @Column(name = "CTFOR_CGCCPF", nullable = false)
    private String cgcCpf;

    @Column(name = "CTFOR_ORDEM_CGC", nullable = false)
    private Integer ordemCgc;

    @Column(name = "RAZAO_SOCIAL", nullable = false)
    private String razaoSocial;

    @Column(name = "NOME_FANTASIA")
    private String nomeFantasia;

    @Column(name = "ENDERECO")
    private String endereco;

    @Column(name = "BAIRRO")
    private String bairro;

    @Column(name = "CIDADE")
    private String cidade;

    @Column(name = "ESTADO")
    private String estado;

    @Column(name = "TELEX")
    private String celular;

    @Column(name = "CEP")
    private String cep;

    @Column(name = "IE")
    private String ie;

    @Column(name = "TELEFONE")
    private String telefone;

    @Column(name = "OBSERV")
    private String observacao;

    @Column(name = "FONE1")
    private String fone1;

    @Column(name = "FONE2")
    private String fone2;

    @Column(name = "UF", nullable = false)
    private String uf;

    @Column(name = "CONTATO")
    private String contato;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "CODIGO_NATUREZA_FORNECEDOR", nullable = false)
    private Integer codNaturezaFornecedor;

    @Column(name = "USUARIO")
    private String usuario;

    @Column(name = "CODIGO_CNAE")
    private String codCnae;

    @Column(name = "TIPO_CONTA")
    private String tipoConta;

    @Column(name = "EMAIL2")
    private String email2;

    @Column(name = "COD_PAIS")
    private Integer codPais;

    @Column(name = "INSCRICAO_MUNICIPAL")
    private String inscricaoMunicipal;

    @Column(name = "ACEITE_LGPD")
    private Integer aceiteLgpd;

    @Column(name = "BLOQUEADO")
    private String bloqueado;

    @OneToMany(mappedBy = "ctforCodigo", fetch = FetchType.LAZY)
    private List<GfdDocumento> documentos;

    @ManyToOne
    @JoinColumn(name = "ID_TIPO_FORNECEDOR")
    private GfdTipoFornecedor tipoFornecedor;
}