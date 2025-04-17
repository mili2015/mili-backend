package br.com.mili.milibackend.infra.security.repository.projections;

public interface SystemAuthProjection {
    Integer getSystemId();
    String getAuthority();
}
