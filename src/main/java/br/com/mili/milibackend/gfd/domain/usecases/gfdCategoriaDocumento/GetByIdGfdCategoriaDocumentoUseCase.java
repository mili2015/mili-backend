package br.com.mili.milibackend.gfd.domain.usecases.gfdCategoriaDocumento;

import br.com.mili.milibackend.gfd.application.dto.gfdCategoriaDocumento.GfdCategoriaDocumentoGetByIdOutputDto;

public interface GetByIdGfdCategoriaDocumentoUseCase {
    GfdCategoriaDocumentoGetByIdOutputDto execute(Integer id);

}
