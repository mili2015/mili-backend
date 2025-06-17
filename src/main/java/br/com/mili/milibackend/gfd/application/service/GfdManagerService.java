package br.com.mili.milibackend.gfd.application.service;

import br.com.mili.milibackend.fornecedor.application.dto.FornecedorGetByCodUsuarioInputDto;
import br.com.mili.milibackend.fornecedor.application.dto.gfdDocumento.FindLatestDocumentsGroupedByTipoAndFornecedorIdOutputDto;
import br.com.mili.milibackend.fornecedor.application.dto.gfdDocumento.GfdDocumentoCreateInputDto;
import br.com.mili.milibackend.fornecedor.application.dto.gfdDocumento.GfdDocumentoGetAllInputDto;
import br.com.mili.milibackend.fornecedor.application.dto.gfdTipoDocumento.GfdTipoDocumentoGetAllInputDto;
import br.com.mili.milibackend.fornecedor.application.dto.gfdTipoDocumento.GfdTipoDocumentoGetAllOutputDto;
import br.com.mili.milibackend.fornecedor.domain.entity.Fornecedor;
import br.com.mili.milibackend.fornecedor.domain.entity.GfdDocumentoStatusEnum;
import br.com.mili.milibackend.fornecedor.domain.entity.GfdTipoDocumentoTipoEnum;
import br.com.mili.milibackend.fornecedor.domain.interfaces.service.IFornecedorService;
import br.com.mili.milibackend.fornecedor.domain.interfaces.service.IGfdTipoDocumentoService;
import br.com.mili.milibackend.gfd.application.dto.*;
import br.com.mili.milibackend.gfd.domain.interfaces.IGfdDocumentoService;
import br.com.mili.milibackend.gfd.domain.interfaces.IGfdManagerService;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
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

import static br.com.mili.milibackend.gfd.adapter.exception.GfdCodeException.GFD_FORNECEDOR_NAO_ENCONTRADO;

@Service
public class GfdManagerService implements IGfdManagerService {

    private final IFornecedorService fornecedorService;
    private final IGfdTipoDocumentoService GfdTipoDocumentoService;
    private final IGfdDocumentoService gfdDocumentoService;
    private final ModelMapper modelMapper;
    private final Tika tika;

    public GfdManagerService(IFornecedorService fornecedorService, IGfdTipoDocumentoService GfdTipoDocumentoService, IGfdDocumentoService gfdDocumentoService, ModelMapper modelMapper, Tika tika) {
        this.fornecedorService = fornecedorService;
        this.GfdTipoDocumentoService = GfdTipoDocumentoService;
        this.gfdDocumentoService = gfdDocumentoService;
        this.modelMapper = modelMapper;
        this.tika = tika;
    }

    @Override
    public GfdVerificarFornecedorOutputDto verifyFornecedor(GfdVerificarFornecedorInputDto inputDto) {
        var fornecedor = recuperarFornecedor(inputDto.getCodUsuario(), inputDto.getId());

        String razaoSocial = fornecedor.getRazaoSocial();

        return new GfdVerificarFornecedorOutputDto(
                representanteCadastrado(fornecedor),
                razaoSocial != null ? razaoSocial : "Bem-Vindo"
        );
    }

    private Fornecedor recuperarFornecedor(Integer codUsuario, Integer id) {
        Fornecedor fornecedor = null;

        if (id != null) {
            var fornecedorGetByIdOutputDto = fornecedorService.getById(id);

            if (fornecedorGetByIdOutputDto == null) {
                throw new NotFoundException(GFD_FORNECEDOR_NAO_ENCONTRADO.getMensagem(), GFD_FORNECEDOR_NAO_ENCONTRADO.getCode());
            }

            fornecedor = modelMapper.map(fornecedorGetByIdOutputDto, Fornecedor.class);

        } else if (codUsuario != null) {
            var fornecedorGetByCodUsuarioInputDto = new FornecedorGetByCodUsuarioInputDto(codUsuario);
            var fornecedorGetByCodUsuarioOutputDto = fornecedorService.getByCodUsuario(fornecedorGetByCodUsuarioInputDto);

            if (fornecedorGetByCodUsuarioOutputDto == null) {
                throw new NotFoundException(GFD_FORNECEDOR_NAO_ENCONTRADO.getMensagem(), GFD_FORNECEDOR_NAO_ENCONTRADO.getCode());
            }

            fornecedor = modelMapper.map(fornecedorGetByCodUsuarioOutputDto, Fornecedor.class);
        }

        return fornecedor;
    }

    @Override
    public List<GfdVerificarDocumentosOutputDto> verifyDocumentos(@Valid GfdVerificarDocumentosInputDto inputDto) {
        var listVerificarDocumentosOutputDto = new LinkedHashSet<GfdVerificarDocumentosOutputDto>();

        var fornecedor = recuperarFornecedor(inputDto.getCodUsuario(), inputDto.getId());

        var latestDocuments = gfdDocumentoService.findLatestDocumentsGroupedByTipoAndFornecedorId(fornecedor.getCodigo());

        var fornecedorTipoDocumentos = getFornecedorTipoDocumentos();

        addNonMandatoryDocuments(listVerificarDocumentosOutputDto, latestDocuments, fornecedorTipoDocumentos);
        addMandatoryDocuments(listVerificarDocumentosOutputDto, latestDocuments, fornecedorTipoDocumentos);

        return listVerificarDocumentosOutputDto.stream().toList();

    }

    private Fornecedor recuperarForncedor(Integer inputDto, Integer inputDto1) {
        var fornecedor = recuperarFornecedor(inputDto, inputDto1);

        if (fornecedor == null) {
            throw new NotFoundException(GFD_FORNECEDOR_NAO_ENCONTRADO.getMensagem(), GFD_FORNECEDOR_NAO_ENCONTRADO.getCode());
        }
        return fornecedor;
    }

    @Override
    public GfdFornecedorGetOutputDto getFornecedor(GfdFornecedorGetInputDto inputDto) {
        var fornecedor = recuperarForncedor(inputDto.getCodUsuario(), inputDto.getId());

        return modelMapper.map(fornecedor, GfdFornecedorGetOutputDto.class);
    }

    @Override
    public GfdUploadDocumentoOutputDto uploadDocumento(GfdUploadDocumentoInputDto inputDto) {
        var listGfdDocumentoOutputDto = new ArrayList<GfdUploadDocumentoOutputDto.GfdTipoDocumentoDto>();

        var fornecedor = recuperarForncedor(inputDto.getCodUsuario(), inputDto.getId());

        // recupera o tipo do documento
        // *posteriormente* verifica se foi enviado o cod do funcionario caso foi enviado sera adicionado como funcionario
        var tipoDocumento = GfdTipoDocumentoService.getById(inputDto.getGfdTipoDocumento().getId());


        //salva o arquivo no banco
        var listGfdDocumentoDto = inputDto.getListGfdDocumento();

        for (GfdUploadDocumentoInputDto.GfdDocumentoDto gfdDocumentoDto : listGfdDocumentoDto) {
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
                    .gfdTipoDocumento(gfdTipoDocumentoDto)
                    .build();

            var gfdDocumentoCreateInputDto = GfdDocumentoCreateInputDto.builder()
                    .gfdDocumentoDto(gfdDocumentoInputDto)
                    .base64File(base64File)
                    .build();

            var gfdDocumentoCreateOutputDto = gfdDocumentoService.create(gfdDocumentoCreateInputDto);

            listGfdDocumentoOutputDto.add(modelMapper.map(gfdDocumentoCreateOutputDto, GfdUploadDocumentoOutputDto.GfdTipoDocumentoDto.class));
        }

        return new GfdUploadDocumentoOutputDto(listGfdDocumentoOutputDto);
    }

    @Override
    public GfdDocumentosGetAllOutputDto getAllDocumentos(GfdDocumentosGetAllInputDto inputDto) {
        var fornecedor = recuperarForncedor(inputDto.getCodUsuario(), inputDto.getId());

        var gfdDocumentoGetAllInputDto = modelMapper.map(inputDto, GfdDocumentoGetAllInputDto.class);
        gfdDocumentoGetAllInputDto.setCtforCodigo(fornecedor.getCodigo());

        var pageGfdDocumentoService = gfdDocumentoService.getAll(gfdDocumentoGetAllInputDto);

        var gfdDocumentoDto = pageGfdDocumentoService.getContent().stream()
                .map(gfdDocumento -> {
                    gfdDocumento.setNomeArquivo(CleanFileName.clear(gfdDocumento.getNomeArquivo()));
                    return modelMapper.map(gfdDocumento, GfdDocumentosGetAllOutputDto.GfdDocumentoDto.class);
                })
                .toList();
        var pageGfdDocumentoDto = new PageBaseImpl<>(gfdDocumentoDto, pageGfdDocumentoService.getPage(), pageGfdDocumentoService.getSize(), pageGfdDocumentoService.getTotalElements()) {
        };

        var todosTiposDocumentoFornecedor = getFornecedorTipoDocumentos();

        // mostra doc anterior e doc proximo
        Integer idDocumento = inputDto.getTipoDocumentoId();

        var indexof = todosTiposDocumentoFornecedor.stream()
                .filter(tipo -> tipo.getId().equals(idDocumento))
                .findFirst()
                .map(todosTiposDocumentoFornecedor::indexOf)
                .orElse(-1);

        var nextDoc = 0;
        var previusDoc = 0;

        if (!todosTiposDocumentoFornecedor.isEmpty()) {
            var lastTipoDocumentoId = getFornecedorTipoDocumentos().get(getFornecedorTipoDocumentos().size() - 1).getId();
            var isLastTipoDocumentoId = lastTipoDocumentoId <= idDocumento;

            nextDoc = isLastTipoDocumentoId ? 0 : todosTiposDocumentoFornecedor.get(indexof + 1).getId();
            previusDoc = indexof > 0 ? todosTiposDocumentoFornecedor.get(indexof - 1).getId() : 0;
        }

        // adiciona os tipos documento
        var gfdTipoDocumentoDto = popularTipoDocumetoDto(inputDto, gfdDocumentoDto);

        return GfdDocumentosGetAllOutputDto.builder()
                .gfdTipoDocumento(gfdTipoDocumentoDto)
                .gfdDocumento(pageGfdDocumentoDto)
                .nextDoc(nextDoc)
                .previousDoc(previusDoc)
                .build();
    }

    private void addNonMandatoryDocuments(Set<GfdVerificarDocumentosOutputDto> outputDtoList, List<FindLatestDocumentsGroupedByTipoAndFornecedorIdOutputDto> listDocumento, List<GfdTipoDocumentoGetAllOutputDto> tipoDocumentos) {
        tipoDocumentos.stream()
                .filter(tipoDoc -> !tipoDoc.getObrigatoriedade())
                .forEach(tipoDoc -> {
                    var outputDto = createOutputDto("OUTROS", tipoDoc.getNome(), tipoDoc.getId());
                    outputDtoList.add(outputDto);
                });
    }

    private void addMandatoryDocuments(Set<GfdVerificarDocumentosOutputDto> outputDtoList, List<FindLatestDocumentsGroupedByTipoAndFornecedorIdOutputDto> documentos, List<GfdTipoDocumentoGetAllOutputDto> tipoDocumentos) {
        tipoDocumentos.stream()
                .filter(GfdTipoDocumentoGetAllOutputDto::getObrigatoriedade)
                .forEach(tipoDoc -> {
                    if (fornecedorHasDocument(documentos, tipoDoc)) {
                        addExistingDocument(outputDtoList, documentos, tipoDoc);
                    } else {
                        var outputDto = createOutputDto("NÃO ENVIADO", tipoDoc.getNome(), tipoDoc.getId());
                        outputDtoList.add(outputDto);
                    }
                });
    }

    private void addExistingDocument(Set<GfdVerificarDocumentosOutputDto> outputDtoList, List<FindLatestDocumentsGroupedByTipoAndFornecedorIdOutputDto> documentos, GfdTipoDocumentoGetAllOutputDto tipoDoc) {
        documentos.stream()
                .filter(doc -> doc.getGfdTipoDocumento().getId().equals(tipoDoc.getId()))
                .forEach(doc -> {
                    var outputDto = createOutputDto(doc.getStatus().getDescricao(), tipoDoc.getNome(), tipoDoc.getId());
                    outputDtoList.add(outputDto);
                });
    }

    private boolean fornecedorHasDocument(List<FindLatestDocumentsGroupedByTipoAndFornecedorIdOutputDto> documentos, GfdTipoDocumentoGetAllOutputDto tipoDoc) {
        return documentos.stream()
                .anyMatch(doc -> doc.getGfdTipoDocumento().getId().equals(tipoDoc.getId()));
    }

    private GfdVerificarDocumentosOutputDto createOutputDto(String status, String nome, Integer idTipoDocumento) {
        var outputDto = new GfdVerificarDocumentosOutputDto();
        outputDto.setStatus(status);
        outputDto.setNome(nome);
        outputDto.setIdTipoDocumento(idTipoDocumento);
        return outputDto;
    }

    private List<GfdTipoDocumentoGetAllOutputDto> getFornecedorTipoDocumentos() {
        var tipoDocumentoInputDto = new GfdTipoDocumentoGetAllInputDto(GfdTipoDocumentoTipoEnum.FORNECEDOR);
        return GfdTipoDocumentoService.getAll(tipoDocumentoInputDto);
    }

    private GfdDocumentosGetAllOutputDto.GfdTipoDocumentoDto popularTipoDocumetoDto(GfdDocumentosGetAllInputDto inputDto, List<GfdDocumentosGetAllOutputDto.GfdDocumentoDto> gfdDocumentoDto) {
        var gfdTipoDocumentoDto = new GfdDocumentosGetAllOutputDto.GfdTipoDocumentoDto();

        if (!gfdDocumentoDto.isEmpty()) {
            gfdTipoDocumentoDto.setId(gfdDocumentoDto.get(0).getGfdTipoDocumento().getId());
            gfdTipoDocumentoDto.setNome(gfdDocumentoDto.get(0).getGfdTipoDocumento().getNome());
            gfdTipoDocumentoDto.setDiasValidade(gfdDocumentoDto.get(0).getGfdTipoDocumento().getDiasValidade());
        } else if (inputDto.getTipoDocumentoId() != null) {
            var tipoDocumento = GfdTipoDocumentoService.getById(inputDto.getTipoDocumentoId());
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
