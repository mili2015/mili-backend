package br.com.mili.milibackend.gfd.domain.usecases;

import br.com.mili.milibackend.gfd.application.dto.manager.documentos.GfdMUploadDocumentoInputDto;
import br.com.mili.milibackend.gfd.application.dto.manager.documentos.GfdMUploadDocumentoOutputDto;

public interface UploadGfdDocumentoUseCase {
    GfdMUploadDocumentoOutputDto execute(GfdMUploadDocumentoInputDto inputDto);
}
