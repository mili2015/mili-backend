package br.com.mili.milibackend.gfd.domain.usecases;

import br.com.mili.milibackend.gfd.application.dto.GfdMUploadDocumentoInputDto;
import br.com.mili.milibackend.gfd.application.dto.GfdMUploadDocumentoOutputDto;

public interface UploadGfdDocumentoUseCase {
    GfdMUploadDocumentoOutputDto execute(GfdMUploadDocumentoInputDto inputDto);
}
