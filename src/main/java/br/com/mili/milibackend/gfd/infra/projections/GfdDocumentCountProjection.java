package br.com.mili.milibackend.gfd.infra.projections;

public interface GfdDocumentCountProjection {
    Integer getTotalEnviado();
    Integer getTotalConforme();
    Integer getTotalNaoConforme();
    Integer getTotalEmAnalise();
    Integer getNaoEnviado();
}
