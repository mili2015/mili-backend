package br.com.mili.milibackend.gfd.domain.usecases.gfdDocumento;

import br.com.mili.milibackend.gfd.application.dto.gfdTipoDocumento.GfdTipoDocumentoGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdTipoDocumento.GfdTipoDocumentoGetAllOutputDto;

import java.util.List;

public interface GetAllTipoDocumentoUseCase {
    List<GfdTipoDocumentoGetAllOutputDto> execute(GfdTipoDocumentoGetAllInputDto inputDto);
}
