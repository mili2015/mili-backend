package br.com.mili.milibackend.gfd.application.usecases.GfdDocumento;

import br.com.mili.milibackend.gfd.adapter.web.external.external.ReportExternalService;
import br.com.mili.milibackend.gfd.adapter.web.external.external.dto.ReportGfdDocumentoRequestDto;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.relatorio.GfdDocumentoRelatorioFiltroDto;
import br.com.mili.milibackend.gfd.domain.entity.GfdDocumentoStatusEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RelatorioGfdDocumentoUseCaseImplTest {

    @InjectMocks
    private RelatorioGfdDocumentoUseCaseImpl useCase;

    @Mock
    private ReportExternalService reportExternalService;

    @Test
    void test_execute_deve_chamar_service_com_where_por_ctfor_func_status() {
        // arrange
        var filtro = GfdDocumentoRelatorioFiltroDto.builder()
                .ctforCodigo(321)
                .funcionarioId(99)
                .status(GfdDocumentoStatusEnum.CONFORME)
                .build();

        byte[] expectedBytes = new byte[]{1,2,3};
        when(reportExternalService.getDocumento(any(ReportGfdDocumentoRequestDto.class)))
                .thenReturn(expectedBytes);

        ArgumentCaptor<ReportGfdDocumentoRequestDto> captor = ArgumentCaptor.forClass(ReportGfdDocumentoRequestDto.class);

        // act
        byte[] result = useCase.execute(filtro);

        // assert
        assertArrayEquals(expectedBytes, result);
        verify(reportExternalService).getDocumento(captor.capture());

        var sent = captor.getValue();
        assertNotNull(sent);
        assertNotNull(sent.getParamQuery());
        String where = sent.getParamQuery();
        assertTrue(where.contains(" AND B.CTFOR_CODIGO = 321"), "Deve conter filtro de fornecedor");
        assertTrue(where.contains(" AND A.ID_FUNCIONARIO = 99"), "Deve conter filtro de funcionario");
        assertTrue(where.contains(" AND A.STATUS = 'CONFORME'"), "Deve conter filtro de status");
    }

    @Test
    void test_execute_deve_chamar_service_sem_filtros_quando_null() {
        // arrange
        var filtro = GfdDocumentoRelatorioFiltroDto.builder().build();
        byte[] expectedBytes = new byte[]{10};
        when(reportExternalService.getDocumento(any(ReportGfdDocumentoRequestDto.class)))
                .thenReturn(expectedBytes);

        ArgumentCaptor<ReportGfdDocumentoRequestDto> captor = ArgumentCaptor.forClass(ReportGfdDocumentoRequestDto.class);

        // act
        byte[] result = useCase.execute(filtro);

        // assert
        assertArrayEquals(expectedBytes, result);
        verify(reportExternalService).getDocumento(captor.capture());
        var sent = captor.getValue();
        assertNotNull(sent);
        assertNotNull(sent.getParamQuery());
        assertEquals("", sent.getParamQuery());
    }
}
