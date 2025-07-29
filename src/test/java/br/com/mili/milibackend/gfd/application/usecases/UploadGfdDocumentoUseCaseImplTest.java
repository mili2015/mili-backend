package br.com.mili.milibackend.gfd.application.usecases;

import br.com.mili.milibackend.fornecedor.domain.entity.Fornecedor;
import br.com.mili.milibackend.fornecedor.infra.repository.fornecedorRepository.FornecedorRepository;
import br.com.mili.milibackend.gfd.application.dto.GfdMUploadDocumentoInputDto;
import br.com.mili.milibackend.gfd.application.dto.GfdMUploadDocumentoOutputDto;
import br.com.mili.milibackend.gfd.application.dto.fileprocess.DocumentoFileData;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoCreateOutputDto;
import br.com.mili.milibackend.gfd.domain.entity.GfdDocumentoStatusEnum;
import br.com.mili.milibackend.gfd.domain.entity.GfdTipoDocumento;
import br.com.mili.milibackend.gfd.domain.entity.GfdTipoDocumentoTipoEnum;
import br.com.mili.milibackend.gfd.domain.interfaces.FileProcessingService;
import br.com.mili.milibackend.gfd.domain.usecases.CreateDocumentoUseCase;
import br.com.mili.milibackend.gfd.infra.repository.GfdTipoDocumentoRepository;
import br.com.mili.milibackend.shared.exception.types.BadRequestException;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import br.com.mili.milibackend.shared.infra.aws.dto.AttachmentDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
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
        inputDto.setId(null); // codUsuario será usado

        var file = new AttachmentDto("base64encoded", "documento.pdf");
        var docDto = new GfdMUploadDocumentoInputDto.GfdDocumentoDto(file, LocalDate.now(), LocalDate.now().plusDays(10));
        inputDto.setListGfdDocumento(List.of(docDto));

        var tipoDocDto = new GfdMUploadDocumentoInputDto.GfdTipoDocumentoDto(10);
        inputDto.setGfdTipoDocumento(tipoDocDto);

        var funcionarioDto = new GfdMUploadDocumentoInputDto.GfdFuncionarioDto(100);
        inputDto.setFuncionario(funcionarioDto);

        fornecedor = new Fornecedor();
        fornecedor.setCodigo(123);

        tipoDocumento = new GfdTipoDocumento();
        tipoDocumento.setId(10);
        tipoDocumento.setTipo(GfdTipoDocumentoTipoEnum.FORNECEDOR);
    }

    @Test
    void teste_deve_realizar_upload_documento_com_sucesso() {
        when(fornecedorRepository.findByCodUsuario(1)).thenReturn(Optional.of(fornecedor));
        when(gfdTipoDocumentoRepository.findById(10)).thenReturn(Optional.of(tipoDocumento));

        var documentoFileData = new DocumentoFileData("bytes".getBytes(), "application/pdf", "documento.pdf", 1234);
        when(fileProcessingService.processFile(anyString(), anyString())).thenReturn(documentoFileData);

        var createOutputDto = new GfdDocumentoCreateOutputDto();
        when(createDocumentoUseCase.execute(any())).thenReturn(createOutputDto);

        var outputDtoMock = new GfdMUploadDocumentoOutputDto.GfdTipoDocumentoDto(10, "Nome", "FUNCIONARIO", 30, true, true);
        when(modelMapper.map(eq(createOutputDto), eq(GfdMUploadDocumentoOutputDto.GfdTipoDocumentoDto.class))).thenReturn(outputDtoMock);

        GfdMUploadDocumentoOutputDto result = useCase.execute(inputDto);

        assertThat(result).isNotNull();
        assertThat(result.getGfdTipoDocumento()).hasSize(1);
        assertThat(result.getGfdTipoDocumento().get(0).getId()).isEqualTo(10);
    }

    @Test
    void teste_deve_lancar_excecao_quando_forncedor_nao_encontrado() {
        when(fornecedorRepository.findByCodUsuario(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(inputDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Fornecedor não encontrado");
    }

    @Test
    void teste_deve_lancar_excecao_quando_tipo_documento_invalido_para_funcionario() {
        tipoDocumento.setTipo(GfdTipoDocumentoTipoEnum.FORNECEDOR);
        when(fornecedorRepository.findByCodUsuario(1)).thenReturn(Optional.of(fornecedor));
        when(gfdTipoDocumentoRepository.findById(10)).thenReturn(Optional.of(tipoDocumento));

        assertThatThrownBy(() -> useCase.execute(inputDto))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("tipo de documento não é permitido para funcionário");
    }
}
