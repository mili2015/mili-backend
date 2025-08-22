package br.com.mili.milibackend.fornecedor.domain.usecases;

public interface ValidatePermissionFornecedorUseCase {
    boolean execute(Integer codUsuario, Integer fornecedorId);
}
