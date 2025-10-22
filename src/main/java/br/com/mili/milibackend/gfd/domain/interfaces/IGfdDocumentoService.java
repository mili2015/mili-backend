package br.com.mili.milibackend.gfd.domain.interfaces;

import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.*;
import br.com.mili.milibackend.shared.page.pagination.MyPage;

import java.time.LocalDate;
import java.util.List;

public interface IGfdDocumentoService {
    List<FindLatestDocumentsGroupedByTipoAndFornecedorIdOutputDto> findLatestDocumentsGroupedByTipoAndFornecedorId(Integer fornecedorId, Integer idFuncionario, LocalDate periodo, String setor);
    GfdDocumentoDownloadOutputDto download(GfdDocumentoDownloadInputDto inputDto);
}
