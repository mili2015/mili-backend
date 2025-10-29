package br.com.mili.milibackend.gfd.domain.usecases.gfdFuncionario;

import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioUpdateObservacaoInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioUpdateObservacaoOutputDto;

public interface UpdateObservacaoFuncionarioUseCase {
    GfdFuncionarioUpdateObservacaoOutputDto execute(GfdFuncionarioUpdateObservacaoInputDto inputDto);
}
