package br.com.mili.milibackend.fornecedor.domain.interfaces.service;

import br.com.mili.milibackend.fornecedor.application.dto.*;
import br.com.mili.milibackend.shared.page.pagination.MyPage;

public interface IFornecedorService {
    public FornecedorGetByCodUsuarioOutputDto getByCodUsuario(FornecedorGetByCodUsuarioInputDto inputDto);
    public FornecedorMeusDadosUpdateOutputDto updateMeusDados(FornecedorMeusDadosUpdateInputDto inputDto);
    MyPage<FornecedorGetAllOutputDto> getAll(FornecedorGetAllInputDto inputDto);
    FornecedorGetByIdOutputDto getById(Integer id);
}
