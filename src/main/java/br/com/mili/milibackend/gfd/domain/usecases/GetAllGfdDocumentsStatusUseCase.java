package br.com.mili.milibackend.gfd.domain.usecases;

import br.com.mili.milibackend.gfd.application.dto.manager.documentos.GfdMVerificarDocumentosInputDto;
import br.com.mili.milibackend.gfd.application.dto.manager.documentos.GfdMVerificarDocumentosOutputDto;

public interface GetAllGfdDocumentsStatusUseCase {
    GfdMVerificarDocumentosOutputDto execute(GfdMVerificarDocumentosInputDto inputDto);
}
