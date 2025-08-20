package br.com.mili.milibackend.fornecedor.domain.usecases;

import br.com.mili.milibackend.fornecedor.domain.entity.Fornecedor;

public interface GetFornecedorByCodOrIdUseCase {
    Fornecedor execute(Integer codUsuario, Integer id);
}
