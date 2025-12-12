package br.com.mili.milibackend.gfd.domain.usecases.gfdDocumento;

import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.relatorio.GfdDocumentoRelatorioFiltroDto;

public interface RelatorioGfdDocumentoUseCase {
    byte[] execute(GfdDocumentoRelatorioFiltroDto filtro);
}
