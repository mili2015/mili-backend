package br.com.mili.milibackend.gfd.domain.interfaces;

import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.*;

public interface IGfdFuncionarioService {
    GfdFuncionarioCreateOutputDto create(GfdFuncionarioCreateInputDto inputDto);
    GfdFuncionarioUpdateOutputDto update(GfdFuncionarioUpdateInputDto inputDto);
    GfdFuncionarioGetByIdOutputDto getById(Integer id);

    void delete(GfdFuncionarioDeleteInputDto inputDto);
}
