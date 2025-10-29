package br.com.mili.milibackend.gfd.application.usecases;

import br.com.mili.milibackend.fornecedor.domain.entity.Fornecedor;
import br.com.mili.milibackend.fornecedor.domain.usecases.GetFornecedorByCodOrIdUseCase;
import br.com.mili.milibackend.fornecedor.infra.repository.fornecedorRepository.FornecedorRepository;
import br.com.mili.milibackend.gfd.application.dto.manager.documentos.GfdMUploadDocumentoInputDto;
import br.com.mili.milibackend.gfd.application.dto.manager.documentos.GfdMUploadDocumentoOutputDto;
import br.com.mili.milibackend.gfd.application.dto.fileprocess.DocumentoFileData;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoCreateOutputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumentoPeriodo.GfdDocumentoPeriodoCreateOutputDto;
import br.com.mili.milibackend.gfd.application.usecases.GfdDocumento.UploadGfdDocumentoUseCaseImpl;
import br.com.mili.milibackend.gfd.domain.entity.GfdTipoDocumento;
import br.com.mili.milibackend.gfd.domain.entity.GfdTipoDocumentoTipoEnum;
import br.com.mili.milibackend.gfd.domain.interfaces.FileProcessingService;
import br.com.mili.milibackend.gfd.domain.usecases.gfdDocumento.CreateDocumentoPeriodoUseCase;
import br.com.mili.milibackend.gfd.domain.usecases.gfdDocumento.CreateDocumentoUseCase;
import br.com.mili.milibackend.gfd.infra.repository.GfdTipoDocumentoRepository;
import br.com.mili.milibackend.shared.exception.types.BadRequestException;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import br.com.mili.milibackend.shared.infra.aws.IS3Service;
import br.com.mili.milibackend.shared.infra.aws.StorageFolderEnum;
import br.com.mili.milibackend.shared.infra.aws.dto.AttachmentDto;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static br.com.mili.milibackend.gfd.adapter.exception.GfdMCodeException.GFD_FORNECEDOR_NAO_ENCONTRADO;
import static br.com.mili.milibackend.gfd.adapter.exception.GfdMCodeException.GFD_TIPO_DOCUMENTO_FUNCIONARIO_BAD_REQUEST;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UploadGfdDocumentoUseCaseImplTest {

    @Mock
    private FornecedorRepository fornecedorRepository;

    @Mock
    private GfdTipoDocumentoRepository gfdTipoDocumentoRepository;

    @Mock
    private CreateDocumentoUseCase createDocumentoUseCase;

    @Mock
    private FileProcessingService fileProcessingService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private GetFornecedorByCodOrIdUseCase getFornecedorByCodOrIdUseCase;

    @Mock
    private CreateDocumentoPeriodoUseCase createDocumentoPeriodoUseCase;

    @Mock
    private Gson gson;

    @Mock
    private IS3Service s3Service;

    @InjectMocks
    private UploadGfdDocumentoUseCaseImpl useCase;

    private GfdMUploadDocumentoInputDto inputDto;
    private Fornecedor fornecedor;
    private GfdTipoDocumento tipoDocumento;

    @BeforeEach
    void setup() {
        inputDto = new GfdMUploadDocumentoInputDto();
        inputDto.setUsuario("usuarioTeste");
        inputDto.setCodUsuario(1);
        inputDto.setId(null);

        var file = new AttachmentDto("base64encoded", "documento.pdf");
        var docDto = new GfdMUploadDocumentoInputDto.GfdDocumentoDto(file, LocalDate.now(), LocalDate.now().plusDays(10));
        inputDto.setGfdDocumento(docDto);

        var tipoDocDto = new GfdMUploadDocumentoInputDto.GfdTipoDocumentoDto(10);
        inputDto.setGfdTipoDocumento(tipoDocDto);

        var funcionarioDto = new GfdMUploadDocumentoInputDto.GfdFuncionarioDto(100);
        inputDto.setFuncionario(funcionarioDto);
        inputDto.setAnalista(true);
        fornecedor = new Fornecedor();
        fornecedor.setCodigo(123);
        fornecedor.setAceiteLgpd(1);

        tipoDocumento = new GfdTipoDocumento();
        tipoDocumento.setId(10);
        tipoDocumento.setTipo(GfdTipoDocumentoTipoEnum.FORNECEDOR);
        tipoDocumento.setClassificacao("classificacao");
        tipoDocumento.setDiasValidade(30);
    }

    @Test
    void teste_deve_realizar_upload_documento_com_sucesso() {
        inputDto.setId(1);
        tipoDocumento.setTipo(GfdTipoDocumentoTipoEnum.FUNCIONARIO_CLT);

        when(getFornecedorByCodOrIdUseCase.execute(1, inputDto.getId(), true)).thenReturn(fornecedor);
        when(gfdTipoDocumentoRepository.findById(10)).thenReturn(Optional.of(tipoDocumento));

        var documentoFileData = new DocumentoFileData("bytes" .getBytes(), "application/pdf", "documento.pdf", 1234);
        when(fileProcessingService.processFile(anyString(), anyString(), any())).thenReturn(documentoFileData);

        var createOutputDto = new GfdDocumentoCreateOutputDto();
        createOutputDto.setId(10);
        when(createDocumentoUseCase.execute(any())).thenReturn(createOutputDto);

        var createPeriodoOutputDto = new GfdDocumentoPeriodoCreateOutputDto();
        createPeriodoOutputDto.setId(10);
        when(createDocumentoPeriodoUseCase.execute(any())).thenReturn(createPeriodoOutputDto);

        var outputDtoMock = new GfdMUploadDocumentoOutputDto(10, "Nome", "FUNCIONARIO", 30, true, true);
        when(modelMapper.map(eq(createOutputDto), eq(GfdMUploadDocumentoOutputDto.class))).thenReturn(outputDtoMock);

        when(gson.toJson(any(AttachmentDto.class))).thenReturn("{json}");

        GfdMUploadDocumentoOutputDto result = useCase.execute(inputDto);

        verify(s3Service).upload(eq(StorageFolderEnum.GFD), eq("{json}"));

        assertThat(result).isNotNull();
    }

    @Test
    void teste_deve_lancar_excecao_quando_forncedor_nao_encontrado() {
        when(getFornecedorByCodOrIdUseCase.execute(1, inputDto.getId(), true)).thenReturn(fornecedor);

        NotFoundException ex = assertThrows(NotFoundException.class, () -> useCase.execute(inputDto));
        assertEquals(GFD_FORNECEDOR_NAO_ENCONTRADO.getMensagem(), ex.getMessage());
        assertEquals(GFD_FORNECEDOR_NAO_ENCONTRADO.getCode(), ex.getCode());

    }

    @Test
    void teste_deve_lancar_excecao_quando_tipo_documento_invalido_para_funcionario() {
        tipoDocumento.setTipo(GfdTipoDocumentoTipoEnum.FORNECEDOR);
        when(getFornecedorByCodOrIdUseCase.execute(1, inputDto.getId(), true)).thenReturn(fornecedor);
        when(gfdTipoDocumentoRepository.findById(10)).thenReturn(Optional.of(tipoDocumento));

        BadRequestException ex = assertThrows(BadRequestException.class, () -> useCase.execute(inputDto));
        assertEquals(GFD_TIPO_DOCUMENTO_FUNCIONARIO_BAD_REQUEST.getMensagem(), ex.getMessage());
        assertEquals(GFD_TIPO_DOCUMENTO_FUNCIONARIO_BAD_REQUEST.getCode(), ex.getCode());

    }
}
