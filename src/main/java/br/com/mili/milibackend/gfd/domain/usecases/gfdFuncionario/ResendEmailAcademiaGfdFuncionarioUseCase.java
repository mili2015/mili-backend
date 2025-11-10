package br.com.mili.milibackend.gfd.domain.usecases.gfdFuncionario;

import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioResendEmailAcademiaInputDto;

public interface ResendEmailAcademiaGfdFuncionarioUseCase {
    void execute(GfdFuncionarioResendEmailAcademiaInputDto inputDto);
}
