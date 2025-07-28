package br.com.mili.milibackend.gfd.application.policy;


import br.com.mili.milibackend.shared.infra.security.model.CustomUserPrincipal;

public interface IGfdPolicy {
    boolean isAnalista(CustomUserPrincipal user);
    boolean isFornecedor(CustomUserPrincipal user);
}
