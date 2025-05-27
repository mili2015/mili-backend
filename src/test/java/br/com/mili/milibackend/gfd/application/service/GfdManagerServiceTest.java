package br.com.mili.milibackend.gfd.application.service;

import br.com.mili.milibackend.fornecedor.application.dto.FornecedorGetByCodUsuarioInputDto;
import br.com.mili.milibackend.fornecedor.application.dto.FornecedorGetByCodUsuarioOutputDto;
import br.com.mili.milibackend.fornecedor.application.dto.FornecedorGetByIdOutputDto;
import br.com.mili.milibackend.fornecedor.application.dto.GfdTipoDocumentoGetByIdOutputDto;
import br.com.mili.milibackend.fornecedor.application.dto.gfdDocumento.FindLatestDocumentsGroupedByTipoAndFornecedorIdOutputDto;
import br.com.mili.milibackend.fornecedor.application.dto.gfdDocumento.GfdDocumentoCreateOutputDto;
import br.com.mili.milibackend.fornecedor.application.dto.gfdDocumento.GfdDocumentoGetAllInputDto;
import br.com.mili.milibackend.fornecedor.application.dto.gfdTipoDocumento.GfdTipoDocumentoGetAllOutputDto;
import br.com.mili.milibackend.fornecedor.domain.entity.Fornecedor;
import br.com.mili.milibackend.fornecedor.domain.entity.GfdDocumentoStatusEnum;
import br.com.mili.milibackend.fornecedor.domain.entity.GfdTipoDocumentoTipoEnum;
import br.com.mili.milibackend.fornecedor.domain.interfaces.service.IFornecedorService;
import br.com.mili.milibackend.fornecedor.domain.interfaces.service.IGfdTipoDocumentoService;
import br.com.mili.milibackend.gfd.application.dto.*;
import br.com.mili.milibackend.gfd.domain.interfaces.IGfdDocumentoService;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import br.com.mili.milibackend.shared.infra.aws.dto.AttachmentDto;
import br.com.mili.milibackend.shared.page.pagination.PageBaseImpl;
import org.apache.tika.Tika;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static br.com.mili.milibackend.gfd.adapter.exception.GfdCodeException.GFD_FORNECEDOR_NAO_ENCONTRADO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GfdManagerServiceTest {

    @InjectMocks
    private GfdManagerService gfdManagerService;

    @Mock
    private IFornecedorService fornecedorService;

    @Mock
    private IGfdTipoDocumentoService gfdTipoDocumentoService;

    @Mock
    private IGfdDocumentoService gfdDocumentoService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private Tika tika;

    @Test
    void test_VerifyFornecedor__deve_retornar_dados_corretos_quando_fornecedor_encontrado_por_id() {
        // Arrange
        GfdVerificarFornecedorInputDto inputDto = new GfdVerificarFornecedorInputDto();
        inputDto.setId(1);

        var fornecedor = new Fornecedor();
        fornecedor.setRazaoSocial("Teste");
        fornecedor.setContato("Contato");
        fornecedor.setEmail("email@teste.com");
        fornecedor.setCelular("123456789");

        var fornecedorGetByIdOutputDto = new FornecedorGetByIdOutputDto();
        fornecedorGetByIdOutputDto.setRazaoSocial("Teste");
        fornecedorGetByIdOutputDto.setContato("Contato");
        fornecedorGetByIdOutputDto.setEmail("email@teste.com");
        fornecedorGetByIdOutputDto.setCelular("123456789");

        when(fornecedorService.getById(1)).thenReturn(fornecedorGetByIdOutputDto);
        when(modelMapper.map(any(), eq(Fornecedor.class))).thenReturn(fornecedor);

        // Act
        GfdVerificarFornecedorOutputDto outputDto = gfdManagerService.verifyFornecedor(inputDto);

        // Assert
        assertTrue(outputDto.getRepresentanteCadastrado());
        assertEquals("Teste", outputDto.getRazaoSocial());
    }

    @Test
    void test_VerifyFornecedor__deve_lancar_not_found_exception_quando_fornecedor_nao_encontrado() {
        // Arrange
        GfdVerificarFornecedorInputDto inputDto = new GfdVerificarFornecedorInputDto();
        inputDto.setId(1);

        when(fornecedorService.getById(1)).thenReturn(null);

        // Act & Assert
        NotFoundException ex = assertThrows(NotFoundException.class, () -> gfdManagerService.verifyFornecedor(inputDto));
        assertEquals(GFD_FORNECEDOR_NAO_ENCONTRADO.getMensagem(), ex.getMessage());
        assertEquals(GFD_FORNECEDOR_NAO_ENCONTRADO.getCode(), ex.getCode());
    }

    @Test
    void test_VerifyFornecedor__deve_retornar_representante_nao_cadastrado_quando_fornecedor_encontrado_por_cod_usuario() {
        // Arrange
        GfdVerificarFornecedorInputDto inputDto = new GfdVerificarFornecedorInputDto();
        inputDto.setCodUsuario(123);

        var fornecedor = new Fornecedor();
        fornecedor.setRazaoSocial("Teste");

        var fornecedorGetByCodUsuarioOutputDto = new FornecedorGetByCodUsuarioOutputDto();
        fornecedorGetByCodUsuarioOutputDto.setRazaoSocial("Teste");


        when(fornecedorService.getByCodUsuario(any(FornecedorGetByCodUsuarioInputDto.class))).thenReturn(fornecedorGetByCodUsuarioOutputDto);
        when(modelMapper.map(any(), eq(Fornecedor.class))).thenReturn(fornecedor);

        // Act
        GfdVerificarFornecedorOutputDto outputDto = gfdManagerService.verifyFornecedor(inputDto);

        // Assert
        assertFalse(outputDto.getRepresentanteCadastrado());
        assertEquals("Teste", outputDto.getRazaoSocial());
    }

    @Test
    void test_VerifyDocumentos__deve_retornar_status_nao_enviado_e_outros_quando_sem_documentos() {
        // Arrange
        GfdVerificarDocumentosInputDto inputDto = new GfdVerificarDocumentosInputDto();
        inputDto.setId(1);

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setCodigo(1);

        var fornecedorGetByIdOutputDto = new FornecedorGetByIdOutputDto();
        fornecedor.setCodigo(1);

        when(fornecedorService.getById(1)).thenReturn(fornecedorGetByIdOutputDto);

        when(modelMapper.map(any(), eq(Fornecedor.class))).thenReturn(fornecedor);
        when(gfdDocumentoService.findLatestDocumentsGroupedByTipoAndFornecedorId(1)).thenReturn(Collections.emptyList());
        when(gfdTipoDocumentoService.getAll(any())).thenReturn(Arrays.asList(
                new GfdTipoDocumentoGetAllOutputDto(1, "Doc1", GfdTipoDocumentoTipoEnum.FORNECEDOR,30 ,true,true),
                new GfdTipoDocumentoGetAllOutputDto(2, "Doc2", GfdTipoDocumentoTipoEnum.FORNECEDOR,30 ,false,true)
        ));

        // Act
        List<GfdVerificarDocumentosOutputDto> outputDtoList = gfdManagerService.verifyDocumentos(inputDto);

        // Assert
        assertEquals(2, outputDtoList.size());
        assertEquals("NÃO ENVIADO", outputDtoList.get(1).getStatus());
        assertEquals("OUTROS", outputDtoList.get(0).getStatus());
    }

    @Test
    void test_VerifyDocumentos__deve_retornar_status_conforme_e_outros_quando_documentos_obrigatorios_existem() {
        // Arrange
        GfdVerificarDocumentosInputDto inputDto = new GfdVerificarDocumentosInputDto();
        inputDto.setId(1);

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setCodigo(1);

        FindLatestDocumentsGroupedByTipoAndFornecedorIdOutputDto doc = new FindLatestDocumentsGroupedByTipoAndFornecedorIdOutputDto();
        doc.setStatus(GfdDocumentoStatusEnum.CONFORME);
        doc.setGfdTipoDocumento(new FindLatestDocumentsGroupedByTipoAndFornecedorIdOutputDto.GfdTipoDocumentoDto(1, "Doc1", 30 ));

        var fornecedorGetByIdOutputDto = new FornecedorGetByIdOutputDto();
        fornecedor.setCodigo(1);

        when(fornecedorService.getById(1)).thenReturn(fornecedorGetByIdOutputDto);

        when(modelMapper.map(any(), eq(Fornecedor.class))).thenReturn(fornecedor);
        when(gfdDocumentoService.findLatestDocumentsGroupedByTipoAndFornecedorId(1)).thenReturn(Arrays.asList(doc));
        when(gfdTipoDocumentoService.getAll(any())).thenReturn(Arrays.asList(
                new GfdTipoDocumentoGetAllOutputDto(1, "Doc1", GfdTipoDocumentoTipoEnum.FORNECEDOR,30 ,true,true),
                new GfdTipoDocumentoGetAllOutputDto(2, "Doc2", GfdTipoDocumentoTipoEnum.FORNECEDOR,30 ,false,true)
        ));

        // Act
        List<GfdVerificarDocumentosOutputDto> outputDtoList = gfdManagerService.verifyDocumentos(inputDto);

        // Assert
        assertEquals(2, outputDtoList.size());
        assertEquals("CONFORME", outputDtoList.get(1).getStatus());
        assertEquals("OUTROS", outputDtoList.get(0).getStatus());
    }

    @Test
    void test_GetFornecedor__deve_retornar_fornecedor_quando_encontrado_por_id() {
        // Arrange
        GfdFornecedorGetInputDto inputDto = new GfdFornecedorGetInputDto();
        inputDto.setId(1);

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setRazaoSocial("Teste");

        var fornecedorGetByIdOutputDto = new FornecedorGetByIdOutputDto();
        fornecedor.setCodigo(1);

        when(fornecedorService.getById(1)).thenReturn(fornecedorGetByIdOutputDto);

        when(modelMapper.map(any(), eq(Fornecedor.class))).thenReturn(fornecedor);
        when(modelMapper.map(fornecedor, GfdFornecedorGetOutputDto.class)).thenReturn(new GfdFornecedorGetOutputDto());

        // Act
        GfdFornecedorGetOutputDto outputDto = gfdManagerService.getFornecedor(inputDto);

        // Assert
        assertNotNull(outputDto);
    }

    @Test
    void test_GetFornecedor__deve_lancar_not_found_exception_quando_fornecedor_nao_encontrado() {
        // Arrange
        GfdFornecedorGetInputDto inputDto = new GfdFornecedorGetInputDto();
        inputDto.setId(1);

        when(fornecedorService.getById(1)).thenReturn(null);

        // Act & Assert
        NotFoundException ex = assertThrows(NotFoundException.class, () ->  gfdManagerService.getFornecedor(inputDto));
        assertEquals(GFD_FORNECEDOR_NAO_ENCONTRADO.getMensagem(), ex.getMessage());
        assertEquals(GFD_FORNECEDOR_NAO_ENCONTRADO.getCode(), ex.getCode());
    }

    @Test
    void test_UploadDocumento__deve_realizar_upload_com_sucesso() {
        // Arrange
        GfdUploadDocumentoInputDto inputDto = new GfdUploadDocumentoInputDto();
        inputDto.setId(1);
        inputDto.setGfdTipoDocumento(new GfdUploadDocumentoInputDto.GfdTipoDocumentoDto(1));
        inputDto.setListGfdDocumento(Arrays.asList(
                new GfdUploadDocumentoInputDto.GfdDocumentoDto(new AttachmentDto("file.txt", "data"), LocalDate.now())
        ));

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setCodigo(1);

        var fornecedorGetByIdOutputDto = new FornecedorGetByIdOutputDto();
        fornecedor.setCodigo(1);

        when(fornecedorService.getById(1)).thenReturn(fornecedorGetByIdOutputDto);

        when(modelMapper.map(any(), eq(Fornecedor.class))).thenReturn(fornecedor);

        var gfdTipoDocumentoGetByIdOutputDto = new GfdTipoDocumentoGetByIdOutputDto();
        gfdTipoDocumentoGetByIdOutputDto.setId(1);

        when(gfdTipoDocumentoService.getById(1)).thenReturn(gfdTipoDocumentoGetByIdOutputDto);
        when(tika.detect(any(byte[].class))).thenReturn("text/plain");
        when(gfdDocumentoService.create(any())).thenReturn(new GfdDocumentoCreateOutputDto());
        when(modelMapper.map(any(), eq(GfdUploadDocumentoOutputDto.GfdTipoDocumentoDto.class))).thenReturn(new GfdUploadDocumentoOutputDto.GfdTipoDocumentoDto());

        // Act
        GfdUploadDocumentoOutputDto outputDto = gfdManagerService.uploadDocumento(inputDto);

        // Assert
        assertNotNull(outputDto);
        assertEquals(1, outputDto.getGfdTipoDocumento().size());
    }

    @Test
    void test_UploadDocumento__deve_lancar_not_found_exception_quando_fornecedor_nao_encontrado() {
        // Arrange
        GfdUploadDocumentoInputDto inputDto = new GfdUploadDocumentoInputDto();
        inputDto.setId(1);

        when(fornecedorService.getById(1)).thenReturn(null);

        // Act & Assert
        NotFoundException ex = assertThrows(NotFoundException.class, () -> gfdManagerService.uploadDocumento(inputDto));
        assertEquals(GFD_FORNECEDOR_NAO_ENCONTRADO.getMensagem(), ex.getMessage());
        assertEquals(GFD_FORNECEDOR_NAO_ENCONTRADO.getCode(), ex.getCode());
    }

    @Test
    void test_GetAllDocumentos__deve_retornar_lista_vazia_quando_sem_documentos() {
        // Arrange
        GfdDocumentosGetAllInputDto inputDto = new GfdDocumentosGetAllInputDto();
        inputDto.setId(1);

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setCodigo(1);

        var fornecedorGetByIdOutputDto = new FornecedorGetByIdOutputDto();
        fornecedorGetByIdOutputDto.setCodigo(1);

        when(fornecedorService.getById(1)).thenReturn(fornecedorGetByIdOutputDto);
        when(modelMapper.map(any(), eq(Fornecedor.class))).thenReturn(fornecedor);

        var gfdDocumentoGetAllInputDto = new GfdDocumentoGetAllInputDto();
        when(modelMapper.map(any(), eq(GfdDocumentoGetAllInputDto.class))).thenReturn(gfdDocumentoGetAllInputDto);

        when(gfdDocumentoService.getAll(any())).thenReturn(new PageBaseImpl<>(Collections.emptyList(), 1, 10, 0));

        // Act
        GfdDocumentosGetAllOutputDto outputDto = gfdManagerService.getAllDocumentos(inputDto);

        // Assert
        assertNotNull(outputDto);
        assertTrue(outputDto.getGfdDocumento().getContent().isEmpty());
    }

    @Test
    void test_GetAllDocumentos__deve_retornar_dados_corretos_quando_tipo_documento_especificado_e_sem_documentos() {
        // Arrange
        GfdDocumentosGetAllInputDto inputDto = new GfdDocumentosGetAllInputDto();
        inputDto.setId(1);
        inputDto.setTipoDocumentoId(1);

        FornecedorGetByIdOutputDto fornecedorGetByIdOutputDto = new FornecedorGetByIdOutputDto();
        fornecedorGetByIdOutputDto.setCodigo(1);

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setCodigo(1);

        GfdTipoDocumentoGetByIdOutputDto tipoDocumento = new GfdTipoDocumentoGetByIdOutputDto();
        tipoDocumento.setId(1);
        tipoDocumento.setNome("Doc1");

        when(fornecedorService.getById(1)).thenReturn(fornecedorGetByIdOutputDto);
        when(modelMapper.map(fornecedorGetByIdOutputDto, Fornecedor.class)).thenReturn(fornecedor);

        GfdDocumentoGetAllInputDto gfdDocumentoGetAllInputDto = new GfdDocumentoGetAllInputDto();
        when(modelMapper.map(inputDto, GfdDocumentoGetAllInputDto.class)).thenReturn(gfdDocumentoGetAllInputDto);

        when(gfdDocumentoService.getAll(any())).thenReturn(new PageBaseImpl<>(Collections.emptyList(), 1, 10, 0));
        when(gfdTipoDocumentoService.getById(1)).thenReturn(tipoDocumento);

        // Act
        GfdDocumentosGetAllOutputDto outputDto = gfdManagerService.getAllDocumentos(inputDto);

        // Assert
        assertNotNull(outputDto);
        assertNotNull(outputDto.getGfdDocumento());
        assertTrue(outputDto.getGfdDocumento().getContent().isEmpty(), "A lista de documentos deve estar vazia");
        assertNotNull(outputDto.getGfdTipoDocumento());
        assertEquals(1, outputDto.getGfdTipoDocumento().getId(), "O ID do tipo de documento deve ser 1");
        assertEquals("Doc1", outputDto.getGfdTipoDocumento().getNome(), "O nome do tipo de documento deve ser 'Doc1'");
    }
}