package br.com.mili.milibackend.gfd.domain.usecases;

import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoUpdateInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoUpdateOutputDto;

public interface UpdateGfdDocumentoUseCase {
    GfdDocumentoUpdateOutputDto execute(GfdDocumentoUpdateInputDto inputDto) ;

}