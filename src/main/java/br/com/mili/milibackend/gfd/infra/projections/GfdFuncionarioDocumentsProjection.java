package br.com.mili.milibackend.gfd.infra.projections;

public interface GfdFuncionarioDocumentsProjection {
    Integer getTotalEnviado();
    Integer getTotalConforme();
    Integer getTotalNaoConforme();
    Integer getTotalEmAnalise();
    Integer getNaoEnviado();
}
