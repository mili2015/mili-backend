package br.com.mili.milibackend.gfd.domain.usecases;

import br.com.mili.milibackend.gfd.application.dto.manager.documentos.GfdMDocumentosGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.manager.documentos.GfdMDocumentosGetAllOutputDto;

public interface GetAllSupplierDocumentsUseCase {
    GfdMDocumentosGetAllOutputDto execute(GfdMDocumentosGetAllInputDto inputDto);
}
