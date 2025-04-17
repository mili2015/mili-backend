package br.com.mili.milibackend.fornecedor.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Builder
@Table(name = "CT_FORNECEDOR")
public class Fornecedor {

    @Id
    @Column(name = "CTFOR_CODIGO", length = 9, nullable = false)
    private String codigo;

    @Column(name = "CTFOR_CGCCPF", length = 15, nullable = false)
    private String cgcCpf;

    @Column(name = "CTFOR_ORDEM_CGC", nullable = false)
    private Integer ordemCgc;

    @Column(name = "RAZAO_SOCIAL", length = 100, nullable = false)
    private String razaoSocial;

    @Column(name = "NOME_FANTASIA", length = 50)
    private String nomeFantasia;

    @Column(name = "ENDERECO", length = 75)
    private String endereco;

    @Column(name = "BAIRRO", length = 60)
    private String bairro;

    @Column(name = "CIDADE", length = 32)
    private String cidade;

    @Column(name = "ESTADO", length = 2)
    private String estado;

    @Column(name = "CEP", length = 8)
    private String cep;

    @Column(name = "ALIQICMS")
    private Integer aliqIcms;

    @Column(name = "PGENDERECO", length = 50)
    private String pgEndereco;

    @Column(name = "PGBAIRRO", length = 40)
    private String pgBairro;

    @Column(name = "PGCIDADE", length = 20)
    private String pgCidade;

    @Column(name = "PGESTADO", length = 2)
    private String pgEstado;

    @Column(name = "PGCEP", length = 8)
    private String pgCep;

    @Column(name = "IE", length = 14)
    private String ie;

    @Column(name = "TELEFONE", length = 14)
    private String telefone;

    @Column(name = "FAX", length = 14)
    private String fax;

    @Column(name = "OBSERV", length = 50)
    private String observacao;

    @Column(name = "TELEX", length = 20)
    private String telex;

    @Column(name = "FONE1", length = 14)
    private String fone1;

    @Column(name = "FONE2", length = 14)
    private String fone2;

    @Column(name = "UF", length = 2, nullable = false)
    private String uf;

    @Column(name = "CONTATO", length = 50)
    private String contato;

    @Column(name = "CODFORN_ANT")
    private Integer codFornAnt;

    @Column(name = "COD_BANCO")
    private Integer codBanco;

    @Column(name = "COD_AGENCIA")
    private Integer codAgencia;

    @Column(name = "DIGITO_AGENCIA", length = 1)
    private String digitoAgencia;

    @Column(name = "NUMERO_CC")
    private Long numeroCc;

    @Column(name = "DIGITO_CC", length = 2)
    private String digitoCc;

    @Column(name = "EMAIL", length = 150)
    private String email;

    @Column(name = "CODIGO_NATUREZA_FORNECEDOR", nullable = false)
    private Integer codNaturezaFornecedor;

    @Column(name = "VALIDADE_SITUACAO_CADASTRAL")
    @Temporal(TemporalType.DATE)
    private LocalDate validadeSituacaoCadastral;

    @Column(name = "CONTA_CONTABIL", length = 12)
    private String contaContabil;

    @Column(name = "AGRUPAR_LANC_CONTABIL", length = 1)
    private String agruparLancContabil;

    @Column(name = "HISTORICO_CONTABIL", length = 5)
    private String historicoContabil;

    @Column(name = "USUARIO", length = 30)
    private String usuario;

    @Column(name = "MAQUINA", length = 30)
    private String maquina;

    @Column(name = "BOLETO", length = 1)
    private String boleto;

    @Column(name = "DTCONSULTA_SINTEGRA")
    @Temporal(TemporalType.DATE)
    private LocalDate dtConsultaSintegra;

    @Column(name = "SITUACAO_SINTEGRA", length = 50)
    private String situacaoSintegra;

    @Column(name = "MENSAGEM_SINTEGRA", length = 60)
    private String mensagemSintegra;

    @Column(name = "REGIMEAPURACAOICMS", length = 120)
    private String regimeApuracaoIcms;

    @Column(name = "DIVERGENCIA_SINTEGRA", length = 255)
    private String divergenciaSintegra;

    @Column(name = "DATA_ULTIMA_COMPRA")
    @Temporal(TemporalType.DATE)
    private LocalDate dataUltimaCompra;

    @Column(name = "TIPO_DOCUMENTO_EMITIDO", length = 1)
    private String tipoDocumentoEmitido;

    @Column(name = "TIPO_SERVICO_ST", length = 1)
    private String tipoServicoSt;

    @Column(name = "CODIGO_GRUPO_SERVICO")
    private Integer codGrupoServico;

    @Column(name = "CODIGO_SUB_GRUPO_SERVICO")
    private Integer codSubGrupoServico;

    @Column(name = "LOCAL_PRESTACAO_SERVICO", length = 1)
    private String localPrestacaoServico;

    @Column(name = "CODIGO_CNAE", length = 7)
    private String codCnae;

    @Column(name = "TIPO_CONTA", length = 10)
    private String tipoConta;

    @Column(name = "EMAIL2", length = 50)
    private String email2;

    @Column(name = "CIDADE_ANTERIOR", length = 32)
    private String cidadeAnterior;

    @Column(name = "COD_PAIS")
    private Integer codPais;

    @Column(name = "OPTANTE_SIMPLES", length = 1)
    private String optanteSimples;

    @Column(name = "INSCRICAO_MUNICIPAL", length = 30)
    private String inscricaoMunicipal;

    @Column(name = "BLOQUEADO", length = 1)
    private String bloqueado;

    @Column(name = "MOTIVO_BLOQUEIO", length = 50)
    private String motivoBloqueio;

    @Column(name = "EMAIL_RNC", length = 250)
    private String emailRnc;

    @Column(name = "DATA_CADASTRO")
    @Temporal(TemporalType.DATE)
    private LocalDate dataCadastro;

    @Column(name = "DATA_NASCIMENTO_ABERTURA")
    @Temporal(TemporalType.DATE)
    private LocalDate dataNascimentoAbertura;

    @Column(name = "CONTA_BANCARIA_ANTERIOR", length = 40)
    private String contaBancariaAnterior;

    @Column(name = "ARQUIVO_AUTORIZACAO_DEPOSITO", length = 150)
    private String arquivoAutorizacaoDeposito;

    @Column(name = "SPED_REINF_TIPO_SERVICO", length = 10)
    private String spedReinfTipoServico;

    @Column(name = "SPED_REINF_ATIV_ECONOMICA", length = 10)
    private String spedReinfAtivEconomica;

    @Column(name = "VALIDADE_REGIME_APURACAO")
    @Temporal(TemporalType.DATE)
    private LocalDate validadeRegimeApuracao;

    @Column(name = "EMAIL_CONTAS_PAGAR", length = 60)
    private String emailContasPagar;

    @Column(name = "DATA_ATUALIZACAO_RM")
    @Temporal(TemporalType.DATE)
    private LocalDate dataAtualizacaoRm;

    @Column(name = "ENDERECO_SINTEGRA", length = 200)
    private String enderecoSintegra;

    @Column(name = "NUMERO_SINTEGRA", length = 40)
    private String numeroSintegra;

    @Column(name = "COMPLEMENTO_SINTEGRA", length = 200)
    private String complementoSintegra;

    @Column(name = "BAIRRO_SINTEGRA", length = 60)
    private String bairroSintegra;

    @Column(name = "EXCLUIR_ICMS_BASE_PIS_COFINS", length = 1)
    private String excluirIcmsBasePisCofins;

    @Column(name = "TIPO_CHAVE_PIX", length = 20)
    private String tipoChavePix;

    @Column(name = "CHAVE_PIX", length = 100)
    private String chavePix;

    @Column(name = "OPERACAO_CONTA_CAIXA", length = 3)
    private String operacaoContaCaixa;

    @Column(name = "PERMITIR_COMPRA_FORNEC_DIRETO", length = 1)
    private String permitirCompraFornDireto;

    @Column(name = "COD_NATUREZA_REND")
    private Integer codNaturezaRend;

    @Column(name = "FSC_TIPO_FORNECEDOR", length = 100)
    private String fscTipoFornecedor;

    @Column(name = "FSC_CATEGORIA_MATERIAL", length = 100)
    private String fscCategoriaMaterial;

    @Column(name = "CNPJ_CONTA_BANCARIA", length = 14)
    private String cnpjContaBancaria;

    @Column(name = "CTUSU_CODIGO", length = 14)
    private Integer codUsuario;
}