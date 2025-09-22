package br.com.mili.milibackend.gfd.domain.usecases.gfdCategoriaDocumento;

import br.com.mili.milibackend.gfd.application.dto.gfdCategoriaDocumento.GfdCategoriaDocumentoCreateInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdCategoriaDocumento.GfdCategoriaDocumentoCreateOutputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdCategoriaDocumento.GfdCategoriaDocumentoGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdCategoriaDocumento.GfdCategoriaDocumentoGetAllOutputDto;

import java.util.List;

public interface CreateGfdCategoriaDocumentoUseCase {
    GfdCategoriaDocumentoCreateOutputDto execute(GfdCategoriaDocumentoCreateInputDto inputDto);

}
