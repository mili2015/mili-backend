package br.com.mili.milibackend.gfd.application.service;

import br.com.mili.milibackend.envioEmail.domain.entity.EnvioEmail;
import br.com.mili.milibackend.envioEmail.domain.interfaces.IEnvioEmailService;
import br.com.mili.milibackend.fornecedor.application.dto.FornecedorGetByCodUsuarioInputDto;
import br.com.mili.milibackend.fornecedor.application.dto.FornecedorGetByCodUsuarioOutputDto;
import br.com.mili.milibackend.fornecedor.application.dto.FornecedorGetByIdOutputDto;
import br.com.mili.milibackend.gfd.application.dto.GfdTipoDocumentoGetByIdOutputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.*;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.*;
import br.com.mili.milibackend.gfd.application.dto.gfdTipoDocumento.GfdTipoDocumentoGetAllOutputDto;
import br.com.mili.milibackend.fornecedor.domain.entity.*;
import br.com.mili.milibackend.fornecedor.domain.interfaces.service.IFornecedorService;
import br.com.mili.milibackend.gfd.domain.interfaces.IGfdFuncionarioService;
import br.com.mili.milibackend.gfd.domain.interfaces.IGfdTipoDocumentoService;
import br.com.mili.milibackend.gfd.application.dto.*;
import br.com.mili.milibackend.gfd.domain.entity.GfdDocumentoStatusEnum;
import br.com.mili.milibackend.gfd.domain.entity.GfdFuncionarioTipoContratacaoEnum;
import br.com.mili.milibackend.gfd.domain.entity.GfdTipoDocumentoTipoEnum;
import br.com.mili.milibackend.gfd.domain.interfaces.IGfdDocumentoService;
import br.com.mili.milibackend.shared.exception.types.ForbiddenException;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import br.com.mili.milibackend.shared.infra.aws.dto.AttachmentDto;
import br.com.mili.milibackend.shared.page.pagination.PageBaseImpl;
import org.apache.tika.Tika;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static br.com.mili.milibackend.gfd.adapter.exception.GfdMCodeException.GFD_FORNECEDOR_NAO_ENCONTRADO;
import static br.com.mili.milibackend.gfd.adapter.exception.GfdMCodeException.GFD_FUNCIONARIO_SEM_PERMISSAO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GfdManagerServiceTest {

    @InjectMocks
    private GfdManagerService gfdManagerService;

    @Mock
    private IFornecedorService fornecedorService;

    @Mock
    private IEnvioEmailService envioEmailService;

    @Mock
    private IGfdFuncionarioService gfdFuncionarioService;

    @Mock
    private IGfdTipoDocumentoService gfdTipoDocumentoService;

    @Mock
    private IGfdDocumentoService gfdDocumentoService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private Tika tika;


    private static GfdDocumentoGetAllOutputDto buildDocumento() {
        return GfdDocumentoGetAllOutputDto.builder()
                .id(1)
                .ctforCodigo(123)
                .tipoDocumento("Nota Fiscal")
                .nomeArquivo("nf-123456.pdf")
                .nomeArquivoPath("/documentos/fornecedor/123/nf-123456.pdf")
                .tamanhoArquivo(204800)
                .dataEmissao(LocalDate.of(2025, 1, 16))
                .dataCadastro(LocalDate.of(2025, 7, 15))
                .dataValidade(LocalDate.of(2025, 1, 15))
                .tipoArquivo("application/pdf")
                .observacao("Documento emitido pela transportadora")
                .status(GfdDocumentoStatusEnum.ENVIADO)
                .gfdTipoDocumento(GfdDocumentoGetAllOutputDto.GfdTipoDocumentoDto.builder()
                        .id(10)
                        .nome("Nota Fiscal")
                        .diasValidade(180)
                        .build())
                .funcionario(GfdDocumentoGetAllOutputDto.FuncionarioDto.builder()
                        .id(1)
                        .nome("Teste")
                        .cpf("12345678909")
                        .build())
                .build();
    }


    @Test
    void test_VerifyFornecedor__deve_retornar_dados_corretos_quando_fornecedor_encontrado_por_id() {
        // Arrange
        GfdMVerificarFornecedorInputDto inputDto = new GfdMVerificarFornecedorInputDto();
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
        GfdMVerificarFornecedorOutputDto outputDto = gfdManagerService.verifyFornecedor(inputDto);

        // Assert
        assertTrue(outputDto.getRepresentanteCadastrado());
        assertEquals("Teste", outputDto.getRazaoSocial());
    }

    @Test
    void test_VerifyFornecedor__deve_lancar_not_found_exception_quando_fornecedor_nao_encontrado() {
        // Arrange
        GfdMVerificarFornecedorInputDto inputDto = new GfdMVerificarFornecedorInputDto();
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
        GfdMVerificarFornecedorInputDto inputDto = new GfdMVerificarFornecedorInputDto();
        inputDto.setCodUsuario(123);

        var fornecedor = new Fornecedor();
        fornecedor.setRazaoSocial("Teste");

        var fornecedorGetByCodUsuarioOutputDto = new FornecedorGetByCodUsuarioOutputDto();
        fornecedorGetByCodUsuarioOutputDto.setRazaoSocial("Teste");


        when(fornecedorService.getByCodUsuario(any(FornecedorGetByCodUsuarioInputDto.class))).thenReturn(fornecedorGetByCodUsuarioOutputDto);
        when(modelMapper.map(any(), eq(Fornecedor.class))).thenReturn(fornecedor);

        // Act
        GfdMVerificarFornecedorOutputDto outputDto = gfdManagerService.verifyFornecedor(inputDto);

        // Assert
        assertFalse(outputDto.getRepresentanteCadastrado());
        assertEquals("Teste", outputDto.getRazaoSocial());
    }

    @Test
    void test_VerifyDocumentos__deve_retornar_status_nao_enviado_e_outros_quando_sem_documentos() {
        // Arrange
        GfdMVerificarDocumentosInputDto inputDto = new GfdMVerificarDocumentosInputDto();
        inputDto.setId(1);

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setCodigo(1);

        var fornecedorGetByIdOutputDto = new FornecedorGetByIdOutputDto();
        fornecedor.setCodigo(1);

        when(fornecedorService.getById(1)).thenReturn(fornecedorGetByIdOutputDto);

        when(modelMapper.map(any(), eq(Fornecedor.class))).thenReturn(fornecedor);
        when(gfdDocumentoService.findLatestDocumentsGroupedByTipoAndFornecedorId(1, null)).thenReturn(Collections.emptyList());
        when(gfdTipoDocumentoService.getAll(any())).thenReturn(Arrays.asList(
                new GfdTipoDocumentoGetAllOutputDto(1, "Doc1", GfdTipoDocumentoTipoEnum.FORNECEDOR, 30, true, true),
                new GfdTipoDocumentoGetAllOutputDto(2, "Doc2", GfdTipoDocumentoTipoEnum.FORNECEDOR, 30, false, true)
        ));

        // Act
        GfdMVerificarDocumentosOutputDto outputDtoList = gfdManagerService.verifyDocumentos(inputDto);

        // Assert
        assertEquals(2, outputDtoList.getDocumentos().size());
        assertEquals("NÃO ENVIADO", outputDtoList.getDocumentos().get(1).getStatus());
        assertEquals("OUTROS", outputDtoList.getDocumentos().get(0).getStatus());
    }

    @Test
    void test_VerifyDocumentos__deve_retornar_status_conforme_e_outros_quando_documentos_obrigatorios_existem() {
        // Arrange
        GfdMVerificarDocumentosInputDto inputDto = new GfdMVerificarDocumentosInputDto();
        inputDto.setId(1);

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setCodigo(1);

        FindLatestDocumentsGroupedByTipoAndFornecedorIdOutputDto doc = new FindLatestDocumentsGroupedByTipoAndFornecedorIdOutputDto();
        doc.setStatus(GfdDocumentoStatusEnum.CONFORME);
        doc.setGfdTipoDocumento(new FindLatestDocumentsGroupedByTipoAndFornecedorIdOutputDto.GfdTipoDocumentoDto(1, "Doc1", 30));

        var fornecedorGetByIdOutputDto = new FornecedorGetByIdOutputDto();
        fornecedor.setCodigo(1);

        when(fornecedorService.getById(1)).thenReturn(fornecedorGetByIdOutputDto);

        when(modelMapper.map(any(), eq(Fornecedor.class))).thenReturn(fornecedor);
        when(gfdDocumentoService.findLatestDocumentsGroupedByTipoAndFornecedorId(1, null)).thenReturn(Arrays.asList(doc));
        when(gfdTipoDocumentoService.getAll(any())).thenReturn(Arrays.asList(
                new GfdTipoDocumentoGetAllOutputDto(1, "Doc1", GfdTipoDocumentoTipoEnum.FORNECEDOR, 30, true, true),
                new GfdTipoDocumentoGetAllOutputDto(2, "Doc2", GfdTipoDocumentoTipoEnum.FORNECEDOR, 30, false, true)
        ));

        // Act
        GfdMVerificarDocumentosOutputDto outputDtoList = gfdManagerService.verifyDocumentos(inputDto);

        // Assert
        assertEquals(2, outputDtoList.getDocumentos().size());
        assertEquals("CONFORME", outputDtoList.getDocumentos().get(1).getStatus());
        assertEquals("OUTROS", outputDtoList.getDocumentos().get(0).getStatus());
    }

    @Test
    void test_GetFornecedor__deve_retornar_fornecedor_quando_encontrado_por_id() {
        // Arrange
        GfdMFornecedorGetInputDto inputDto = new GfdMFornecedorGetInputDto();
        inputDto.setId(1);

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setRazaoSocial("Teste");

        var fornecedorGetByIdOutputDto = new FornecedorGetByIdOutputDto();
        fornecedor.setCodigo(1);

        when(fornecedorService.getById(1)).thenReturn(fornecedorGetByIdOutputDto);

        when(modelMapper.map(any(), eq(Fornecedor.class))).thenReturn(fornecedor);
        when(modelMapper.map(fornecedor, GfdMFornecedorGetOutputDto.class)).thenReturn(new GfdMFornecedorGetOutputDto());

        // Act
        GfdMFornecedorGetOutputDto outputDto = gfdManagerService.getFornecedor(inputDto);

        // Assert
        assertNotNull(outputDto);
    }

    @Test
    void test_GetFornecedor__deve_lancar_not_found_exception_quando_fornecedor_nao_encontrado() {
        // Arrange
        GfdMFornecedorGetInputDto inputDto = new GfdMFornecedorGetInputDto();
        inputDto.setId(1);

        when(fornecedorService.getById(1)).thenReturn(null);

        // Act & Assert
        NotFoundException ex = assertThrows(NotFoundException.class, () -> gfdManagerService.getFornecedor(inputDto));
        assertEquals(GFD_FORNECEDOR_NAO_ENCONTRADO.getMensagem(), ex.getMessage());
        assertEquals(GFD_FORNECEDOR_NAO_ENCONTRADO.getCode(), ex.getCode());
    }

    @Test
    void test_UploadDocumento__deve_realizar_upload_com_sucesso() {
        // Arrange
        GfdMUploadDocumentoInputDto inputDto = new GfdMUploadDocumentoInputDto();
        inputDto.setId(1);
        inputDto.setGfdTipoDocumento(new GfdMUploadDocumentoInputDto.GfdTipoDocumentoDto(1));


        inputDto.setListGfdDocumento(Arrays.asList(
                new GfdMUploadDocumentoInputDto.GfdDocumentoDto(new AttachmentDto("file.txt", "data"), LocalDate.now(), LocalDate.now().plusDays(1))
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
        when(modelMapper.map(any(), eq(GfdMUploadDocumentoOutputDto.GfdTipoDocumentoDto.class))).thenReturn(new GfdMUploadDocumentoOutputDto.GfdTipoDocumentoDto());

        // Act
        GfdMUploadDocumentoOutputDto outputDto = gfdManagerService.uploadDocumento(inputDto);

        // Assert
        assertNotNull(outputDto);
        assertEquals(1, outputDto.getGfdTipoDocumento().size());
    }

    @Test
    void test_UploadDocumento__deve_lancar_not_found_exception_quando_fornecedor_nao_encontrado() {
        // Arrange
        GfdMUploadDocumentoInputDto inputDto = new GfdMUploadDocumentoInputDto();
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
        GfdMDocumentosGetAllInputDto inputDto = new GfdMDocumentosGetAllInputDto();
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
        GfdMDocumentosGetAllOutputDto outputDto = gfdManagerService.getAllDocumentos(inputDto);

        // Assert
        assertNotNull(outputDto);
        assertTrue(outputDto.getGfdDocumento().getContent().isEmpty());
    }

    @Test
    void test_GetAllDocumentos__deve_retornar_dados_corretos_quando_tipo_documento_especificado_e_sem_documentos() {
        // Arrange
        GfdMDocumentosGetAllInputDto inputDto = new GfdMDocumentosGetAllInputDto();
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
        GfdMDocumentosGetAllOutputDto outputDto = gfdManagerService.getAllDocumentos(inputDto);

        // Assert
        assertNotNull(outputDto);
        assertNotNull(outputDto.getGfdDocumento());
        assertTrue(outputDto.getGfdDocumento().getContent().isEmpty(), "A lista de documentos deve estar vazia");
        assertNotNull(outputDto.getGfdTipoDocumento());
        assertEquals(1, outputDto.getGfdTipoDocumento().getId(), "O ID do tipo de documento deve ser 1");
        assertEquals("Doc1", outputDto.getGfdTipoDocumento().getNome(), "O nome do tipo de documento deve ser 'Doc1'");
    }

    @Test
    void test_GetAllDocumentos__deve_retornar_documentos_funcionario_quando_tipo_documento_especificado() {
        // Arrange
        GfdMDocumentosGetAllInputDto inputDto = new GfdMDocumentosGetAllInputDto();
        inputDto.setId(1);
        inputDto.setTipoDocumentoId(1);

        var funcionario = new GfdMDocumentosGetAllInputDto.FuncionarioDto();
        funcionario.setId(1);
        inputDto.setFuncionario(funcionario);

        var fornecedorGetByIdOutputDto = new FornecedorGetByIdOutputDto();
        fornecedorGetByIdOutputDto.setCodigo(1);

        var fornecedor = new Fornecedor();
        fornecedor.setCodigo(1);

        var tipoDocumento = new GfdTipoDocumentoGetByIdOutputDto();
        tipoDocumento.setId(1);
        tipoDocumento.setNome("Doc1");
        tipoDocumento.setDiasValidade(180);

        var documento1 = buildDocumento();
        documento1.setId(1);
        var documento2 = buildDocumento();
        documento2.setId(2);

        var documentos = Arrays.asList(documento1, documento2);

        // Mock do funcionario
        var funcionarioDto = new GfdFuncionarioGetByIdOutputDto();
        funcionarioDto.setId(funcionario.getId());
        funcionarioDto.setTipoContratacao(GfdFuncionarioTipoContratacaoEnum.CLT.getDescricao());

        when(gfdFuncionarioService.getById(any())).thenReturn(funcionarioDto);

        // Mock do fornecedor
        when(fornecedorService.getById(1)).thenReturn(fornecedorGetByIdOutputDto);
        when(modelMapper.map(fornecedorGetByIdOutputDto, Fornecedor.class)).thenReturn(fornecedor);


        // Mock do input mapeado
        var gfdDocumentoGetAllInputDto = new GfdDocumentoGetAllInputDto();
        when(modelMapper.map(inputDto, GfdDocumentoGetAllInputDto.class)).thenReturn(gfdDocumentoGetAllInputDto);

        // Mock do retorno de documentos
        when(gfdDocumentoService.getAll(any())).thenReturn(new PageBaseImpl<>(documentos, 1, 10, 2));

        GfdMDocumentosGetAllOutputDto.GfdDocumentoDto.GfdTipoDocumentoDto tipoDto = new GfdMDocumentosGetAllOutputDto.GfdDocumentoDto.GfdTipoDocumentoDto();
        tipoDto.setId(1);
        tipoDto.setNome("Doc1");
        tipoDto.setDiasValidade(180);

        var documentoDto1 = new GfdMDocumentosGetAllOutputDto.GfdDocumentoDto();
        documentoDto1.setId(1);
        documentoDto1.setNomeArquivo("nf-123456.pdf");
        documentoDto1.setGfdTipoDocumento(tipoDto);

        var documentoDto2 = new GfdMDocumentosGetAllOutputDto.GfdDocumentoDto();
        documentoDto2.setId(2);
        documentoDto2.setNomeArquivo("nf-654321.pdf");
        documentoDto2.setGfdTipoDocumento(tipoDto);

        when(modelMapper.map(eq(documento1), eq(GfdMDocumentosGetAllOutputDto.GfdDocumentoDto.class))).thenReturn(documentoDto1);
        when(modelMapper.map(eq(documento2), eq(GfdMDocumentosGetAllOutputDto.GfdDocumentoDto.class))).thenReturn(documentoDto2);

        // Act
        var outputDto = gfdManagerService.getAllDocumentos(inputDto);

        // Assert
        assertNotNull(outputDto);
        assertNotNull(outputDto.getGfdDocumento());
        assertEquals(2, outputDto.getGfdDocumento().getContent().size(), "Deve retornar 2 documentos");

        assertEquals(1, outputDto.getGfdDocumento().getContent().get(0).getId());
        assertEquals(2, outputDto.getGfdDocumento().getContent().get(1).getId());

        assertNotNull(outputDto.getGfdTipoDocumento());
        assertEquals(1, outputDto.getGfdTipoDocumento().getId(), "ID do tipo de documento deve ser 1");
        assertEquals("Doc1", outputDto.getGfdTipoDocumento().getNome(), "Nome deve ser 'Doc1'");
        assertEquals(180, outputDto.getGfdTipoDocumento().getDiasValidade(), "Dias de validade deve ser 180");
    }

    @Test
    void test_GetAllDocumentos__deve_calcular_navegacao_nextDoc_previousDoc_corretamente() {
        // Arrange
        var inputDto = new GfdMDocumentosGetAllInputDto();
        inputDto.setId(1);

        inputDto.setTipoDocumentoId(2);

        // Coloca funcionario para pegar lista de tipo FUNCIONARIO
        var funcionario = new GfdMDocumentosGetAllInputDto.FuncionarioDto();
        funcionario.setId(1);
        inputDto.setFuncionario(funcionario);

        var fornecedorGetByIdOutputDto = new FornecedorGetByIdOutputDto();
        fornecedorGetByIdOutputDto.setCodigo(1);

        var fornecedor = new Fornecedor();
        fornecedor.setCodigo(1);

        var tiposDocumento = Arrays.asList(
                new GfdTipoDocumentoGetAllOutputDto(1, "Tipo 1", GfdTipoDocumentoTipoEnum.FUNCIONARIO_CLT, 100, true, true),
                new GfdTipoDocumentoGetAllOutputDto(2, "Tipo 2", GfdTipoDocumentoTipoEnum.FUNCIONARIO_CLT, 100, true, true),
                new GfdTipoDocumentoGetAllOutputDto(3, "Tipo 3", GfdTipoDocumentoTipoEnum.FUNCIONARIO_CLT, 100, true, true)
        );

        // Mocks
        when(fornecedorService.getById(1)).thenReturn(fornecedorGetByIdOutputDto);
        when(modelMapper.map(fornecedorGetByIdOutputDto, Fornecedor.class)).thenReturn(fornecedor);

        var gfdDocumentoGetAllInputDto = new GfdDocumentoGetAllInputDto();
        when(modelMapper.map(inputDto, GfdDocumentoGetAllInputDto.class)).thenReturn(gfdDocumentoGetAllInputDto);

        when(gfdDocumentoService.getAll(any())).thenReturn(new PageBaseImpl<>(Collections.emptyList(), 1, 10, 0));

        var tipoDocumentoAtual = new GfdTipoDocumentoGetByIdOutputDto();
        tipoDocumentoAtual.setId(2);
        tipoDocumentoAtual.setNome("Tipo 2");
        tipoDocumentoAtual.setDiasValidade(120);
        when(gfdTipoDocumentoService.getById(2)).thenReturn(tipoDocumentoAtual);


        var serviceSpy = Mockito.spy(gfdManagerService);
        doReturn(tiposDocumento).when(serviceSpy).getFornecedorTipoDocumentos(funcionario.getId());

        // Act
        var outputDto = serviceSpy.getAllDocumentos(inputDto);

        // Assert
        assertNotNull(outputDto);

        // Espera que previousDoc seja o id anterior (1) e nextDoc seja o próximo (3)
        assertEquals(3, outputDto.getNextDoc(), "nextDoc deve ser 3");
        assertEquals(1, outputDto.getPreviousDoc(), "previousDoc deve ser 1");
    }

    @Test
    void test_CreateFuncionario__deve_criar_funcionario_corretamente_quando_dados_validos() {
        // Arrange
        Integer codUsuario = 10;
        Integer codFornecedor = 20;

        // DTO de entrada com dados básicos
        GfdMFuncionarioCreateInputDto.GfdFuncionarioDto.FornecedorDto fornecedorInputDto =
                new GfdMFuncionarioCreateInputDto.GfdFuncionarioDto.FornecedorDto(codFornecedor);

        GfdMFuncionarioCreateInputDto.GfdFuncionarioDto funcionarioInputDto =
                new GfdMFuncionarioCreateInputDto.GfdFuncionarioDto();
        funcionarioInputDto.setNome("João");
        funcionarioInputDto.setFornecedor(fornecedorInputDto);

        GfdMFuncionarioCreateInputDto inputDto = new GfdMFuncionarioCreateInputDto();
        inputDto.setCodUsuario(codUsuario);
        inputDto.setFuncionario(funcionarioInputDto);

        // Fornecedor mapeado
        FornecedorGetByIdOutputDto fornecedorDto = new FornecedorGetByIdOutputDto();
        fornecedorDto.setCodigo(codFornecedor);
        fornecedorDto.setCodUsuario(codUsuario);

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setCodigo(codFornecedor);
        fornecedor.setCodUsuario(codUsuario);

        // Funcionário de entrada para o service de domínio
        GfdFuncionarioCreateInputDto funcionarioCreateInputDto = new GfdFuncionarioCreateInputDto();
        funcionarioCreateInputDto.setFornecedor(new GfdFuncionarioCreateInputDto.FornecedorDto(codFornecedor));
        funcionarioCreateInputDto.setNome("João");

        // Funcionário criado pelo service
        GfdFuncionarioCreateOutputDto funcionarioCriado = new GfdFuncionarioCreateOutputDto();
        funcionarioCriado.setId(1);
        funcionarioCriado.setNome("João");

        // DTO mapeado de saída
        GfdMFuncionarioCreateOutputDto.GfdFuncionarioDto funcionarioOutputDto =
                new GfdMFuncionarioCreateOutputDto.GfdFuncionarioDto();
        funcionarioOutputDto.setId(1);
        funcionarioOutputDto.setNome("João");

        // Mocks
        when(fornecedorService.getById(codFornecedor)).thenReturn(fornecedorDto);
        when(modelMapper.map(fornecedorDto, Fornecedor.class)).thenReturn(fornecedor);
        when(modelMapper.map(funcionarioInputDto, GfdFuncionarioCreateInputDto.class)).thenReturn(new GfdFuncionarioCreateInputDto());
        when(gfdFuncionarioService.create(any())).thenReturn(funcionarioCriado);
        when(modelMapper.map(funcionarioCriado, GfdMFuncionarioCreateOutputDto.GfdFuncionarioDto.class)).thenReturn(funcionarioOutputDto);

        // Act
        var output = gfdManagerService.createFuncionario(inputDto);

        // Assert
        assertNotNull(output);
        assertNotNull(output.getFuncionario());
        assertEquals(1, output.getFuncionario().getId());
        assertEquals("João", output.getFuncionario().getNome());
    }
    @Test
    void test_CreateFuncionario_deve_lancar_ForbiddenException_quando_usuario_sem_permissao() {
        // Arrange
        Integer codUsuario = 100;
        Integer codFornecedor = 200;

        var fornecedorInputDto = new GfdMFuncionarioCreateInputDto.GfdFuncionarioDto.FornecedorDto(codFornecedor);
        var funcDto = new GfdMFuncionarioCreateInputDto.GfdFuncionarioDto();
        funcDto.setFornecedor(fornecedorInputDto);
        funcDto.setNome("Maria");

        var inputDto = new GfdMFuncionarioCreateInputDto();
        inputDto.setCodUsuario(codUsuario);
        inputDto.setFuncionario(funcDto);

        var fornecedorGetByIdDto = new FornecedorGetByIdOutputDto();
        fornecedorGetByIdDto.setCodigo(codFornecedor);
        fornecedorGetByIdDto.setCodUsuario(333);
        when(fornecedorService.getById(codFornecedor)).thenReturn(fornecedorGetByIdDto);

        var fornecedorEntity = new Fornecedor();
        fornecedorEntity.setCodigo(codFornecedor);
        fornecedorEntity.setCodUsuario(333);
        when(modelMapper.map(fornecedorGetByIdDto, Fornecedor.class))
                .thenReturn(fornecedorEntity);

        // Act
        var ex = assertThrows(ForbiddenException.class, () -> gfdManagerService.createFuncionario(inputDto));
        assertEquals(GFD_FUNCIONARIO_SEM_PERMISSAO.getMensagem(), ex.getMessage());
        assertEquals(GFD_FUNCIONARIO_SEM_PERMISSAO.getCode(), ex.getCode());
    }

    @Test
    void test_UpdateFuncionario__deve_atualizar_funcionario_corretamente_quando_dados_validos() {
        // Arrange
        Integer codUsuario = 100;
        Integer codFornecedor = 200;
        Integer idFuncionario = 5;

        var fornecedorInputDto = new GfdMFuncionarioUpdateInputDto.GfdFuncionarioDto.FornecedorDto(codFornecedor);
        var funcDto = new GfdMFuncionarioUpdateInputDto.GfdFuncionarioDto();
        funcDto.setId(idFuncionario);
        funcDto.setFornecedor(fornecedorInputDto);
        funcDto.setNome("Maria");

        var inputDto = new GfdMFuncionarioUpdateInputDto();
        inputDto.setCodUsuario(codUsuario);
        inputDto.setFuncionario(funcDto);

        var fornecedorGetByIdDto = new FornecedorGetByIdOutputDto();
        fornecedorGetByIdDto.setCodigo(codFornecedor);
        fornecedorGetByIdDto.setCodUsuario(codUsuario);
        when(fornecedorService.getById(codFornecedor)).thenReturn(fornecedorGetByIdDto);

        var fornecedorEntity = new Fornecedor();
        fornecedorEntity.setCodigo(codFornecedor);
        fornecedorEntity.setCodUsuario(codUsuario);
        when(modelMapper.map(fornecedorGetByIdDto, Fornecedor.class))
                .thenReturn(fornecedorEntity);

        var domainUpdateInput = new GfdFuncionarioUpdateInputDto();
        domainUpdateInput.setId(idFuncionario);
        domainUpdateInput.setNome("Maria");
        when(modelMapper.map(funcDto, GfdFuncionarioUpdateInputDto.class))
                .thenReturn(domainUpdateInput);

        var domainOutputDto = new GfdFuncionarioUpdateOutputDto();
        domainOutputDto.setId(idFuncionario);
        domainOutputDto.setNome("Maria Atualizada");
        when(gfdFuncionarioService.update(domainUpdateInput))
                .thenReturn(domainOutputDto);

        var outFuncDto = new GfdMFuncionarioUpdateOutputDto.GfdFuncionarioDto();
        outFuncDto.setId(idFuncionario);
        outFuncDto.setNome("Maria Atualizada");
        when(modelMapper.map(domainOutputDto, GfdMFuncionarioUpdateOutputDto.GfdFuncionarioDto.class))
                .thenReturn(outFuncDto);

        // Act
        var result = gfdManagerService.updateFuncionario(inputDto);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getFuncionario());
        assertEquals(idFuncionario, result.getFuncionario().getId());
        assertEquals("Maria Atualizada", result.getFuncionario().getNome());
    }

    @Test
    void test_UpdateFuncionario_deve_lancar_ForbiddenException_quando_usuario_sem_permissao() {
        // Arrange
        Integer codUsuario = 100;
        Integer codFornecedor = 200;
        Integer idFuncionario = 5;

        var fornecedorInputDto = new GfdMFuncionarioUpdateInputDto.GfdFuncionarioDto.FornecedorDto(codFornecedor);
        var funcDto = new GfdMFuncionarioUpdateInputDto.GfdFuncionarioDto();
        funcDto.setId(idFuncionario);
        funcDto.setFornecedor(fornecedorInputDto);
        funcDto.setNome("Maria");

        var inputDto = new GfdMFuncionarioUpdateInputDto();
        inputDto.setCodUsuario(codUsuario);
        inputDto.setFuncionario(funcDto);

        var fornecedorGetByIdDto = new FornecedorGetByIdOutputDto();
        fornecedorGetByIdDto.setCodigo(codFornecedor);
        fornecedorGetByIdDto.setCodUsuario(333);
        when(fornecedorService.getById(codFornecedor)).thenReturn(fornecedorGetByIdDto);

        var fornecedorEntity = new Fornecedor();
        fornecedorEntity.setCodigo(codFornecedor);
        fornecedorEntity.setCodUsuario(333);
        when(modelMapper.map(fornecedorGetByIdDto, Fornecedor.class))
                .thenReturn(fornecedorEntity);

        // Act
        var ex = assertThrows(ForbiddenException.class, () -> gfdManagerService.updateFuncionario(inputDto));
        assertEquals(GFD_FUNCIONARIO_SEM_PERMISSAO.getMensagem(), ex.getMessage());
        assertEquals(GFD_FUNCIONARIO_SEM_PERMISSAO.getCode(), ex.getCode());
    }

    @Test
    void test_getFuncionario_deve_lancar_ForbiddenException_quando_usuario_sem_permissao() {
        // Arrange
        Integer codUsuario = 100;
        Integer codFornecedor = 200;
        Integer idFuncionario = 5;

        var fornecedorInputDto = new GfdMFuncionarioGetInputDto.GfdFuncionarioDto.FornecedorDto(codFornecedor);
        var funcDto = new GfdMFuncionarioGetInputDto.GfdFuncionarioDto();
        funcDto.setId(idFuncionario);
        funcDto.setFornecedor(fornecedorInputDto);
        funcDto.setNome("Maria");

        var inputDto = new GfdMFuncionarioGetInputDto();
        inputDto.setCodUsuario(codUsuario);
        inputDto.setFuncionario(funcDto);

        var fornecedorGetByIdDto = new FornecedorGetByIdOutputDto();
        fornecedorGetByIdDto.setCodigo(codFornecedor);
        fornecedorGetByIdDto.setCodUsuario(333);
        when(fornecedorService.getById(codFornecedor)).thenReturn(fornecedorGetByIdDto);

        var fornecedorEntity = new Fornecedor();
        fornecedorEntity.setCodigo(codFornecedor);
        fornecedorEntity.setCodUsuario(333);
        when(modelMapper.map(fornecedorGetByIdDto, Fornecedor.class))
                .thenReturn(fornecedorEntity);

        // Act
        var ex = assertThrows(ForbiddenException.class, () -> gfdManagerService.getFuncionario(inputDto));
        assertEquals(GFD_FUNCIONARIO_SEM_PERMISSAO.getMensagem(), ex.getMessage());
        assertEquals(GFD_FUNCIONARIO_SEM_PERMISSAO.getCode(), ex.getCode());
    }

    @Test
    void test_getFuncionario_deve_retornar_funcionario_quando_input_valido() {
        // Arrange
        Integer codUsuario    = 100;
        Integer codFornecedor = 200;
        Integer idFuncionario = 5;

        var fornecedorInputDto = new GfdMFuncionarioGetInputDto.GfdFuncionarioDto.FornecedorDto(codFornecedor);
        var funcDto = new GfdMFuncionarioGetInputDto.GfdFuncionarioDto();
        funcDto.setId(idFuncionario);
        funcDto.setFornecedor(fornecedorInputDto);
        funcDto.setNome("Maria");

        var inputDto = new GfdMFuncionarioGetInputDto();
        inputDto.setCodUsuario(codUsuario);
        inputDto.setFuncionario(funcDto);

        var fornecedorGetByIdDto = new FornecedorGetByIdOutputDto();
        fornecedorGetByIdDto.setCodigo(codFornecedor);
        fornecedorGetByIdDto.setCodUsuario(codUsuario);

        when(fornecedorService.getById(codFornecedor)).thenReturn(fornecedorGetByIdDto);

        var fornecedorEntity = new Fornecedor();
        fornecedorEntity.setCodigo(codFornecedor);
        fornecedorEntity.setCodUsuario(codUsuario);

        when(modelMapper.map(fornecedorGetByIdDto, Fornecedor.class))
                .thenReturn(fornecedorEntity);

        var funcionarioEntity = new GfdFuncionarioGetByIdOutputDto();
        funcionarioEntity.setId(idFuncionario);
        funcionarioEntity.setNome("Maria");
        when(gfdFuncionarioService.getById(idFuncionario))
                .thenReturn(funcionarioEntity);

        var funcionarioDto = new GfdMFuncionarioGetOutputDto.GfdFuncionarioDto();
        funcionarioDto.setId(idFuncionario);
        funcionarioDto.setNome("Maria");
        when(modelMapper.map(funcionarioEntity, GfdMFuncionarioGetOutputDto.GfdFuncionarioDto.class))
                .thenReturn(funcionarioDto);

        // Act
        var result = gfdManagerService.getFuncionario(inputDto);

        // Assert
        assertNotNull(result, "O retorno não deve ser nulo");
        assertNotNull(result.getFuncionario(), "O campo 'funcionario' não deve ser nulo");
        assertEquals(idFuncionario, result.getFuncionario().getId(), "O ID deve bater com o esperado");
        assertEquals("Maria",       result.getFuncionario().getNome(), "O nome deve bater com o esperado");
    }

    @Test
    void test_deleteFuncionario_deve_deletar_funcionario_quando_input_valido() {
        // Arrange
        Integer codUsuario    = 100;
        Integer codFornecedor = 200;
        Integer idFuncionario = 5;

        var fornecedorInputDto = new GfdMFuncionarioDeleteInputDto.GfdFuncionarioDto.FornecedorDto(codFornecedor);
        var funcDto = new GfdMFuncionarioDeleteInputDto.GfdFuncionarioDto();
        funcDto.setId(idFuncionario);
        funcDto.setFornecedor(fornecedorInputDto);

        var inputDto = new GfdMFuncionarioDeleteInputDto();
        inputDto.setCodUsuario(codUsuario);
        inputDto.setFuncionario(funcDto);

        var fornecedorGetByIdDto = new FornecedorGetByIdOutputDto();
        fornecedorGetByIdDto.setCodigo(codFornecedor);
        fornecedorGetByIdDto.setCodUsuario(codUsuario);
        when(fornecedorService.getById(codFornecedor)).thenReturn(fornecedorGetByIdDto);

        var fornecedorEntity = new Fornecedor();
        fornecedorEntity.setCodigo(codFornecedor);
        fornecedorEntity.setCodUsuario(codUsuario);
        when(modelMapper.map(fornecedorGetByIdDto, Fornecedor.class))
                .thenReturn(fornecedorEntity);

        var domainDeleteDto = new GfdFuncionarioDeleteInputDto();
        domainDeleteDto.setId(idFuncionario);
        when(modelMapper.map(funcDto, GfdFuncionarioDeleteInputDto.class))
                .thenReturn(domainDeleteDto);

        // Act
        gfdManagerService.deleteFuncionario(inputDto);

        // Assert
        verify(gfdFuncionarioService, times(1)).delete(domainDeleteDto);
    }

    @Test
    void test_deleteFuncionario_deve_lancar_ForbiddenException_quando_usuario_sem_permissao() {
        // Arrange
        Integer codUsuario    = 100;
        Integer codFornecedor = 200;
        Integer idFuncionario = 5;

        var fornecedorInputDto = new GfdMFuncionarioDeleteInputDto.GfdFuncionarioDto.FornecedorDto(codFornecedor);
        var funcDto = new GfdMFuncionarioDeleteInputDto.GfdFuncionarioDto();
        funcDto.setId(idFuncionario);
        funcDto.setFornecedor(fornecedorInputDto);

        var inputDto = new GfdMFuncionarioDeleteInputDto();
        inputDto.setCodUsuario(codUsuario);
        inputDto.setFuncionario(funcDto);

        var fornecedorGetByIdDto = new FornecedorGetByIdOutputDto();
        fornecedorGetByIdDto.setCodigo(codFornecedor);
        fornecedorGetByIdDto.setCodUsuario(999); // diferente
        when(fornecedorService.getById(codFornecedor)).thenReturn(fornecedorGetByIdDto);

        var fornecedorEntity = new Fornecedor();
        fornecedorEntity.setCodigo(codFornecedor);
        fornecedorEntity.setCodUsuario(999);
        when(modelMapper.map(fornecedorGetByIdDto, Fornecedor.class))
                .thenReturn(fornecedorEntity);

        // Act & Assert
        var ex = assertThrows(ForbiddenException.class, () ->
                gfdManagerService.deleteFuncionario(inputDto)
        );
        assertEquals(
                GFD_FUNCIONARIO_SEM_PERMISSAO.getMensagem(),
                ex.getMessage()
        );
    }


    @Test
    void test_updateDocumento_deve_enviar_email_quando_status_nao_conforme() {
        // arrange
        final Integer usuarioLogadoId = 123;
        final Integer fornecedorExistenteId = 77;
        final Integer documentoId = 10;
        final String statusNaoConforme = "NÃO CONFORME";
        final String emailFornecedor = "fornecedor@mili.com";

        Fornecedor fornecedor;
        fornecedor = new Fornecedor();
        fornecedor.setCodigo(fornecedorExistenteId);
        fornecedor.setCodUsuario(usuarioLogadoId);
        fornecedor.setEmail(emailFornecedor);

        var input = new GfdMDocumentoUpdateInputDto();
        input.setCodUsuario(usuarioLogadoId);
        input.setFornecedor(new GfdMDocumentoUpdateInputDto.FornecedorDto(fornecedorExistenteId));

        var docInput = new GfdMDocumentoUpdateInputDto.GfdDocumentoUpdateInputDto(documentoId, LocalDate.now(), null, statusNaoConforme, "obs");
        input.setDocumento(docInput);

        var docMapped = new GfdDocumentoUpdateInputDto();
        var updated = new GfdDocumentoUpdateOutputDto();
        updated.setId(documentoId);
        updated.setStatus(GfdDocumentoStatusEnum.NAO_CONFORME);
        updated.setGfdTipoDocumento(new GfdDocumentoUpdateOutputDto.GfdTipoDocumentoDto(5, "Contrato", 30));

        var outputDto = new GfdMDocumentoUpdateOutputDto.GfdDocumentoUpdateOutputDto();

        when(fornecedorService.getById(fornecedorExistenteId)).thenReturn(mock(FornecedorGetByIdOutputDto.class));
        when(modelMapper.map(any(FornecedorGetByIdOutputDto.class), eq(Fornecedor.class))).thenReturn(fornecedor);
        when(modelMapper.map(docInput, GfdDocumentoUpdateInputDto.class)).thenReturn(docMapped);
        when(gfdDocumentoService.update(docMapped)).thenReturn(updated);
        when(modelMapper.map(any(GfdDocumentoUpdateOutputDto.class), eq(GfdMDocumentoUpdateOutputDto.GfdDocumentoUpdateOutputDto.class))).thenReturn(outputDto);

        //act
        var result = gfdManagerService.updateDocumento(input);

        //assert
        assertNotNull(result);
        verify(envioEmailService).enviarFila(any(EnvioEmail.class));
    }


    @Test
    void test_updateDocumento_deve_lancar_NotFoundException_quando_id_e_codUsuario_null() {
        var input = new GfdMDocumentoUpdateInputDto();
        input.setCodUsuario(null);
        input.setFornecedor(null);

        assertThrows(NotFoundException.class, () -> gfdManagerService.updateDocumento(input));
    }

    @Test
    void test_updateDocumento_deve_lancar_NotFoundException_quando_getById_retorna_null() {
        final Integer fornecedorExistenteId = 77;

        var input = new GfdMDocumentoUpdateInputDto();
        input.setCodUsuario(null);
        input.setFornecedor(new GfdMDocumentoUpdateInputDto.FornecedorDto(fornecedorExistenteId));

        when(fornecedorService.getById(fornecedorExistenteId)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> gfdManagerService.updateDocumento(input));
    }

    @Test
    void test_updateDocumento_deve_lancar_NotFoundException_quando_getByCodUsuario_retorna_null() {
        final Integer usuarioLogadoId = 123;

        var input = new GfdMDocumentoUpdateInputDto();
        input.setCodUsuario(usuarioLogadoId);
        input.setFornecedor(null);

        when(fornecedorService.getByCodUsuario(any())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> gfdManagerService.updateDocumento(input));
    }

    @Test
    void test_updateDocumento_deve_lancar_ForbiddenException_quando_usuario_sem_permissao() {
        final Integer fornecedorExistenteId = 77;
        final Integer usuarioLogadoId = 123;
        final String emailFornecedor = "fornecedor@mili.com";

        Fornecedor fornecedor;
        fornecedor = new Fornecedor();
        fornecedor.setCodigo(fornecedorExistenteId);
        fornecedor.setCodUsuario(usuarioLogadoId);
        fornecedor.setEmail(emailFornecedor);

        var input = new GfdMDocumentoUpdateInputDto();
        input.setCodUsuario(999); // diferente do fornecedor.getCodUsuario()
        input.setFornecedor(new GfdMDocumentoUpdateInputDto.FornecedorDto(fornecedorExistenteId));

        when(fornecedorService.getById(fornecedorExistenteId)).thenReturn(mock(FornecedorGetByIdOutputDto.class));
        when(modelMapper.map(any(FornecedorGetByIdOutputDto.class), eq(Fornecedor.class))).thenReturn(fornecedor);

        assertThrows(ForbiddenException.class, () -> gfdManagerService.updateDocumento(input));
    }

    @Test
    void test_downloadDocumento_deve_retornar_outputDto_quando_usuario_tem_permissao() {
        // Arrange
        final Integer idDocumento = 10;
        final Integer idFornecedor = 77;
        final Integer idUsuarioLogado = 123;
        final String emailFornecedor = "fornecedor@mili.com";

        var inputDto = new GfdMDocumentoDownloadInputDto(idDocumento, idUsuarioLogado, idFornecedor);
        var downloadInputDto = new GfdDocumentoDownloadInputDto(idDocumento);
        var downloadOutputDto = new GfdDocumentoDownloadOutputDto(idDocumento, "s3/path/file.pdf");
        var expectedOutputDto = new GfdMDocumentoDownloadOutputDto(idDocumento, "s3/path/file.pdf");

        var fornecedorOutputDto = new FornecedorGetByIdOutputDto();
        fornecedorOutputDto.setCodigo(idFornecedor);
        fornecedorOutputDto.setCodUsuario(idUsuarioLogado);
        fornecedorOutputDto.setEmail(emailFornecedor);

        var fornecedor = new Fornecedor();
        fornecedor.setCodigo(idFornecedor);
        fornecedor.setCodUsuario(idUsuarioLogado);
        fornecedor.setEmail(emailFornecedor);

        when(fornecedorService.getById(idFornecedor)).thenReturn(fornecedorOutputDto);
        when(modelMapper.map(fornecedorOutputDto, Fornecedor.class)).thenReturn(fornecedor);
        when(modelMapper.map(inputDto, GfdDocumentoDownloadInputDto.class)).thenReturn(downloadInputDto);
        when(gfdDocumentoService.download(downloadInputDto)).thenReturn(downloadOutputDto);
        when(modelMapper.map(downloadOutputDto, GfdMDocumentoDownloadOutputDto.class)).thenReturn(expectedOutputDto);

        // Act
        var resultado = gfdManagerService.downloadDocumento(inputDto);

        // Assert
        assertNotNull(resultado);
        assertEquals(expectedOutputDto, resultado);
    }


    @Test
    void test_deleteDocumento_deve_chamar_servico_delete_quando_permissao_valida() {
        // Arrange
        final Integer idDocumento = 10;
        final Integer idFornecedor = 77;
        final Integer idUsuarioLogado = 123;
        final String emailFornecedor = "fornecedor@mili.com";

        var fornecedorOutputDto = new FornecedorGetByIdOutputDto();
        fornecedorOutputDto.setCodigo(idFornecedor);
        fornecedorOutputDto.setCodUsuario(idUsuarioLogado);
        fornecedorOutputDto.setEmail(emailFornecedor);

        var fornecedor = new Fornecedor();
        fornecedor.setCodigo(idFornecedor);
        fornecedor.setCodUsuario(idUsuarioLogado);
        fornecedor.setEmail(emailFornecedor);

        var input = new GfdMDocumentoDeleteInputDto(idDocumento, idUsuarioLogado, idFornecedor);
        var deleteDto = new GfdDocumentoDeleteInputDto();

        when(fornecedorService.getById(idFornecedor)).thenReturn(mock(FornecedorGetByIdOutputDto.class));
        when(modelMapper.map(any(FornecedorGetByIdOutputDto.class), eq(Fornecedor.class))).thenReturn(fornecedor);
        when(modelMapper.map(input, GfdDocumentoDeleteInputDto.class)).thenReturn(deleteDto);

        gfdManagerService.deleteDocumento(input);

        verify(gfdDocumentoService).delete(deleteDto);
    }
}