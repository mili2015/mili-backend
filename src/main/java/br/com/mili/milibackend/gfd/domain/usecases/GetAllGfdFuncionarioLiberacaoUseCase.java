package br.com.mili.milibackend.gfd.domain.usecases;

import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.gfdFuncionarioLiberacao.GfdFuncionarioLiberacaoGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.gfdFuncionarioLiberacao.GfdFuncionarioLiberacaoGetAllOutputDto;
import br.com.mili.milibackend.shared.page.pagination.MyPage;

import java.util.List;

public interface GetAllGfdFuncionarioLiberacaoUseCase {
    List<GfdFuncionarioLiberacaoGetAllOutputDto> execute(GfdFuncionarioLiberacaoGetAllInputDto inputDto);
}
