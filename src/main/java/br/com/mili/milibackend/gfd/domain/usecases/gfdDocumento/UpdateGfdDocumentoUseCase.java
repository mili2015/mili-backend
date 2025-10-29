package br.com.mili.milibackend.gfd.domain.usecases.gfdDocumento;

import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoUpdateInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoUpdateOutputDto;

public interface UpdateGfdDocumentoUseCase {
    GfdDocumentoUpdateOutputDto execute(GfdDocumentoUpdateInputDto inputDto) ;

}