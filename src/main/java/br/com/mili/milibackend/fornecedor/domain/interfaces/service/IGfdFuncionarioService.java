package br.com.mili.milibackend.fornecedor.domain.interfaces.service;

import br.com.mili.milibackend.fornecedor.application.dto.gfdFuncionario.*;
import br.com.mili.milibackend.shared.page.pagination.MyPage;

public interface IGfdFuncionarioService {
    MyPage<GfdFuncionarioGetAllOutputDto> getAll(GfdFuncionarioGetAllInputDto inputDto);
    GfdFuncionarioCreateOutputDto create(GfdFuncionarioCreateInputDto inputDto);
    GfdFuncionarioUpdateOutputDto update(GfdFuncionarioUpdateInputDto inputDto);
    GfdFuncionarioGetByIdOutputDto getById(Integer id);

    void delete(GfdFuncionarioDeleteInputDto inputDto);
}
