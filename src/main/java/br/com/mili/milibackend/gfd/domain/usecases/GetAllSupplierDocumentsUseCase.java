package br.com.mili.milibackend.gfd.domain.usecases;

import br.com.mili.milibackend.gfd.application.dto.GfdMDocumentosGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.GfdMDocumentosGetAllOutputDto;

public interface GetAllSupplierDocumentsUseCase {
    GfdMDocumentosGetAllOutputDto execute(GfdMDocumentosGetAllInputDto inputDto);
}
