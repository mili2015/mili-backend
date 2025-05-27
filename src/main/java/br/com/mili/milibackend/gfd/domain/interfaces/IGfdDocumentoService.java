package br.com.mili.milibackend.gfd.domain.interfaces;

import br.com.mili.milibackend.fornecedor.application.dto.gfdDocumento.*;
import br.com.mili.milibackend.shared.page.pagination.MyPage;

import java.util.List;

public interface IGfdDocumentoService {
    GfdDocumentoCreateOutputDto create(GfdDocumentoCreateInputDto inputDto);
    MyPage<GfdDocumentoGetAllOutputDto> getAll(GfdDocumentoGetAllInputDto inputDto);
    GfdDocumentoUpdateOutputDto update (GfdDocumentoUpdateInputDto inputDto);
    GfdDocumentoDownloadOutputDto download(GfdDocumentoDownloadInputDto inputDto);
    List<FindLatestDocumentsGroupedByTipoAndFornecedorIdOutputDto> findLatestDocumentsGroupedByTipoAndFornecedorId(Integer fornecedorId);
}
