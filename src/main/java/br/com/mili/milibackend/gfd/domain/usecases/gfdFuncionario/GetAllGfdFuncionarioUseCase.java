package br.com.mili.milibackend.gfd.domain.usecases.gfdFuncionario;


import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioGetAllOutputDto;
import br.com.mili.milibackend.shared.page.pagination.MyPage;

public interface GetAllGfdFuncionarioUseCase {
    MyPage<GfdFuncionarioGetAllOutputDto> execute(GfdFuncionarioGetAllInputDto inputDto);
}
