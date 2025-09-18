package br.com.mili.milibackend.gfd.domain.usecases.gfdFuncionario;

import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioUpdateInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioUpdateOutputDto;

public interface UpdateGfdFuncionarioUseCase {
    GfdFuncionarioUpdateOutputDto execute(GfdFuncionarioUpdateInputDto inputDto);
}
