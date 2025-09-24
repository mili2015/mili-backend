package br.com.mili.milibackend.gfd.domain.usecases.gfdFuncionario;

import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioCreateInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioCreateOutputDto;

public interface CreateFuncionarioUseCase {
    GfdFuncionarioCreateOutputDto execute(GfdFuncionarioCreateInputDto inputDto);
}
