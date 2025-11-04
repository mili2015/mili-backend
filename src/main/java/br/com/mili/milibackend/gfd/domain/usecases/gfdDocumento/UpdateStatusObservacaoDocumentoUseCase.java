package br.com.mili.milibackend.gfd.domain.usecases.gfdDocumento;

import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoUpdateStatusObservacaoInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoUpdateStatusObservacaoOutputDto;

public interface UpdateStatusObservacaoDocumentoUseCase {
    GfdDocumentoUpdateStatusObservacaoOutputDto execute(GfdDocumentoUpdateStatusObservacaoInputDto inputDto);
}
