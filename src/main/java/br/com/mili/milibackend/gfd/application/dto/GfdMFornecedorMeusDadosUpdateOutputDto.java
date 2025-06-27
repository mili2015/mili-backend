package br.com.mili.milibackend.gfd.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GfdMFornecedorMeusDadosUpdateOutputDto {
    private String codigo;

    private String cgcCpf;

    private Integer ordemCgc;

    private String razaoSocial;

    private String nomeFantasia;

    private String endereco;

    private String bairro;

    private String cidade;

    private String estado;

    private String cep;

    private Integer aliqIcms;

    private String pgEndereco;

    private String pgBairro;

    private String pgCidade;

    private String pgEstado;

    private String pgCep;

    private String ie;

    private String telefone;

    private String fax;

    private String observacao;

    private String telex;

    private String fone1;

    private String fone2;

    private String uf;

    private String contato;

    private Integer codFornAnt;

    private Integer codBanco;

    private Integer codAgencia;

    private String digitoAgencia;

    private Long numeroCc;

    private String digitoCc;

    private String email;

    private Integer codNaturezaFornecedor;


    private LocalDate validadeSituacaoCadastral;

    private String contaContabil;

    private String agruparLancContabil;

    private String historicoContabil;

    private String usuario;

    private String maquina;

    private String boleto;


    private LocalDate dtConsultaSintegra;

    private String situacaoSintegra;

    private String mensagemSintegra;

    private String regimeApuracaoIcms;

    private String divergenciaSintegra;


    private LocalDate dataUltimaCompra;

    private String tipoDocumentoEmitido;

    private String tipoServicoSt;

    private Integer codGrupoServico;

    private Integer codSubGrupoServico;

    private String localPrestacaoServico;

    private String codCnae;

    private String tipoConta;

    private String email2;

    private String cidadeAnterior;

    private Integer codPais;

    private String optanteSimples;

    private String inscricaoMunicipal;

    private String bloqueado;

    private String motivoBloqueio;

    private String emailRnc;


    private LocalDate dataCadastro;


    private LocalDate dataNascimentoAbertura;

    private String contaBancariaAnterior;

    private String arquivoAutorizacaoDeposito;

    private String spedReinfTipoServico;

    private String spedReinfAtivEconomica;


    private LocalDate validadeRegimeApuracao;

    private String emailContasPagar;


    private LocalDate dataAtualizacaoRm;

    private String enderecoSintegra;

    private String numeroSintegra;

    private String complementoSintegra;

    private String bairroSintegra;

    private String excluirIcmsBasePisCofins;

    private String tipoChavePix;

    private String chavePix;

    private String operacaoContaCaixa;

    private String permitirCompraFornDireto;

    private Integer codNaturezaRend;

    private String fscTipoFornecedor;

    private String fscCategoriaMaterial;

    private String cnpjContaBancaria;

    private Integer codUsuario;
}
