package br.com.mili.milibackend.gfd.domain.usecases.gfdFuncionario;

import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.gfdFuncionarioLiberacao.GfdFuncionarioLiberacaoGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.gfdFuncionarioLiberacao.GfdFuncionarioLiberacaoGetAllOutputDto;

import java.util.List;

public interface GetAllGfdFuncionarioLiberacaoUseCase {
    List<GfdFuncionarioLiberacaoGetAllOutputDto> execute(GfdFuncionarioLiberacaoGetAllInputDto inputDto);
}
