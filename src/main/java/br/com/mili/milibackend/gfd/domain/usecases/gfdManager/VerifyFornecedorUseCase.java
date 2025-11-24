package br.com.mili.milibackend.gfd.domain.usecases.gfdManager;

import br.com.mili.milibackend.gfd.application.dto.manager.funcionario.GfdMVerificarFornecedorInputDto;
import br.com.mili.milibackend.gfd.application.dto.manager.funcionario.GfdMVerificarFornecedorOutputDto;

public interface VerifyFornecedorUseCase {
    GfdMVerificarFornecedorOutputDto execute(GfdMVerificarFornecedorInputDto inputDto);
}
