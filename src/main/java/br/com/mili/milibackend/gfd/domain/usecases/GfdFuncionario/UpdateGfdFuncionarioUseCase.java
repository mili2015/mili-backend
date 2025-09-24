package br.com.mili.milibackend.gfd.domain.usecases.GfdFuncionario;

import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioUpdateInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioUpdateOutputDto;

public interface UpdateGfdFuncionarioUseCase {
    GfdFuncionarioUpdateOutputDto execute(GfdFuncionarioUpdateInputDto inputDto);
}
