package br.com.mili.milibackend.gfd.application.usecases.GfdDocumento;

import br.com.mili.milibackend.gfd.adapter.web.external.external.ReportExternalService;
import br.com.mili.milibackend.gfd.adapter.web.external.external.dto.ReportGfdDocumentoRequestDto;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.relatorio.GfdDocumentoRelatorioFiltroDto;
import br.com.mili.milibackend.gfd.domain.usecases.gfdDocumento.RelatorioGfdDocumentoUseCase;
import org.springframework.stereotype.Service;

@Service
public class RelatorioGfdDocumentoUseCaseImpl implements RelatorioGfdDocumentoUseCase {

    private final ReportExternalService reportExternalService;

    public RelatorioGfdDocumentoUseCaseImpl(ReportExternalService reportExternalService) {
        this.reportExternalService = reportExternalService;
    }

    @Override
    public byte[] execute(GfdDocumentoRelatorioFiltroDto filtro) {
        StringBuilder where = new StringBuilder();

        if (filtro.getCtforCodigo() != null) {
            where.append(" AND B.CTFOR_CODIGO = ").append(filtro.getCtforCodigo());
        }

        if (filtro.getFuncionarioId() != null) {
            where.append(" AND A.ID_FUNCIONARIO = ").append(filtro.getFuncionarioId());
        }

        if (filtro.getStatus() != null) {
            where.append(" AND A.STATUS = '").append(filtro.getStatus().toJson()).append("'");
        }

        ReportGfdDocumentoRequestDto req = ReportGfdDocumentoRequestDto.builder()
                .paramQuery(where.toString())
                .build();

        return reportExternalService.getDocumento(req);
    }
}
