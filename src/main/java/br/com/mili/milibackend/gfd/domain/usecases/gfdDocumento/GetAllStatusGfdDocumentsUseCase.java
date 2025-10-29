package br.com.mili.milibackend.gfd.domain.usecases.gfdDocumento;

import br.com.mili.milibackend.gfd.application.dto.manager.documentos.GfdMVerificarDocumentosInputDto;
import br.com.mili.milibackend.gfd.application.dto.manager.documentos.GfdMVerificarDocumentosOutputDto;

public interface GetAllStatusGfdDocumentsUseCase {
    GfdMVerificarDocumentosOutputDto execute(GfdMVerificarDocumentosInputDto inputDto);
}
