package br.com.mili.milibackend.gfd.domain.usecases.gfdCategoriaDocumento;

import br.com.mili.milibackend.gfd.application.dto.gfdCategoriaDocumento.GfdCategoriaDocumentoGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdCategoriaDocumento.GfdCategoriaDocumentoGetAllOutputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdCategoriaDocumento.GfdCategoriaDocumentoGetByIdOutputDto;

import java.util.List;

public interface GetByIdGfdCategoriaDocumentoUseCase {
    GfdCategoriaDocumentoGetByIdOutputDto execute(Integer id);

}
