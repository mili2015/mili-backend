package br.com.mili.milibackend.gfd.domain.usecases.gfdDocumento;

import br.com.mili.milibackend.gfd.application.dto.gfdDocumentoPeriodo.GfdDocumentoPeriodoCreateInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumentoPeriodo.GfdDocumentoPeriodoCreateOutputDto;

public interface CreateDocumentoPeriodoUseCase {
    GfdDocumentoPeriodoCreateOutputDto execute(GfdDocumentoPeriodoCreateInputDto inputDto);

}
