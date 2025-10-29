package br.com.mili.milibackend.gfd.domain.usecases.gfdDocumento;

import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoDeleteInputDto;

public interface DeleteGfdDocumentoUseCase {
    void execute(GfdDocumentoDeleteInputDto inputDto);
}
