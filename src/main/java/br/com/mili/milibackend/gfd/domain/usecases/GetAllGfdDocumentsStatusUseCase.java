package br.com.mili.milibackend.gfd.domain.usecases;

import br.com.mili.milibackend.gfd.application.dto.GfdMVerificarDocumentosInputDto;
import br.com.mili.milibackend.gfd.application.dto.GfdMVerificarDocumentosOutputDto;

public interface GetAllGfdDocumentsStatusUseCase {
    GfdMVerificarDocumentosOutputDto execute(GfdMVerificarDocumentosInputDto inputDto);
}
