package br.com.mili.milibackend.gfd.application.service;

import br.com.mili.milibackend.envioEmail.domain.entity.EnvioEmail;
import br.com.mili.milibackend.envioEmail.domain.interfaces.IEnvioEmailService;
import br.com.mili.milibackend.envioEmail.shared.RemetenteEnum;
import br.com.mili.milibackend.fornecedor.domain.entity.Fornecedor;
import br.com.mili.milibackend.fornecedor.domain.usecases.GetFornecedorByCodOrIdUseCase;
import br.com.mili.milibackend.fornecedor.domain.usecases.ValidatePermissionFornecedorUseCase;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoDeleteInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoDownloadInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoUpdateInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioCreateInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioDeleteInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioUpdateInputDto;
import br.com.mili.milibackend.gfd.application.dto.manager.documentos.*;
import br.com.mili.milibackend.gfd.application.dto.manager.fornecedor.GfdMFornecedorGetInputDto;
import br.com.mili.milibackend.gfd.application.dto.manager.fornecedor.GfdMFornecedorGetOutputDto;
import br.com.mili.milibackend.gfd.application.dto.manager.funcionario.*;
import br.com.mili.milibackend.gfd.domain.entity.GfdDocumentoStatusEnum;
import br.com.mili.milibackend.gfd.domain.interfaces.IGfdDocumentoService;
import br.com.mili.milibackend.gfd.domain.interfaces.IGfdFuncionarioService;
import br.com.mili.milibackend.gfd.domain.interfaces.IGfdManagerService;
import br.com.mili.milibackend.gfd.domain.usecases.DeleteGfdDocumentoUseCase;
import br.com.mili.milibackend.gfd.domain.usecases.GetAllGfdFuncionarioUseCase;
import br.com.mili.milibackend.gfd.domain.usecases.gfdFuncionario.CreateFuncionarioUseCase;
import br.com.mili.milibackend.gfd.domain.usecases.gfdFuncionario.UpdateGfdFuncionarioUseCase;
import br.com.mili.milibackend.gfd.domain.usecases.UpdateGfdDocumentoUseCase;
import br.com.mili.milibackend.gfd.infra.email.GfdDocumentoEmailTemplate;
import br.com.mili.milibackend.shared.exception.types.ConflictException;
import br.com.mili.milibackend.shared.exception.types.ForbiddenException;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import br.com.mili.milibackend.shared.page.pagination.MyPage;
import br.com.mili.milibackend.shared.page.pagination.PageBaseImpl;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static br.com.mili.milibackend.gfd.adapter.exception.GfdFuncionarioCodeException.GFD_FUNCIONARIO_NAO_ENCONTRADO;
import static br.com.mili.milibackend.gfd.adapter.exception.GfdMCodeException.GFD_FUNCIONARIO_SEM_PERMISSAO;
import static br.com.mili.milibackend.gfd.adapter.exception.GfdMCodeException.GFD_LEI_LGPD_NAO_ACEITA;

@Service
public class GfdManagerService implements IGfdManagerService {

    private final IGfdDocumentoService gfdDocumentoService;
    private final ModelMapper modelMapper;
    private final IGfdFuncionarioService gfdFuncionarioService;
    private final IEnvioEmailService envioEmailService;
    private final String baseUrl;
    private final GetAllGfdFuncionarioUseCase getAllGfdFuncionarioUseCase;
    private final GetFornecedorByCodOrIdUseCase getFornecedorByCodOrIdUseCase;
    private final UpdateGfdDocumentoUseCase updateGfdDocumentoUseCase;
    private final DeleteGfdDocumentoUseCase deleteGfdDocumentoUseCase;
    private final ValidatePermissionFornecedorUseCase validatePermissionFornecedorUseCase;
    private final CreateFuncionarioUseCase createFuncionarioUseCase;
    private final UpdateGfdFuncionarioUseCase updateGfdFuncionarioUseCase;

    public GfdManagerService(IGfdDocumentoService gfdDocumentoService, ModelMapper modelMapper, IGfdFuncionarioService gfdFuncionarioService, IEnvioEmailService envioEmailService, @Value("${frontend.url.origin}") String baseUrl, GetAllGfdFuncionarioUseCase getAllGfdFuncionarioUseCase, GetFornecedorByCodOrIdUseCase getFornecedorByCodOrIdUseCase, UpdateGfdDocumentoUseCase updateGfdDocumentoUseCase, DeleteGfdDocumentoUseCase deleteGfdDocumentoUseCase, ValidatePermissionFornecedorUseCase validatePermissionFornecedorUseCase, CreateFuncionarioUseCase createFuncionarioUseCase, UpdateGfdFuncionarioUseCase updateGfdFuncionarioUseCase) {
        this.gfdDocumentoService = gfdDocumentoService;
        this.modelMapper = modelMapper;
        this.gfdFuncionarioService = gfdFuncionarioService;
        this.envioEmailService = envioEmailService;
        this.baseUrl = baseUrl;
        this.getAllGfdFuncionarioUseCase = getAllGfdFuncionarioUseCase;
        this.getFornecedorByCodOrIdUseCase = getFornecedorByCodOrIdUseCase;
        this.updateGfdDocumentoUseCase = updateGfdDocumentoUseCase;
        this.deleteGfdDocumentoUseCase = deleteGfdDocumentoUseCase;
        this.validatePermissionFornecedorUseCase = validatePermissionFornecedorUseCase;
        this.createFuncionarioUseCase = createFuncionarioUseCase;
        this.updateGfdFuncionarioUseCase = updateGfdFuncionarioUseCase;
    }

    @Override
    public GfdMVerificarFornecedorOutputDto verifyFornecedor(GfdMVerificarFornecedorInputDto inputDto) {
        var fornecedor = getFornecedorByCodOrIdUseCase.execute(inputDto.getCodUsuario(), inputDto.getId());

        String razaoSocial = fornecedor.getRazaoSocial();

        return new GfdMVerificarFornecedorOutputDto(representanteCadastrado(fornecedor), razaoSocial != null ? razaoSocial : "Bem-Vindo");
    }


    @Override
    public GfdMFornecedorGetOutputDto getFornecedor(GfdMFornecedorGetInputDto inputDto) {
        var fornecedor = getFornecedorByCodOrIdUseCase.execute(inputDto.getCodUsuario(), inputDto.getId());

        return modelMapper.map(fornecedor, GfdMFornecedorGetOutputDto.class);
    }

    @Override
    public GfdMFuncionarioGetAllOutputDto getAllFuncionarios(GfdMFuncionarioGetAllInputDto inputDto) {
        var funcionarioInput = inputDto.getFuncionario();

        Integer fornecedorId = funcionarioInput != null && funcionarioInput.getFornecedor() != null ? funcionarioInput.getFornecedor().getCodigo() : null;

        // Busca e valida fornecedor
        var fornecedor = getFornecedorAndValidate(inputDto.getCodUsuario(), fornecedorId);

        var filtro = modelMapper.map(funcionarioInput, GfdFuncionarioGetAllInputDto.class);
        filtro.setFornecedor(new GfdFuncionarioGetAllInputDto.FornecedorDto(fornecedorId));

        var pageFuncionario = getAllGfdFuncionarioUseCase.execute(filtro);

        var content = pageFuncionario.getContent().stream().map(funcionario -> modelMapper.map(funcionario, GfdMFuncionarioGetAllOutputDto.GfdFuncionarioDto.class)).toList();

        MyPage<GfdMFuncionarioGetAllOutputDto.GfdFuncionarioDto> pagedResult = new PageBaseImpl<>(content, pageFuncionario.getPage(), pageFuncionario.getSize(), pageFuncionario.getTotalElements());

        return new GfdMFuncionarioGetAllOutputDto(pagedResult);
    }

    @Override
    public GfdMFuncionarioCreateOutputDto createFuncionario(@Valid GfdMFuncionarioCreateInputDto inputDto) {
        var inputFornecedorId = inputDto.getFuncionario() != null && inputDto.getFuncionario().getFornecedor() != null
                ? inputDto.getFuncionario().getFornecedor().getCodigo()
                : null;

        var fornecedor = getFornecedorAndValidate(
                inputDto.getCodUsuario(),
                inputFornecedorId
        );

        var gfdFuncionarioCreateInputDto = modelMapper.map(inputDto.getFuncionario(), GfdFuncionarioCreateInputDto.class);
        gfdFuncionarioCreateInputDto.setFornecedor(new GfdFuncionarioCreateInputDto.FornecedorDto(fornecedor.getCodigo()));

        var funcionarioCreated = createFuncionarioUseCase.execute(gfdFuncionarioCreateInputDto);

        var functionarioOutputDto = modelMapper.map(funcionarioCreated, GfdMFuncionarioCreateOutputDto.GfdFuncionarioDto.class);

        return new GfdMFuncionarioCreateOutputDto(functionarioOutputDto);
    }

    private Fornecedor getFornecedorAndValidate(Integer codUsuario, Integer idFornecedor) {
        // busca o fornecedor
        var fornecedor = getFornecedorByCodOrIdUseCase.execute(codUsuario, idFornecedor);

        //todo: colocar no dto isAnalista pare evitar isso
        if (!validatePermissionFornecedorUseCase.execute(codUsuario, fornecedor.getCodigo())) {
            throw new ForbiddenException(GFD_FUNCIONARIO_SEM_PERMISSAO.getMensagem(), GFD_FUNCIONARIO_SEM_PERMISSAO.getCode());
        }

        if (fornecedor.getAceiteLgpd() == null || fornecedor.getAceiteLgpd() == 0) {
            throw new ConflictException(GFD_LEI_LGPD_NAO_ACEITA.getMensagem(), GFD_LEI_LGPD_NAO_ACEITA.getCode());
        }

        return fornecedor;
    }

    @Override
    public GfdMFuncionarioUpdateOutputDto updateFuncionario(@Valid GfdMFuncionarioUpdateInputDto inputDto) {
        var inputFornecedorId = inputDto.getFuncionario() != null && inputDto.getFuncionario().getFornecedor() != null
                ? inputDto.getFuncionario().getFornecedor().getCodigo()
                : null;

        var fornecedor = getFornecedorAndValidate(inputDto.getCodUsuario(), inputFornecedorId);

        var gfdFuncionarioUpdateInputDto = modelMapper.map(inputDto.getFuncionario(), GfdFuncionarioUpdateInputDto.class);

        gfdFuncionarioUpdateInputDto.setFornecedor(new GfdFuncionarioUpdateInputDto.FornecedorDto(fornecedor.getCodigo()));

        var funcionarioUpdated = updateGfdFuncionarioUseCase.execute(gfdFuncionarioUpdateInputDto);

        var functionarioDto = modelMapper.map(funcionarioUpdated, GfdMFuncionarioUpdateOutputDto.GfdFuncionarioDto.class);

        return new GfdMFuncionarioUpdateOutputDto(functionarioDto);
    }

    @Override
    public GfdMFuncionarioGetOutputDto getFuncionario(GfdMFuncionarioGetInputDto inputDto) {
        var codFornecedor = inputDto.getFuncionario() != null && inputDto.getFuncionario().getFornecedor() != null ? inputDto.getFuncionario().getFornecedor().getCodigo() : null;

        getFornecedorAndValidate(inputDto.getCodUsuario(), codFornecedor);
        ;
        var getAllInputDto = new GfdFuncionarioGetAllInputDto();
        getAllInputDto.setId(inputDto.getFuncionario().getId());

        var funcionario = getAllGfdFuncionarioUseCase.execute(getAllInputDto).getContent();

        if (funcionario.isEmpty()) {
            throw new NotFoundException(GFD_FUNCIONARIO_NAO_ENCONTRADO.getMensagem(), GFD_FUNCIONARIO_NAO_ENCONTRADO.getCode());
        }

        var functionarioDto = modelMapper.map(funcionario.get(0), GfdMFuncionarioGetOutputDto.GfdFuncionarioDto.class);

        return new GfdMFuncionarioGetOutputDto(functionarioDto);
    }

    @Override
    public void deleteFuncionario(GfdMFuncionarioDeleteInputDto inputDto) {
        var codFornecedor = inputDto.getFuncionario() != null && inputDto.getFuncionario().getFornecedor() != null ? inputDto.getFuncionario().getFornecedor().getCodigo() : null;

        getFornecedorAndValidate(inputDto.getCodUsuario(), codFornecedor);
        ;

        var gfdFuncionarioDeleteInputDto = modelMapper.map(inputDto.getFuncionario(), GfdFuncionarioDeleteInputDto.class);

        gfdFuncionarioService.delete(gfdFuncionarioDeleteInputDto);
    }

    @Override
    public GfdMDocumentoUpdateOutputDto updateDocumento(GfdMDocumentoUpdateInputDto inputDto) {
        var codFornecedor = inputDto.getFornecedor() != null ? inputDto.getFornecedor().getId() : null;

        var fornecedor = getFornecedorAndValidate(inputDto.getCodUsuario(), codFornecedor);

        // atualiza o documento
        var dto = GfdDocumentoUpdateInputDto.builder()
                .documento(
                        inputDto.getDocumento().getId(),
                        inputDto.getDocumento().getDataEmissao(),
                        inputDto.getDocumento().getDataValidade(),
                        inputDto.getDocumento().getStatus(),
                        inputDto.getDocumento().getObservacao())
                .periodo(inputDto.getGfdDocumentoPeriodo() != null ? inputDto.getGfdDocumentoPeriodo().getPeriodo() : null)
                .build();

        var updatedDto = updateGfdDocumentoUseCase.execute(dto);

        // envia o email caso o documento seja nao conforme ou expirado
        boolean isNaoConforme = GfdDocumentoStatusEnum.NAO_CONFORME.getDescricao().equals(updatedDto.getStatus().getDescricao());
        boolean isExpirado = GfdDocumentoStatusEnum.EXPIRADO.getDescricao().equals(updatedDto.getStatus().getDescricao());

        if (isNaoConforme || isExpirado) {
            var funcionarioId = updatedDto.getGfdFuncionario() != null ? updatedDto.getGfdFuncionario().getId() : null;

            for (String email : fornecedor.getEmail().split(";")) {
                enviarEmailForneceddor(
                        updatedDto.getGfdTipoDocumento().getId(),
                        email,
                        updatedDto.getStatus().getDescricao(),
                        funcionarioId
                );
            }
        }

        var gfdDocumentoOutDto = modelMapper.map(updatedDto, GfdMDocumentoUpdateOutputDto.GfdDocumentoUpdateOutputDto.class);
        return new GfdMDocumentoUpdateOutputDto(gfdDocumentoOutDto);
    }

    @Override
    public GfdMDocumentoDownloadOutputDto downloadDocumento(GfdMDocumentoDownloadInputDto inputDto) {
        getFornecedorAndValidate(inputDto.getCodUsuario(), inputDto.getFornecedorId());

        var dto = modelMapper.map(inputDto, GfdDocumentoDownloadInputDto.class);
        var linkDto = gfdDocumentoService.download(dto);

        return modelMapper.map(linkDto, GfdMDocumentoDownloadOutputDto.class);
    }

    @Override
    public void deleteDocumento(GfdMDocumentoDeleteInputDto inputDto) {
        getFornecedorAndValidate(inputDto.getCodUsuario(), inputDto.getFornecedorId());

        var dto = modelMapper.map(inputDto, GfdDocumentoDeleteInputDto.class);
        deleteGfdDocumentoUseCase.execute(dto);
    }

    private void enviarEmailForneceddor(Integer tipoDocumento, String email, String status, Integer funcionarioId) {
        var urlDocumento = baseUrl + "/gfd/fornecedor/docs/" + tipoDocumento;
        var urlDocumentoFuncionario = funcionarioId != null ? baseUrl + "/gfd/fornecedor/funcionario/" + funcionarioId + "/docs/" + tipoDocumento : null;

        var url = urlDocumentoFuncionario == null ? urlDocumento : urlDocumentoFuncionario;

        var template = GfdDocumentoEmailTemplate.template(status, url);

        var titulo = "Mili - Documento: " + status;

        var assunto = "Documento: " + status;

        var envioEmail = EnvioEmail.builder().remetente(RemetenteEnum.MILI.getEndereco()).destinatario(email).assunto(assunto).titulo(titulo).texto(template).build();

        envioEmailService.enviarFila(envioEmail);
    }


    private boolean representanteCadastrado(Fornecedor fornecedor) {
        return fornecedor.getContato() != null && fornecedor.getEmail() != null && fornecedor.getCelular() != null && (fornecedor.getAceiteLgpd() != null && fornecedor.getAceiteLgpd().equals(1));
    }
}
