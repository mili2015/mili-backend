package br.com.mili.milibackend.gfd.application.usecases.GfdDocumento;

import br.com.mili.milibackend.fornecedor.domain.entity.Fornecedor;
import br.com.mili.milibackend.fornecedor.domain.usecases.GetFornecedorByCodOrIdUseCase;
import br.com.mili.milibackend.gfd.application.dto.manager.documentos.GfdMDocumentosGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.manager.documentos.GfdMDocumentosGetAllOutputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoGetAllOutputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdTipoDocumento.GfdTipoDocumentoGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdTipoDocumento.GfdTipoDocumentoGetAllOutputDto;
import br.com.mili.milibackend.gfd.domain.entity.*;
import br.com.mili.milibackend.gfd.domain.usecases.GetAllGfdDocumentosUseCase;
import br.com.mili.milibackend.gfd.domain.usecases.GetAllTipoDocumentoUseCase;
import br.com.mili.milibackend.gfd.infra.repository.GfdDocumentoPeriodoRepository;
import br.com.mili.milibackend.gfd.infra.repository.GfdTipoDocumentoRepository;
import br.com.mili.milibackend.shared.page.pagination.MyPage;
import br.com.mili.milibackend.shared.page.pagination.PageBaseImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllSupplierDocumentsUseCaseImplTest {

    @InjectMocks
    private GetAllSupplierDocumentsUseCaseImpl service;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private GfdDocumentoPeriodoRepository gfdDocumentoPeriodoRepository;

    @Mock
    private GfdTipoDocumentoRepository gfdTipoDocumentoRepository;

    @Mock
    private GetAllGfdDocumentosUseCase getAllGfdDocumentosUseCase;

    @Mock
    private GetAllTipoDocumentoUseCase getAllTipoDocumentoUseCase;

    @Mock
    private GetFornecedorByCodOrIdUseCase getFornecedorByCodOrIdUseCase;

    private Fornecedor fornecedor;
    private GfdTipoDocumento tipoDocumento;
    private GfdMDocumentosGetAllInputDto inputDto;
    private GfdDocumentoGetAllInputDto mappedInputDto;
    private MyPage<GfdDocumentoGetAllOutputDto> pageGfdDocumentos;
    private List<GfdTipoDocumentoGetAllOutputDto> tiposDocumento;

    @BeforeEach
    void setUp() {
        fornecedor = Fornecedor.builder()
                .codigo(123)
                .build();

        tipoDocumento = new GfdTipoDocumento();
        tipoDocumento.setId(1);
        tipoDocumento.setNome("Test Tipo");
        tipoDocumento.setDiasValidade(30);
        tipoDocumento.setClassificacao("GERAL");

        inputDto = new GfdMDocumentosGetAllInputDto();
        inputDto.setCodUsuario(123);
        inputDto.setTipoDocumentoId(1);

        mappedInputDto = new GfdDocumentoGetAllInputDto();
        mappedInputDto.setCtforCodigo(123);

        GfdDocumentoGetAllOutputDto documentoOutput = GfdDocumentoGetAllOutputDto.builder()
                .id(1)
                .ctforCodigo(123)
                .nomeArquivo("test.pdf")
                .build();

        pageGfdDocumentos = new PageBaseImpl<>(Collections.singletonList(documentoOutput), 1, 10, 1L);

        var tipoDto = new GfdTipoDocumentoGetAllOutputDto();
        tipoDto.setId(1);
        tipoDto.setNome("Test Tipo");
        tipoDto.setClassificacao("GERAL");
        tipoDto.setDiasValidade(180);

        var tipoDto2 = new GfdTipoDocumentoGetAllOutputDto();
        tipoDto2.setId(2);
        tipoDto2.setNome("Test Tipo 2");
        tipoDto2.setClassificacao("COMPETENCIA");
        tipoDto2.setDiasValidade(30);

        var tipoDto3 = new GfdTipoDocumentoGetAllOutputDto();
        tipoDto3.setId(3);
        tipoDto3.setNome("Test Tipo 3");
        tipoDto3.setClassificacao("GERAL");
        tipoDto3.setDiasValidade(30);

        tiposDocumento = Arrays.asList(tipoDto, tipoDto2, tipoDto3);
    }

    @Test
    void test_GetAllDocumentos__deve_calcular_navegacao_nextDoc_previousDoc_corretamente() {
        tipoDocumento = new GfdTipoDocumento();
        tipoDocumento.setId(tiposDocumento.get(1).getId());
        tipoDocumento.setNome(tiposDocumento.get(1).getNome());
        tipoDocumento.setDiasValidade(tiposDocumento.get(1).getDiasValidade());
        tipoDocumento.setClassificacao(tiposDocumento.get(1).getClassificacao());

        inputDto.setTipoDocumentoId(tiposDocumento.get(1).getId());

        when(getFornecedorByCodOrIdUseCase.execute(eq(inputDto.getCodUsuario()), eq(inputDto.getId()))).thenReturn(fornecedor);
        when(gfdTipoDocumentoRepository.findById(eq(inputDto.getTipoDocumentoId()))).thenReturn(Optional.of(tipoDocumento));
        when(modelMapper.map(eq(inputDto), eq(GfdDocumentoGetAllInputDto.class))).thenReturn(mappedInputDto);
        when(getAllGfdDocumentosUseCase.execute(any(GfdDocumentoGetAllInputDto.class))).thenReturn(pageGfdDocumentos);

        GfdMDocumentosGetAllOutputDto.GfdDocumentoDto mappedDto = new GfdMDocumentosGetAllOutputDto.GfdDocumentoDto();
        mappedDto.setId(1);
        mappedDto.setCtforCodigo(123);
        mappedDto.setNomeArquivo("test.pdf");
        when(modelMapper.map(any(GfdDocumentoGetAllOutputDto.class), eq(GfdMDocumentosGetAllOutputDto.GfdDocumentoDto.class))).thenReturn(mappedDto);

        GfdTipoDocumentoGetAllInputDto tipoInput = new GfdTipoDocumentoGetAllInputDto();
        tipoInput.setTipo(GfdTipoDocumentoTipoEnum.FORNECEDOR);
        when(getAllTipoDocumentoUseCase.execute(any(GfdTipoDocumentoGetAllInputDto.class))).thenReturn(tiposDocumento);

        // Act
        GfdMDocumentosGetAllOutputDto result = service.execute(inputDto);

        // Assert
        assertEquals(3, result.getNextDoc(), "nextDoc deve ser 3");
        assertEquals(1, result.getPreviousDoc(), "previousDoc deve ser 1");
    }



    @Test
    void test_deve_retornar_lista_de_documentos_quando_funcionario_estiver_vazio_e_classificao_for_geral() {
        when(getFornecedorByCodOrIdUseCase.execute(eq(inputDto.getCodUsuario()), eq(inputDto.getId()))).thenReturn(fornecedor);
        when(gfdTipoDocumentoRepository.findById(eq(inputDto.getTipoDocumentoId()))).thenReturn(Optional.of(tipoDocumento));
        when(modelMapper.map(eq(inputDto), eq(GfdDocumentoGetAllInputDto.class))).thenReturn(mappedInputDto);
        when(getAllGfdDocumentosUseCase.execute(any(GfdDocumentoGetAllInputDto.class))).thenReturn(pageGfdDocumentos);

        GfdMDocumentosGetAllOutputDto.GfdDocumentoDto mappedDto = new GfdMDocumentosGetAllOutputDto.GfdDocumentoDto();
        mappedDto.setId(1);
        mappedDto.setCtforCodigo(123);
        mappedDto.setNomeArquivo("test.pdf");
        when(modelMapper.map(any(GfdDocumentoGetAllOutputDto.class), eq(GfdMDocumentosGetAllOutputDto.GfdDocumentoDto.class))).thenReturn(mappedDto);

        GfdTipoDocumentoGetAllInputDto tipoInput = new GfdTipoDocumentoGetAllInputDto();
        tipoInput.setTipo(GfdTipoDocumentoTipoEnum.FORNECEDOR);
        when(getAllTipoDocumentoUseCase.execute(any(GfdTipoDocumentoGetAllInputDto.class))).thenReturn(tiposDocumento);

        // Act
        GfdMDocumentosGetAllOutputDto result = service.execute(inputDto);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getGfdDocumento().getContent().size());
        assertEquals("Test Tipo", result.getGfdTipoDocumento().getNome());
        verify(gfdDocumentoPeriodoRepository, never()).findByGfdDocumento_IdIn(anyList());
    }

    @Test
    void teste_deve_retornar_periodo_na_dto_quando_classificacao_for_competencia() {
        tipoDocumento.setClassificacao(GfdTipoDocumentoTipoClassificacaoEnum.COMPETENCIA.name());

        when(getFornecedorByCodOrIdUseCase.execute(eq(inputDto.getCodUsuario()), eq(inputDto.getId()))).thenReturn(fornecedor);
        when(gfdTipoDocumentoRepository.findById(eq(inputDto.getTipoDocumentoId()))).thenReturn(Optional.of(tipoDocumento));
        when(modelMapper.map(eq(inputDto), eq(GfdDocumentoGetAllInputDto.class))).thenReturn(mappedInputDto);
        when(getAllGfdDocumentosUseCase.execute(any(GfdDocumentoGetAllInputDto.class))).thenReturn(pageGfdDocumentos);

        var mappedDto = new GfdMDocumentosGetAllOutputDto.GfdDocumentoDto();
        mappedDto.setId(1);
        mappedDto.setCtforCodigo(123);
        mappedDto.setNomeArquivo("test.pdf");
        when(modelMapper.map(any(GfdDocumentoGetAllOutputDto.class), eq(GfdMDocumentosGetAllOutputDto.GfdDocumentoDto.class))).thenReturn(mappedDto);

        GfdDocumentoPeriodo periodo = new GfdDocumentoPeriodo();
        periodo.setId(1);
        periodo.setPeriodoFinal(LocalDate.now());
        periodo.setGfdDocumento(new GfdDocumento());
        periodo.getGfdDocumento().setId(1);

        when(gfdDocumentoPeriodoRepository.findByGfdDocumento_IdIn(eq(Collections.singletonList(1)))).thenReturn(Collections.singletonList(periodo));

        when(getAllTipoDocumentoUseCase.execute(any(GfdTipoDocumentoGetAllInputDto.class))).thenReturn(tiposDocumento);

        // Act
        GfdMDocumentosGetAllOutputDto result = service.execute(inputDto);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getGfdDocumento().getContent().get(0).getGfdDocumentoPeriodo());
        verify(gfdDocumentoPeriodoRepository).findByGfdDocumento_IdIn(anyList());
    }


    @Test
    void teste_deve_retornar_lista_de_documentos_vazia_quando_nao_houver_documentos() {
        MyPage<GfdDocumentoGetAllOutputDto> emptyPage = new PageBaseImpl<>(new ArrayList<>(), 1, 10, 0L);

        when(getFornecedorByCodOrIdUseCase.execute(eq(inputDto.getCodUsuario()), eq(inputDto.getId()))).thenReturn(fornecedor);
        when(gfdTipoDocumentoRepository.findById(eq(inputDto.getTipoDocumentoId()))).thenReturn(Optional.of(tipoDocumento));
        when(modelMapper.map(eq(inputDto), eq(GfdDocumentoGetAllInputDto.class))).thenReturn(mappedInputDto);
        when(getAllGfdDocumentosUseCase.execute(any(GfdDocumentoGetAllInputDto.class))).thenReturn(emptyPage);

        when(getAllTipoDocumentoUseCase.execute(any(GfdTipoDocumentoGetAllInputDto.class))).thenReturn(tiposDocumento);

        // Act
        GfdMDocumentosGetAllOutputDto result = service.execute(inputDto);

        // Assert
        assertNotNull(result);
        assertTrue(result.getGfdDocumento().getContent().isEmpty());
        assertEquals(0L, result.getGfdDocumento().getTotalElements());
    }
}