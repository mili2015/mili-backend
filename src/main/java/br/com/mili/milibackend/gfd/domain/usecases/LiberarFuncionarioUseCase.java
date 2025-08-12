package br.com.mili.milibackend.gfd.domain.usecases;

import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioLiberarInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioLiberarOutputDto;

public interface LiberarFuncionarioUseCase {
    GfdFuncionarioLiberarOutputDto execute(GfdFuncionarioLiberarInputDto inputDto);
}
