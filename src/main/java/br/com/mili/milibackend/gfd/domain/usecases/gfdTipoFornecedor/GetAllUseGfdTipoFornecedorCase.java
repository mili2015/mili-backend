package br.com.mili.milibackend.gfd.domain.usecases.gfdTipoFornecedor;

import br.com.mili.milibackend.gfd.application.dto.gfdTipoFornecedor.GfdTipoFornecedorGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdTipoFornecedor.GfdTipoFornecedorGetAllOutputDto;

import java.util.List;

public interface GetAllUseGfdTipoFornecedorCase {
    List<GfdTipoFornecedorGetAllOutputDto> execute(GfdTipoFornecedorGetAllInputDto inputDto);

}
