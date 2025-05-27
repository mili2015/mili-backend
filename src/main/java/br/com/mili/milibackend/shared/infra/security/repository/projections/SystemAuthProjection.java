package br.com.mili.milibackend.shared.infra.security.repository.projections;

public interface SystemAuthProjection {
    Integer getSystemId();
    String getAuthority();
}
