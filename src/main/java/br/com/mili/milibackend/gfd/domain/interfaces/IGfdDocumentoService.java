package br.com.mili.milibackend.gfd.domain.interfaces;

import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.*;
import br.com.mili.milibackend.shared.page.pagination.MyPage;

import java.util.List;

public interface IGfdDocumentoService {
    MyPage<GfdDocumentoGetAllOutputDto> getAll(GfdDocumentoGetAllInputDto inputDto);
    GfdDocumentoUpdateOutputDto update (GfdDocumentoUpdateInputDto inputDto);
    void delete (GfdDocumentoDeleteInputDto inputDto);
    GfdDocumentoDownloadOutputDto download(GfdDocumentoDownloadInputDto inputDto);
    List<FindLatestDocumentsGroupedByTipoAndFornecedorIdOutputDto> findLatestDocumentsGroupedByTipoAndFornecedorId(Integer fornecedorId, Integer idFuncionario);
}
