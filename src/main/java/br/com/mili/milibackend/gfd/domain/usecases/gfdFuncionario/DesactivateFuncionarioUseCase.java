package br.com.mili.milibackend.gfd.domain.usecases.gfdFuncionario;

import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioDesactivateInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioDesactivateOutputDto;

public interface DesactivateFuncionarioUseCase {
    void execute(GfdFuncionarioDesactivateInputDto inputDto);
}
