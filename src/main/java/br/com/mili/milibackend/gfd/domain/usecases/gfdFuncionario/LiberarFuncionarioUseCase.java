package br.com.mili.milibackend.gfd.domain.usecases.gfdFuncionario;

import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.gfdFuncionarioLiberacao.GfdFuncionarioLiberarInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.gfdFuncionarioLiberacao.GfdFuncionarioLiberarOutputDto;

public interface LiberarFuncionarioUseCase {
    GfdFuncionarioLiberarOutputDto execute(GfdFuncionarioLiberarInputDto inputDto);
}
