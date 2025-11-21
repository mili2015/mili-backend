package br.com.mili.milibackend.gfd.infra.projections;

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
    Integer getIdTipoContratacao();
    String getObservacao();
    Character getAtivo();
    Character getLiberado();
    String getEmail();
    Integer getDesligado();

    Integer getTotalEnviado();
    Integer getTotalConforme();
    Integer getTotalNaoConforme();
    Integer getTotalEmAnalise();
    Integer getNaoEnviado();
}
