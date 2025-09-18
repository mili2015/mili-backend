package br.com.mili.milibackend.gfd.application.service;

import br.com.mili.milibackend.envioEmail.domain.entity.EnvioEmail;
import br.com.mili.milibackend.envioEmail.domain.interfaces.IEnvioEmailService;
import br.com.mili.milibackend.fornecedor.application.dto.FornecedorGetByIdOutputDto;
import br.com.mili.milibackend.fornecedor.domain.entity.Fornecedor;
import br.com.mili.milibackend.fornecedor.domain.usecases.GetFornecedorByCodOrIdUseCase;
import br.com.mili.milibackend.fornecedor.domain.usecases.ValidatePermissionFornecedorUseCase;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.*;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.*;
import br.com.mili.milibackend.gfd.application.dto.manager.documentos.*;
import br.com.mili.milibackend.gfd.application.dto.manager.fornecedor.GfdMFornecedorGetInputDto;
import br.com.mili.milibackend.gfd.application.dto.manager.fornecedor.GfdMFornecedorGetOutputDto;
import br.com.mili.milibackend.gfd.application.dto.manager.funcionario.*;
import br.com.mili.milibackend.gfd.domain.entity.GfdDocumentoStatusEnum;
import br.com.mili.milibackend.gfd.domain.interfaces.IGfdDocumentoService;
import br.com.mili.milibackend.gfd.domain.interfaces.IGfdFuncionarioService;
import br.com.mili.milibackend.gfd.domain.usecases.DeleteGfdDocumentoUseCase;
import br.com.mili.milibackend.gfd.domain.usecases.GetAllGfdFuncionarioUseCase;
import br.com.mili.milibackend.gfd.domain.usecases.gfdFuncionario.CreateFuncionarioUseCase;
import br.com.mili.milibackend.gfd.domain.usecases.gfdFuncionario.UpdateGfdFuncionarioUseCase;
import br.com.mili.milibackend.gfd.domain.usecases.UpdateGfdDocumentoUseCase;
import br.com.mili.milibackend.shared.exception.types.ForbiddenException;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import br.com.mili.milibackend.shared.page.pagination.PageBaseImpl;
import org.apache.tika.Tika;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.List;

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
    private IEnvioEmailService envioEmailService;

    @Mock
    private IGfdFuncionarioService gfdFuncionarioService;

    @Mock
    private CreateFuncionarioUseCase createFuncionarioUseCase;

    @Mock
    private UpdateGfdFuncionarioUseCase updateGfdFuncionarioUseCase;

    @Mock
    private UpdateGfdDocumentoUseCase updateGfdDocumentoUseCase;

    @Mock
    private DeleteGfdDocumentoUseCase deleteGfdDocumentoUseCase;

    @Mock
    private IGfdDocumentoService gfdDocumentoService;

    @Mock
    private ValidatePermissionFornecedorUseCase validatePermissionFornecedorUseCase;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private GetAllGfdFuncionarioUseCase getAllGfdFuncionarioUseCase;

    @Mock
    private GetFornecedorByCodOrIdUseCase getFornecedorByCodOrIdUseCase;

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
        fornecedor.setAceiteLgpd(1);

        when(getFornecedorByCodOrIdUseCase.execute(null, 1)).thenReturn(fornecedor);

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

        when(getFornecedorByCodOrIdUseCase.execute(null, 1))
                .thenThrow(new NotFoundException(
                        GFD_FORNECEDOR_NAO_ENCONTRADO.getMensagem(),
                        GFD_FORNECEDOR_NAO_ENCONTRADO.getCode()
                ));

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
        fornecedor.setContato("Contato");
        fornecedor.setEmail("email@teste.com");
        fornecedor.setCelular("123456789");
        fornecedor.setAceiteLgpd(0);


        when(getFornecedorByCodOrIdUseCase.execute(123, null)).thenReturn(fornecedor);

        // Act
        GfdMVerificarFornecedorOutputDto outputDto = gfdManagerService.verifyFornecedor(inputDto);

        // Assert
        assertFalse(outputDto.getRepresentanteCadastrado());
        assertEquals("Teste", outputDto.getRazaoSocial());
    }

    @Test
    void test_GetFornecedor__deve_retornar_fornecedor_quando_encontrado_por_id() {
        // Arrange
        GfdMFornecedorGetInputDto inputDto = new GfdMFornecedorGetInputDto();
        inputDto.setId(1);

        var fornecedor = new Fornecedor();
        fornecedor.setRazaoSocial("Teste");
        fornecedor.setContato("Contato");
        fornecedor.setEmail("email@teste.com");
        fornecedor.setCelular("123456789");
        fornecedor.setAceiteLgpd(0);


        when(getFornecedorByCodOrIdUseCase.execute(null, 1)).thenReturn(fornecedor);
        when(modelMapper.map(fornecedor, GfdMFornecedorGetOutputDto.class)).thenReturn(new GfdMFornecedorGetOutputDto());

        // Act
        GfdMFornecedorGetOutputDto outputDto = gfdManagerService.getFornecedor(inputDto);

        // Assert
        assertNotNull(outputDto);
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
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setCodigo(codFornecedor);


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
        when(getFornecedorByCodOrIdUseCase.execute(codUsuario, codFornecedor)).thenReturn(fornecedor);
        when(modelMapper.map(funcionarioInputDto, GfdFuncionarioCreateInputDto.class)).thenReturn(new GfdFuncionarioCreateInputDto());
        when(createFuncionarioUseCase.execute(any())).thenReturn(funcionarioCriado);
        when(modelMapper.map(funcionarioCriado, GfdMFuncionarioCreateOutputDto.GfdFuncionarioDto.class)).thenReturn(funcionarioOutputDto);
        when(validatePermissionFornecedorUseCase.execute(any(), any())).thenReturn(true);

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

        var fornecedorEntity = new Fornecedor();
        fornecedorEntity.setCodigo(codFornecedor);
        when(getFornecedorByCodOrIdUseCase.execute(codUsuario, codFornecedor)).thenReturn(fornecedorEntity);
        when(validatePermissionFornecedorUseCase.execute(any(), any())).thenReturn(false);

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

        var fornecedorEntity = new Fornecedor();
        fornecedorEntity.setCodigo(codFornecedor);
        when(getFornecedorByCodOrIdUseCase.execute(codUsuario, codFornecedor)).thenReturn(fornecedorEntity);


        var domainUpdateInput = new GfdFuncionarioUpdateInputDto();
        domainUpdateInput.setId(idFuncionario);
        domainUpdateInput.setNome("Maria");
        when(modelMapper.map(funcDto, GfdFuncionarioUpdateInputDto.class))
                .thenReturn(domainUpdateInput);

        var domainOutputDto = new GfdFuncionarioUpdateOutputDto();
        domainOutputDto.setId(idFuncionario);
        domainOutputDto.setNome("Maria Atualizada");
        when(updateGfdFuncionarioUseCase.execute(domainUpdateInput))
                .thenReturn(domainOutputDto);

        var outFuncDto = new GfdMFuncionarioUpdateOutputDto.GfdFuncionarioDto();
        outFuncDto.setId(idFuncionario);
        outFuncDto.setNome("Maria Atualizada");
        when(modelMapper.map(domainOutputDto, GfdMFuncionarioUpdateOutputDto.GfdFuncionarioDto.class))
                .thenReturn(outFuncDto);

        when(validatePermissionFornecedorUseCase.execute(any(), any())).thenReturn(true);

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

        var fornecedorEntity = new Fornecedor();
        fornecedorEntity.setCodigo(codFornecedor);
        when(getFornecedorByCodOrIdUseCase.execute(codUsuario, codFornecedor)).thenReturn(fornecedorEntity);

        when(validatePermissionFornecedorUseCase.execute(any(), any())).thenReturn(false);
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

        var fornecedorEntity = new Fornecedor();
        fornecedorEntity.setCodigo(codFornecedor);
        when(getFornecedorByCodOrIdUseCase.execute(codUsuario, codFornecedor)).thenReturn(fornecedorEntity);

        when(validatePermissionFornecedorUseCase.execute(any(), any())).thenReturn(false);
        // Act
        var ex = assertThrows(ForbiddenException.class, () -> gfdManagerService.getFuncionario(inputDto));
        assertEquals(GFD_FUNCIONARIO_SEM_PERMISSAO.getMensagem(), ex.getMessage());
        assertEquals(GFD_FUNCIONARIO_SEM_PERMISSAO.getCode(), ex.getCode());
    }

    @Test
    void test_getFuncionario_deve_retornar_funcionario_quando_input_valido() {
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

        var fornecedorEntity = new Fornecedor();
        fornecedorEntity.setCodigo(codFornecedor);

        when(getFornecedorByCodOrIdUseCase.execute(codUsuario, codFornecedor)).thenReturn(fornecedorEntity);
        when(validatePermissionFornecedorUseCase.execute(any(), any())).thenReturn(true);

        var funcionarioEntity = new GfdFuncionarioGetAllOutputDto();
        funcionarioEntity.setId(idFuncionario);
        funcionarioEntity.setNome("Maria");

        when(getAllGfdFuncionarioUseCase.execute(any(GfdFuncionarioGetAllInputDto.class))).thenReturn(new PageBaseImpl<GfdFuncionarioGetAllOutputDto>(List.of(funcionarioEntity), 1, 20, 0));

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
        assertEquals("Maria", result.getFuncionario().getNome(), "O nome deve bater com o esperado");
    }

    @Test
    void test_deleteFuncionario_deve_deletar_funcionario_quando_input_valido() {
        // Arrange
        Integer codUsuario = 100;
        Integer codFornecedor = 200;
        Integer idFuncionario = 5;

        var fornecedorInputDto = new GfdMFuncionarioDeleteInputDto.GfdFuncionarioDto.FornecedorDto(codFornecedor);
        var funcDto = new GfdMFuncionarioDeleteInputDto.GfdFuncionarioDto();
        funcDto.setId(idFuncionario);
        funcDto.setFornecedor(fornecedorInputDto);

        var inputDto = new GfdMFuncionarioDeleteInputDto();
        inputDto.setCodUsuario(codUsuario);
        inputDto.setFuncionario(funcDto);

        var fornecedorEntity = new Fornecedor();
        fornecedorEntity.setCodigo(codFornecedor);
        when(getFornecedorByCodOrIdUseCase.execute(codUsuario, codFornecedor)).thenReturn(fornecedorEntity);

        var domainDeleteDto = new GfdFuncionarioDeleteInputDto();
        domainDeleteDto.setId(idFuncionario);
        when(modelMapper.map(funcDto, GfdFuncionarioDeleteInputDto.class))
                .thenReturn(domainDeleteDto);
        when(validatePermissionFornecedorUseCase.execute(any(), any())).thenReturn(true);

        // Act
        gfdManagerService.deleteFuncionario(inputDto);

        // Assert
        verify(gfdFuncionarioService, times(1)).delete(domainDeleteDto);
    }

    @Test
    void test_deleteFuncionario_deve_lancar_ForbiddenException_quando_usuario_sem_permissao() {
        // Arrange
        Integer codUsuario = 100;
        Integer codFornecedor = 200;
        Integer idFuncionario = 5;

        var fornecedorInputDto = new GfdMFuncionarioDeleteInputDto.GfdFuncionarioDto.FornecedorDto(codFornecedor);
        var funcDto = new GfdMFuncionarioDeleteInputDto.GfdFuncionarioDto();
        funcDto.setId(idFuncionario);
        funcDto.setFornecedor(fornecedorInputDto);

        var inputDto = new GfdMFuncionarioDeleteInputDto();
        inputDto.setCodUsuario(codUsuario);
        inputDto.setFuncionario(funcDto);

        var fornecedorEntity = new Fornecedor();
        fornecedorEntity.setCodigo(codFornecedor);
        when(getFornecedorByCodOrIdUseCase.execute(codUsuario, codFornecedor)).thenReturn(fornecedorEntity);
        when(validatePermissionFornecedorUseCase.execute(any(), any())).thenReturn(false);

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

        fornecedor.setEmail(emailFornecedor);

        var input = new GfdMDocumentoUpdateInputDto();
        input.setCodUsuario(usuarioLogadoId);
        input.setFornecedor(new GfdMDocumentoUpdateInputDto.FornecedorDto(fornecedorExistenteId));

        var docInput = new GfdMDocumentoUpdateInputDto.GfdDocumentoUpdateInputDto(documentoId, LocalDate.now(), null, statusNaoConforme, "obs");
        input.setDocumento(docInput);

        var updated = new GfdDocumentoUpdateOutputDto();
        updated.setId(documentoId);
        updated.setStatus(GfdDocumentoStatusEnum.NAO_CONFORME);
        updated.setGfdTipoDocumento(new GfdDocumentoUpdateOutputDto.GfdTipoDocumentoDto(5, "Contrato", 30));

        var outputDto = new GfdMDocumentoUpdateOutputDto.GfdDocumentoUpdateOutputDto();

        when(getFornecedorByCodOrIdUseCase.execute(usuarioLogadoId, fornecedorExistenteId)).thenReturn(fornecedor);
        when(updateGfdDocumentoUseCase.execute(any(GfdDocumentoUpdateInputDto.class))).thenReturn(updated);
        when(modelMapper.map(any(GfdDocumentoUpdateOutputDto.class), eq(GfdMDocumentoUpdateOutputDto.GfdDocumentoUpdateOutputDto.class))).thenReturn(outputDto);
        when(validatePermissionFornecedorUseCase.execute(any(), any())).thenReturn(true);

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

        when(getFornecedorByCodOrIdUseCase.execute(null, null))
                .thenThrow(new NotFoundException(
                        GFD_FORNECEDOR_NAO_ENCONTRADO.getMensagem(),
                        GFD_FORNECEDOR_NAO_ENCONTRADO.getCode()
                ));


        assertThrows(NotFoundException.class, () -> gfdManagerService.updateDocumento(input));
    }

    @Test
    void test_updateDocumento_deve_lancar_NotFoundException_quando_getById_retorna_null() {
        final Integer fornecedorExistenteId = 77;

        var input = new GfdMDocumentoUpdateInputDto();
        input.setCodUsuario(null);
        input.setFornecedor(new GfdMDocumentoUpdateInputDto.FornecedorDto(fornecedorExistenteId));

        when(getFornecedorByCodOrIdUseCase.execute(null, fornecedorExistenteId))
                .thenThrow(new NotFoundException(
                        GFD_FORNECEDOR_NAO_ENCONTRADO.getMensagem(),
                        GFD_FORNECEDOR_NAO_ENCONTRADO.getCode()
        ));

        assertThrows(NotFoundException.class, () -> gfdManagerService.updateDocumento(input));
    }

    @Test
    void test_updateDocumento_deve_lancar_NotFoundException_quando_getByCodUsuario_retorna_null() {
        final Integer usuarioLogadoId = 123;

        var input = new GfdMDocumentoUpdateInputDto();
        input.setCodUsuario(usuarioLogadoId);
        input.setFornecedor(null);

        when(getFornecedorByCodOrIdUseCase.execute(usuarioLogadoId, null))
                .thenThrow(new NotFoundException(
                        GFD_FORNECEDOR_NAO_ENCONTRADO.getMensagem(),
                        GFD_FORNECEDOR_NAO_ENCONTRADO.getCode()
                ));

        assertThrows(NotFoundException.class, () -> gfdManagerService.updateDocumento(input));
    }

    @Test
    void test_updateDocumento_deve_lancar_ForbiddenException_quando_usuario_sem_permissao() {
        final Integer fornecedorExistenteId = 77;
        final Integer codUsuario = 999;
        final String emailFornecedor = "fornecedor@mili.com";

        Fornecedor fornecedor;
        fornecedor = new Fornecedor();
        fornecedor.setCodigo(fornecedorExistenteId);
        fornecedor.setEmail(emailFornecedor);

        var input = new GfdMDocumentoUpdateInputDto();
        input.setCodUsuario(codUsuario);
        input.setFornecedor(new GfdMDocumentoUpdateInputDto.FornecedorDto(fornecedorExistenteId));

        when(getFornecedorByCodOrIdUseCase.execute(codUsuario, fornecedorExistenteId)).thenReturn(fornecedor);

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

        fornecedor.setEmail(emailFornecedor);

        when(getFornecedorByCodOrIdUseCase.execute(idUsuarioLogado, idFornecedor)).thenReturn(fornecedor);
        when(modelMapper.map(inputDto, GfdDocumentoDownloadInputDto.class)).thenReturn(downloadInputDto);
        when(gfdDocumentoService.download(downloadInputDto)).thenReturn(downloadOutputDto);
        when(modelMapper.map(downloadOutputDto, GfdMDocumentoDownloadOutputDto.class)).thenReturn(expectedOutputDto);
        when(validatePermissionFornecedorUseCase.execute(any(), any())).thenReturn(true);

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

        fornecedor.setEmail(emailFornecedor);

        var input = new GfdMDocumentoDeleteInputDto(idDocumento, idUsuarioLogado, idFornecedor);
        var deleteDto = new GfdDocumentoDeleteInputDto();

        when(getFornecedorByCodOrIdUseCase.execute(idUsuarioLogado, idFornecedor)).thenReturn(fornecedor);
        when(modelMapper.map(input, GfdDocumentoDeleteInputDto.class)).thenReturn(deleteDto);
        when(validatePermissionFornecedorUseCase.execute(any(), any())).thenReturn(true);

        gfdManagerService.deleteDocumento(input);

        verify(deleteGfdDocumentoUseCase).execute(deleteDto);
    }
}