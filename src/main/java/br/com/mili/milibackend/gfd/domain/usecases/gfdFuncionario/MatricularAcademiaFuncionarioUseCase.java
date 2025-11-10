package br.com.mili.milibackend.gfd.domain.usecases.gfdFuncionario;

import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.MatricularAcademiaFuncionarioInputDto;

public interface MatricularAcademiaFuncionarioUseCase {
    Integer execute(MatricularAcademiaFuncionarioInputDto inputDto);
}
