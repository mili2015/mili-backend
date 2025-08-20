package br.com.mili.milibackend.gfd.domain.usecases;


import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoGetAllOutputDto;
import br.com.mili.milibackend.shared.page.pagination.MyPage;

public interface GetAllGfdDocumentosUseCase {
    MyPage<GfdDocumentoGetAllOutputDto> execute(GfdDocumentoGetAllInputDto inputDto);
}
