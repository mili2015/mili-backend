package br.com.mili.milibackend.gfd.domain.usecases.gfdDocumento;

import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoCreateInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoCreateOutputDto;

public interface CreateDocumentoUseCase {
    GfdDocumentoCreateOutputDto execute(GfdDocumentoCreateInputDto inputDto);

}