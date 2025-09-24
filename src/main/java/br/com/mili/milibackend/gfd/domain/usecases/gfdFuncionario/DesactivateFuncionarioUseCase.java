package br.com.mili.milibackend.gfd.domain.usecases.GfdFuncionario;

import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioDesactivateInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioDesactivateOutputDto;

public interface DesactivateFuncionarioUseCase {
    void execute(GfdFuncionarioDesactivateInputDto inputDto);
}
