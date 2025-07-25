package br.com.mili.milibackend.gfd.application.service;

import br.com.mili.milibackend.envioEmail.domain.entity.EnvioEmail;
import br.com.mili.milibackend.envioEmail.domain.interfaces.IEnvioEmailService;
import br.com.mili.milibackend.envioEmail.shared.RemetenteEnum;
import br.com.mili.milibackend.fornecedor.application.dto.FornecedorGetByCodUsuarioInputDto;
import br.com.mili.milibackend.fornecedor.application.dto.GfdTipoDocumentoGetByIdOutputDto;
import br.com.mili.milibackend.fornecedor.application.dto.gfdDocumento.*;
import br.com.mili.milibackend.fornecedor.application.dto.gfdFuncionario.*;
import br.com.mili.milibackend.fornecedor.application.dto.gfdTipoDocumento.GfdTipoDocumentoGetAllInputDto;
import br.com.mili.milibackend.fornecedor.application.dto.gfdTipoDocumento.GfdTipoDocumentoGetAllOutputDto;
import br.com.mili.milibackend.fornecedor.domain.entity.Fornecedor;
import br.com.mili.milibackend.fornecedor.domain.entity.GfdDocumentoStatusEnum;
import br.com.mili.milibackend.fornecedor.domain.entity.GfdFuncionarioTipoContratacaoEnum;
import br.com.mili.milibackend.fornecedor.domain.entity.GfdTipoDocumentoTipoEnum;
import br.com.mili.milibackend.fornecedor.domain.interfaces.service.IFornecedorService;
import br.com.mili.milibackend.fornecedor.domain.interfaces.service.IGfdFuncionarioService;
import br.com.mili.milibackend.fornecedor.domain.interfaces.service.IGfdTipoDocumentoService;
import br.com.mili.milibackend.fornecedor.infra.email.GfdDocumentoEmailTemplate;
import br.com.mili.milibackend.gfd.application.dto.*;
import br.com.mili.milibackend.gfd.domain.interfaces.IGfdDocumentoService;
import br.com.mili.milibackend.gfd.domain.interfaces.IGfdManagerService;
import br.com.mili.milibackend.shared.exception.types.BadRequestException;
import br.com.mili.milibackend.shared.exception.types.ForbiddenException;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import br.com.mili.milibackend.shared.page.pagination.MyPage;
import br.com.mili.milibackend.shared.page.pagination.PageBaseImpl;
import br.com.mili.milibackend.shared.util.CleanFileName;
import jakarta.validation.Valid;
import org.apache.commons.codec.binary.Base64;
import org.apache.tika.Tika;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;

import java.time.LocalDate;
import java.util.*;

import static br.com.mili.milibackend.fornecedor.adapter.exception.GfdFuncionarioCodeException.GFD_FUNCIONARIO_NAO_ENCONTRADO;
import static br.com.mili.milibackend.gfd.adapter.exception.GfdMCodeException.*;

@Service
public class GfdManagerService implements IGfdManagerService {

    private final IFornecedorService fornecedorService;
    private final IGfdTipoDocumentoService gfdTipoDocumentoService;
    private final IGfdDocumentoService gfdDocumentoService;
    private final ModelMapper modelMapper;
    private final Tika tika;
    private final IGfdFuncionarioService gfdFuncionarioService;
    private final IEnvioEmailService envioEmailService;

    public GfdManagerService(IFornecedorService fornecedorService, IGfdTipoDocumentoService GfdTipoDocumentoService, IGfdDocumentoService gfdDocumentoService, ModelMapper modelMapper, Tika tika, IGfdFuncionarioService gfdFuncionarioService, IEnvioEmailService envioEmailService) {
        this.fornecedorService = fornecedorService;
        this.gfdTipoDocumentoService = GfdTipoDocumentoService;
        this.gfdDocumentoService = gfdDocumentoService;
        this.modelMapper = modelMapper;
        this.tika = tika;
        this.gfdFuncionarioService = gfdFuncionarioService;
        this.envioEmailService = envioEmailService;
    }

    @Override
    public GfdMVerificarFornecedorOutputDto verifyFornecedor(GfdMVerificarFornecedorInputDto inputDto) {
        var fornecedor = recuperarFornecedor(inputDto.getCodUsuario(), inputDto.getId());

        String razaoSocial = fornecedor.getRazaoSocial();

        return new GfdMVerificarFornecedorOutputDto(
                representanteCadastrado(fornecedor),
                razaoSocial != null ? razaoSocial : "Bem-Vindo"
        );
    }


    @Override
    public GfdMVerificarDocumentosOutputDto verifyDocumentos(@Valid GfdMVerificarDocumentosInputDto inputDto) {
        var output = new GfdMVerificarDocumentosOutputDto();
        var documentos = new LinkedHashSet<GfdMVerificarDocumentosOutputDto.DocumentoDto>();

        var fornecedor = recuperarFornecedor(inputDto.getCodUsuario(), inputDto.getId());

        // adiciona no title o nome do fornecedor
        output.setTitle(fornecedor.getRazaoSocial());

        var latestDocuments = gfdDocumentoService.findLatestDocumentsGroupedByTipoAndFornecedorId(fornecedor.getCodigo(), inputDto.getIdFuncionario());
        var tipoDocumentoInputDto = new GfdTipoDocumentoGetAllInputDto(GfdTipoDocumentoTipoEnum.FORNECEDOR);


        if (inputDto.getIdFuncionario() != null) {
            // pega as informacoes de funcionario
            var funcionario = getGfdFuncionarioGetByIdOutputDto(inputDto.getIdFuncionario());

            // adiciona no title o nome do funcionario
            output.setTitle(fornecedor.getRazaoSocial() + " - " + funcionario.getNome());

            if (funcionario.getTipoContratacao().equals(GfdFuncionarioTipoContratacaoEnum.CLT.getDescricao())) {
                tipoDocumentoInputDto.setTipo(GfdTipoDocumentoTipoEnum.FUNCIONARIO_CLT);
            } else {
                tipoDocumentoInputDto.setTipo(GfdTipoDocumentoTipoEnum.FUNCIONARIO_MEI);
            }
        }

        var tipoDocumentos = gfdTipoDocumentoService.getAll(tipoDocumentoInputDto);

        addNonMandatoryDocuments(documentos, latestDocuments, tipoDocumentos);
        addMandatoryDocuments(documentos, latestDocuments, tipoDocumentos);

        output.setDocumentos(documentos.stream().toList());

        return output;

    }

    private GfdFuncionarioGetByIdOutputDto getGfdFuncionarioGetByIdOutputDto(Integer inputDto) {
        var funcionario = gfdFuncionarioService.getById(inputDto);

        if (funcionario == null) {
            throw new NotFoundException(GFD_FUNCIONARIO_NAO_ENCONTRADO.getMensagem(), GFD_FUNCIONARIO_NAO_ENCONTRADO.getCode());
        }
        return funcionario;
    }

    private Fornecedor recuperarForncedor(Integer inputDto, Integer inputDto1) {
        var fornecedor = recuperarFornecedor(inputDto, inputDto1);

        if (fornecedor == null) {
            throw new NotFoundException(GFD_FORNECEDOR_NAO_ENCONTRADO.getMensagem(), GFD_FORNECEDOR_NAO_ENCONTRADO.getCode());
        }
        return fornecedor;
    }

    @Override
    public GfdMFornecedorGetOutputDto getFornecedor(GfdMFornecedorGetInputDto inputDto) {
        var fornecedor = recuperarForncedor(inputDto.getCodUsuario(), inputDto.getId());

        return modelMapper.map(fornecedor, GfdMFornecedorGetOutputDto.class);
    }

    @Override
    public GfdMUploadDocumentoOutputDto uploadDocumento(GfdMUploadDocumentoInputDto inputDto) {
        var listGfdDocumentoOutputDto = new ArrayList<GfdMUploadDocumentoOutputDto.GfdTipoDocumentoDto>();

        var fornecedor = recuperarForncedor(inputDto.getCodUsuario(), inputDto.getId());

        // recupera o tipo do documento
        GfdTipoDocumentoGetByIdOutputDto tipoDocumento = gfdTipoDocumentoService.getById(inputDto.getGfdTipoDocumento().getId());

        if (tipoDocumento == null) {
            throw new NotFoundException(GFD_FORNECEDOR_NAO_ENCONTRADO.getMensagem(), GFD_FORNECEDOR_NAO_ENCONTRADO.getCode());
        }

        //verifica se o tipo é de funcionario
        if (inputDto.getFuncionario() != null &&
            tipoDocumento.getTipo() == GfdTipoDocumentoTipoEnum.FORNECEDOR
        ) {
            throw new BadRequestException(GFD_TIPO_DOCUMENTO_FUNCIONARIO_BAD_REQUEST.getMensagem(), GFD_TIPO_DOCUMENTO_FUNCIONARIO_BAD_REQUEST.getCode());
        }

        //salva o arquivo no banco
        var listGfdDocumentoDto = inputDto.getListGfdDocumento();

        for (GfdMUploadDocumentoInputDto.GfdDocumentoDto gfdDocumentoDto : listGfdDocumentoDto) {
            var base64File = gfdDocumentoDto.getBase64File().file();

            byte[] fileData = Base64.decodeBase64(base64File);
            String mimeTypeStr = tika.detect(fileData);

            MimeType mimeType = MimeType.valueOf(mimeTypeStr);

            var nomeArquivo = UUID.randomUUID() + "-" + gfdDocumentoDto.getBase64File().fileName();

            //salva no banco
            var gfdTipoDocumentoDto = new GfdDocumentoCreateInputDto.GfdDocumentoDto.GfdTipoDocumentoDto(tipoDocumento.getId());

            var gfdDocumentoInputDto = GfdDocumentoCreateInputDto.GfdDocumentoDto.builder()
                    .ctforCodigo(fornecedor.getCodigo())
                    .nomeArquivo(nomeArquivo)
                    .nomeArquivoPath("gfd/" + nomeArquivo)
                    .tamanhoArquivo(fileData.length)
                    .dataCadastro(LocalDate.now())
                    .tipoArquivo(mimeType.toString())
                    .dataEmissao(gfdDocumentoDto.getDataEmissao())
                    .dataValidade(gfdDocumentoDto.getDataValidade())
                    .usuario(inputDto.getUsuario())
                    .status(GfdDocumentoStatusEnum.ENVIADO)
                    .gfdTipoDocumento(gfdTipoDocumentoDto);

            // adiciona o funcionario
            if (inputDto.getFuncionario() != null) {
                gfdDocumentoInputDto.gfdFuncionario(
                        new GfdDocumentoCreateInputDto
                                .GfdDocumentoDto
                                .GfdFuncionarioDto(inputDto.getFuncionario().getId())
                );
            }

            var gfdDocumentoCreateInputDto = GfdDocumentoCreateInputDto.builder()
                    .gfdDocumentoDto(gfdDocumentoInputDto.build())
                    .base64File(base64File)
                    .build();

            var gfdDocumentoCreateOutputDto = gfdDocumentoService.create(gfdDocumentoCreateInputDto);

            listGfdDocumentoOutputDto.add(modelMapper.map(gfdDocumentoCreateOutputDto, GfdMUploadDocumentoOutputDto.GfdTipoDocumentoDto.class));
        }

        return new GfdMUploadDocumentoOutputDto(listGfdDocumentoOutputDto);
    }

    @Override
    public GfdMDocumentosGetAllOutputDto getAllDocumentos(GfdMDocumentosGetAllInputDto inputDto) {
        var fornecedor = recuperarForncedor(inputDto.getCodUsuario(), inputDto.getId());

        var gfdDocumentoGetAllInputDto = modelMapper.map(inputDto, GfdDocumentoGetAllInputDto.class);
        gfdDocumentoGetAllInputDto.setCtforCodigo(fornecedor.getCodigo());

        // recupera os documentos
        var pageGfdDocumentoService = gfdDocumentoService.getAll(gfdDocumentoGetAllInputDto);

        var gfdDocumentoDto = pageGfdDocumentoService.getContent().stream()
                .map(gfdDocumento -> {
                    gfdDocumento.setNomeArquivo(CleanFileName.clear(gfdDocumento.getNomeArquivo()));
                    return modelMapper.map(gfdDocumento, GfdMDocumentosGetAllOutputDto.GfdDocumentoDto.class);
                })
                .toList();

        var pageGfdDocumentoDto = new PageBaseImpl<>(gfdDocumentoDto, pageGfdDocumentoService.getPage(), pageGfdDocumentoService.getSize(), pageGfdDocumentoService.getTotalElements()) {
        };

        var recuperarTiposDocumentoFornecedor = getFornecedorTipoDocumentos(inputDto.getFuncionario() != null ? inputDto.getFuncionario().getId() : null);

        // mostra doc anterior e doc proximo
        Integer idDocumento = inputDto.getTipoDocumentoId();

        var indexof = recuperarTiposDocumentoFornecedor.stream()
                .filter(tipo -> tipo.getId().equals(idDocumento))
                .findFirst()
                .map(recuperarTiposDocumentoFornecedor::indexOf)
                .orElse(-1);

        var nextDoc = 0;
        var previusDoc = 0;

        if (!recuperarTiposDocumentoFornecedor.isEmpty()) {

            var lastTipoDocumentoId = recuperarTiposDocumentoFornecedor.get(recuperarTiposDocumentoFornecedor.size() - 1).getId();
            var isLastTipoDocumentoId = lastTipoDocumentoId <= idDocumento;

            nextDoc = isLastTipoDocumentoId ? 0 : recuperarTiposDocumentoFornecedor.get(indexof + 1).getId();
            previusDoc = indexof > 0 ? recuperarTiposDocumentoFornecedor.get(indexof - 1).getId() : 0;
        }

        // adiciona os tipos documento
        var gfdTipoDocumentoDto = criarDtoTipoDocumentoParaResposta(inputDto, gfdDocumentoDto);

        return GfdMDocumentosGetAllOutputDto.builder()
                .gfdTipoDocumento(gfdTipoDocumentoDto)
                .gfdDocumento(pageGfdDocumentoDto)
                .nextDoc(nextDoc)
                .previousDoc(previusDoc)
                .build();
    }

    @Override
    public GfdMFuncionarioGetAllOutputDto getAllFuncionarios(GfdMFuncionarioGetAllInputDto inputDto) {
        var funcionarioInput = inputDto.getFuncionario();

        Integer fornecedorId = funcionarioInput != null && funcionarioInput.getFornecedor() != null
                ? funcionarioInput.getFornecedor().getCodigo()
                : null;

        // Busca e valida fornecedor
        var fornecedor = recuperarFornecedor(inputDto.getCodUsuario(), fornecedorId);
        validarPermissaoCriacaoFuncionario(inputDto.getCodUsuario(), fornecedor.getCodUsuario());

        var filtro = modelMapper.map(funcionarioInput, GfdFuncionarioGetAllInputDto.class);
        filtro.setFornecedor(new GfdFuncionarioGetAllInputDto.FornecedorDto(fornecedor.getCodigo()));

        var pageFuncionario = gfdFuncionarioService.getAll(filtro);

        var content = pageFuncionario.getContent().stream()
                .map(funcionario -> modelMapper.map(funcionario, GfdMFuncionarioGetAllOutputDto.GfdFuncionarioDto.class))
                .toList();

        MyPage<GfdMFuncionarioGetAllOutputDto.GfdFuncionarioDto> pagedResult =
                new PageBaseImpl<>(content, pageFuncionario.getPage(), pageFuncionario.getSize(), pageFuncionario.getTotalElements());

        return new GfdMFuncionarioGetAllOutputDto(pagedResult);
    }

    @Override
    public GfdMFuncionarioCreateOutputDto createFuncionario(GfdMFuncionarioCreateInputDto inputDto) {
        var fornecedorId = inputDto.getFuncionario() != null && inputDto.getFuncionario().getFornecedor() != null ? inputDto.getFuncionario().getFornecedor().getCodigo() : null;

        // busca o fornecedor
        var fornecedor = recuperarFornecedor(inputDto.getCodUsuario(), fornecedorId);

        // quando o usuario vier significa que o utilizador é um fornecedor
        validarPermissaoCriacaoFuncionario(inputDto.getCodUsuario(), fornecedor.getCodUsuario());

        var gfdFuncionarioCreateOutputDto = modelMapper.map(inputDto.getFuncionario(), GfdFuncionarioCreateInputDto.class);

        gfdFuncionarioCreateOutputDto.setFornecedor(new GfdFuncionarioCreateInputDto.FornecedorDto(fornecedor.getCodigo()));

        var functionarioDto = modelMapper.map(gfdFuncionarioService.create(gfdFuncionarioCreateOutputDto), GfdMFuncionarioCreateOutputDto.GfdFuncionarioDto.class);

        return new GfdMFuncionarioCreateOutputDto(functionarioDto);
    }

    @Override
    public GfdMFuncionarioUpdateOutputDto updateFuncionario(GfdMFuncionarioUpdateInputDto inputDto) {
        var fornecedorId = inputDto.getFuncionario() != null && inputDto.getFuncionario().getFornecedor() != null ? inputDto.getFuncionario().getFornecedor().getCodigo() : null;
        // busca o fornecedor
        var fornecedor = recuperarFornecedor(inputDto.getCodUsuario(), fornecedorId);

        // quando o usuario vier significa que o utilizador é um fornecedor
        validarPermissaoCriacaoFuncionario(inputDto.getCodUsuario(), fornecedor.getCodUsuario());

        var gfdFuncionarioUpdateInputDto = modelMapper.map(inputDto.getFuncionario(), GfdFuncionarioUpdateInputDto.class);

        gfdFuncionarioUpdateInputDto.setFornecedor(new GfdFuncionarioUpdateInputDto.FornecedorDto(fornecedor.getCodigo()));

        var functionarioDto = modelMapper.map(gfdFuncionarioService.update(gfdFuncionarioUpdateInputDto), GfdMFuncionarioUpdateOutputDto.GfdFuncionarioDto.class);

        return new GfdMFuncionarioUpdateOutputDto(functionarioDto);
    }

    @Override
    public GfdMFuncionarioGetOutputDto getFuncionario(GfdMFuncionarioGetInputDto inputDto) {
        var fornecedorId = inputDto.getFuncionario() != null && inputDto.getFuncionario().getFornecedor() != null ? inputDto.getFuncionario().getFornecedor().getCodigo() : null;

        // busca o fornecedor
        var fornecedor = recuperarFornecedor(inputDto.getCodUsuario(), fornecedorId);

        // quando o usuario vier significa que o utilizador é um fornecedor
        validarPermissaoCriacaoFuncionario(inputDto.getCodUsuario(), fornecedor.getCodUsuario());

        var functionarioDto = modelMapper.map(gfdFuncionarioService.getById(inputDto.getFuncionario().getId()), GfdMFuncionarioGetOutputDto.GfdFuncionarioDto.class);

        return new GfdMFuncionarioGetOutputDto(functionarioDto);
    }

    @Override
    public void deleteFuncionario(GfdMFuncionarioDeleteInputDto inputDto) {
        var fornecedorId = inputDto.getFuncionario() != null && inputDto.getFuncionario().getFornecedor() != null ? inputDto.getFuncionario().getFornecedor().getCodigo() : null;

        // busca o fornecedor
        var fornecedor = recuperarFornecedor(inputDto.getCodUsuario(), fornecedorId);

        // quando o usuario vier significa que o utilizador é um fornecedor
        validarPermissaoCriacaoFuncionario(inputDto.getCodUsuario(), fornecedor.getCodUsuario());

        var gfdFuncionarioDeleteInputDto = modelMapper.map(inputDto.getFuncionario(), GfdFuncionarioDeleteInputDto.class);

        gfdFuncionarioService.delete(gfdFuncionarioDeleteInputDto);
    }


    @Override
    public GfdMDocumentoUpdateOutputDto updateDocumento(GfdMDocumentoUpdateInputDto inputDto) {
        var fornecedorId = inputDto.getFornecedor() != null ? inputDto.getFornecedor().getId() : null;

        // busca o fornecedor
        var fornecedor = recuperarFornecedor(inputDto.getCodUsuario(), fornecedorId);

        // quando o usuario vier significa que o utilizador é um fornecedor
        validarPermissaoCriacaoFuncionario(inputDto.getCodUsuario(), fornecedor.getCodUsuario());

        // atualiza o documento
        var dto = modelMapper.map(inputDto.getDocumento(), GfdDocumentoUpdateInputDto.class);
        var updatedDto = gfdDocumentoService.update(dto);

        // envia o email caso o documento seja nao conforme ou expirado
        if (GfdDocumentoStatusEnum.NAO_CONFORME.name().equals(updatedDto.getStatus().getDescricao()) ||
            GfdDocumentoStatusEnum.EXPIRADO.name().equals(updatedDto.getStatus().getDescricao())) {

            enviarEmail(updatedDto.getId(), fornecedor.getEmail(), updatedDto.getStatus().getDescricao());
        }

        var gfdDocumentoOutDto = modelMapper.map(updatedDto, GfdMDocumentoUpdateOutputDto.GfdDocumentoUpdateOutputDto.class);
        return new GfdMDocumentoUpdateOutputDto(gfdDocumentoOutDto);
    }

    @Override
    public GfdMDocumentoDownloadOutputDto downloadDocumento(GfdMDocumentoDownloadInputDto inputDto) {
        var fornecedorId = inputDto.getFornecedorId();

        // busca o fornecedor
        var fornecedor = recuperarFornecedor(inputDto.getCodUsuario(), fornecedorId);

        // quando o usuario vier significa que o utilizador é um fornecedor
        validarPermissaoCriacaoFuncionario(inputDto.getCodUsuario(), fornecedor.getCodUsuario());

        var dto = modelMapper.map(inputDto, GfdDocumentoDownloadInputDto.class);
        var linkDto = gfdDocumentoService.download(dto);

        return modelMapper.map(linkDto, GfdMDocumentoDownloadOutputDto.class);
    }

    @Override
    public void deleteDocumento(GfdMDocumentoDeleteInputDto inputDto) {
        var fornecedorId = inputDto.getFornecedorId();

        // busca o fornecedor
        var fornecedor = recuperarFornecedor(inputDto.getCodUsuario(), fornecedorId);

        // quando o usuario vier significa que o utilizador é um fornecedor
        validarPermissaoCriacaoFuncionario(inputDto.getCodUsuario(), fornecedor.getCodUsuario());

        var dto = modelMapper.map(inputDto, GfdDocumentoDeleteInputDto.class);
        gfdDocumentoService.delete(dto);
    }

    private void validarPermissaoCriacaoFuncionario(Integer codUsuario, Integer fornecedorCodUsuario) {
        // Se for nulo, assume-se que é um analista
        if (codUsuario == null) return;


        boolean fornecedorSemUsuario = fornecedorCodUsuario == null;

        if (fornecedorSemUsuario) {
            throw new ForbiddenException(
                    GFD_FUNCIONARIO_SEM_PERMISSAO.getMensagem(),
                    GFD_FUNCIONARIO_SEM_PERMISSAO.getCode()
            );
        }

        boolean usuarioDiferente = !fornecedorCodUsuario.equals(codUsuario);

        if (usuarioDiferente) {
            throw new ForbiddenException(
                    GFD_FUNCIONARIO_SEM_PERMISSAO.getMensagem(),
                    GFD_FUNCIONARIO_SEM_PERMISSAO.getCode()
            );
        }
    }


    private void addNonMandatoryDocuments(Set<GfdMVerificarDocumentosOutputDto.DocumentoDto> outputDtoList, List<FindLatestDocumentsGroupedByTipoAndFornecedorIdOutputDto> listDocumento, List<GfdTipoDocumentoGetAllOutputDto> tipoDocumentos) {
        tipoDocumentos.stream()
                .filter(tipoDoc -> !tipoDoc.getObrigatoriedade())
                .forEach(tipoDoc -> {
                    var outputDto = buildGfdMVerificarDocumentoOutpDto("OUTROS", tipoDoc.getNome(), tipoDoc.getId());
                    outputDtoList.add(outputDto);
                });
    }

    private Fornecedor getById(Integer id) {
        var fornecedorGetByIdOutputDto = fornecedorService.getById(id);

        if (fornecedorGetByIdOutputDto == null) {
            throw new NotFoundException(GFD_FORNECEDOR_NAO_ENCONTRADO.getMensagem(), GFD_FORNECEDOR_NAO_ENCONTRADO.getCode());
        }

        return modelMapper.map(fornecedorGetByIdOutputDto, Fornecedor.class);
    }

    private Fornecedor getByCodUsuario(Integer codUsuario) {
        var fornecedorGetByCodUsuarioInputDto = new FornecedorGetByCodUsuarioInputDto(codUsuario);
        var fornecedorGetByCodUsuarioOutputDto = fornecedorService.getByCodUsuario(fornecedorGetByCodUsuarioInputDto);

        if (fornecedorGetByCodUsuarioOutputDto == null) {
            throw new NotFoundException(GFD_FORNECEDOR_NAO_ENCONTRADO.getMensagem(), GFD_FORNECEDOR_NAO_ENCONTRADO.getCode());
        }

        return modelMapper.map(fornecedorGetByCodUsuarioOutputDto, Fornecedor.class);
    }

    private Fornecedor recuperarFornecedor(Integer codUsuario, Integer id) {
        Fornecedor fornecedor;

        if (id != null) {
            fornecedor = getById(id);
        } else if (codUsuario != null) {
            fornecedor = getByCodUsuario(codUsuario);
        } else {
            throw new NotFoundException(GFD_FORNECEDOR_NAO_ENCONTRADO.getMensagem(), GFD_FORNECEDOR_NAO_ENCONTRADO.getCode());
        }

        return fornecedor;
    }

    private void enviarEmail(Integer documentoId, String email, String status) {
        var template = GfdDocumentoEmailTemplate.template(
                status,
                "https://web2.mili.com.br/mili/sisav/timeline/" + documentoId);

        var titulo = "Mili - Documento: " + documentoId + " - " + status;

        var assunto = "Documento: " + documentoId + " - " + status;

        var envioEmail = EnvioEmail.builder()
                .remetente(RemetenteEnum.MILI.getEndereco())
                .destinatario(email)
                .assunto(assunto)
                .titulo(titulo)
                .texto(template)
                .build();

        envioEmailService.enviarFila(envioEmail);
    }


    private void addMandatoryDocuments(Set<GfdMVerificarDocumentosOutputDto.DocumentoDto> outputDtoList, List<FindLatestDocumentsGroupedByTipoAndFornecedorIdOutputDto> documentos, List<GfdTipoDocumentoGetAllOutputDto> tipoDocumentos) {
        tipoDocumentos.stream()
                .filter(GfdTipoDocumentoGetAllOutputDto::getObrigatoriedade)
                .forEach(tipoDoc -> {
                    if (fornecedorHasDocument(documentos, tipoDoc)) {
                        addExistingDocument(outputDtoList, documentos, tipoDoc);
                    } else {
                        var outputDto = buildGfdMVerificarDocumentoOutpDto("NÃO ENVIADO", tipoDoc.getNome(), tipoDoc.getId());
                        outputDtoList.add(outputDto);
                    }
                });
    }

    private void addExistingDocument(Set<GfdMVerificarDocumentosOutputDto.DocumentoDto> outputDtoList, List<FindLatestDocumentsGroupedByTipoAndFornecedorIdOutputDto> documentos, GfdTipoDocumentoGetAllOutputDto tipoDoc) {
        documentos.stream()
                .filter(doc -> doc.getGfdTipoDocumento().getId().equals(tipoDoc.getId()))
                .forEach(doc -> {
                    var outputDto = buildGfdMVerificarDocumentoOutpDto(doc.getStatus().getDescricao(), tipoDoc.getNome(), tipoDoc.getId());
                    outputDtoList.add(outputDto);
                });
    }

    private boolean fornecedorHasDocument(List<FindLatestDocumentsGroupedByTipoAndFornecedorIdOutputDto> documentos, GfdTipoDocumentoGetAllOutputDto tipoDoc) {
        return documentos.stream()
                .anyMatch(doc -> doc.getGfdTipoDocumento().getId().equals(tipoDoc.getId()));
    }

    private GfdMVerificarDocumentosOutputDto.DocumentoDto buildGfdMVerificarDocumentoOutpDto(String status, String nome, Integer idTipoDocumento) {
        var outputDto = new GfdMVerificarDocumentosOutputDto.DocumentoDto();
        outputDto.setStatus(status);
        outputDto.setNome(nome);
        outputDto.setIdTipoDocumento(idTipoDocumento);
        return outputDto;
    }

    List<GfdTipoDocumentoGetAllOutputDto> getFornecedorTipoDocumentos(Integer funcionarioId) {
        var tipoDocumentoInputDto = new GfdTipoDocumentoGetAllInputDto(GfdTipoDocumentoTipoEnum.FORNECEDOR);

        if (funcionarioId != null) {
            // pega as informacoes de funcionario
            var funcionario = getGfdFuncionarioGetByIdOutputDto(funcionarioId);

            if (funcionario.getTipoContratacao().equals(GfdFuncionarioTipoContratacaoEnum.CLT.getDescricao())) {
                tipoDocumentoInputDto.setTipo(GfdTipoDocumentoTipoEnum.FUNCIONARIO_CLT);
            } else {
                tipoDocumentoInputDto.setTipo(GfdTipoDocumentoTipoEnum.FUNCIONARIO_MEI);
            }
        }

        return gfdTipoDocumentoService.getAll(tipoDocumentoInputDto);
    }

    private GfdMDocumentosGetAllOutputDto.GfdTipoDocumentoDto criarDtoTipoDocumentoParaResposta(GfdMDocumentosGetAllInputDto inputDto, List<GfdMDocumentosGetAllOutputDto.GfdDocumentoDto> gfdDocumentoDto) {
        var gfdTipoDocumentoDto = new GfdMDocumentosGetAllOutputDto.GfdTipoDocumentoDto();

        if (!gfdDocumentoDto.isEmpty()) {
            gfdTipoDocumentoDto.setId(gfdDocumentoDto.get(0).getGfdTipoDocumento().getId());
            gfdTipoDocumentoDto.setNome(gfdDocumentoDto.get(0).getGfdTipoDocumento().getNome());
            gfdTipoDocumentoDto.setDiasValidade(gfdDocumentoDto.get(0).getGfdTipoDocumento().getDiasValidade());
        } else if (inputDto.getTipoDocumentoId() != null) {
            var tipoDocumento = gfdTipoDocumentoService.getById(inputDto.getTipoDocumentoId());
            gfdTipoDocumentoDto.setId(tipoDocumento.getId());
            gfdTipoDocumentoDto.setNome(tipoDocumento.getNome());
            gfdTipoDocumentoDto.setDiasValidade(tipoDocumento.getDiasValidade());

        }
        return gfdTipoDocumentoDto;
    }


    private boolean representanteCadastrado(Fornecedor fornecedor) {
        return fornecedor.getContato() != null &&
               fornecedor.getEmail() != null &&
               fornecedor.getCelular() != null;
    }
}
