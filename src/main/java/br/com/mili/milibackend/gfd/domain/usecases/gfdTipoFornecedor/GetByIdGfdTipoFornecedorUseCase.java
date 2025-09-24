package br.com.mili.milibackend.gfd.domain.usecases.gfdTipoFornecedor;

import br.com.mili.milibackend.gfd.application.dto.gfdTipoFornecedor.GfdTipoFornecedorGetByIdOutputDto;

public interface GetByIdGfdTipoFornecedorUseCase {
    GfdTipoFornecedorGetByIdOutputDto execute(Integer id);

}
