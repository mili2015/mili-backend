package br.com.mili.milibackend.fornecedor.infra.projections;

import java.time.LocalDate;

public interface GfdFuncionarioStatusProjection {
    Integer getCtforCodigo();
    Integer getId();
    String getNome();
    String getCpf();
    LocalDate getDataNascimento();
    String getPaisNacionalidade();
    String getFuncao();
    String getTipoContratacao();
    LocalDate getPeriodoInicial();
    LocalDate getPeriodoFinal();
    String getObservacao();
    Integer getAtivo();

    Integer getTotalEnviado();
    Integer getTotalConforme();
    Integer getTotalNaoConforme();
    Integer getTotalEmAnalise();
    Integer getNaoEnviado();
}
