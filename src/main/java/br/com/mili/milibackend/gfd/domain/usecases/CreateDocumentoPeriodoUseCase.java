package br.com.mili.milibackend.gfd.domain.usecases;

import br.com.mili.milibackend.gfd.application.dto.gfdDocumentoPeriodo.GfdDocumentoPeriodoCreateInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumentoPeriodo.GfdDocumentoPeriodoCreateOutputDto;

import java.util.List;

public interface CreateDocumentoPeriodoUseCase {
    GfdDocumentoPeriodoCreateOutputDto execute(GfdDocumentoPeriodoCreateInputDto inputDto);

}
