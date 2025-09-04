package br.com.mili.milibackend.gfd.domain.interfaces;

import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.*;

public interface IGfdFuncionarioService {
    GfdFuncionarioGetByIdOutputDto getById(Integer id);

    void delete(GfdFuncionarioDeleteInputDto inputDto);
}
