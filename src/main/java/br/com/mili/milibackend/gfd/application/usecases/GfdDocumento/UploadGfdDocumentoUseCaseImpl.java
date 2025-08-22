package br.com.mili.milibackend.gfd.application.usecases.GfdDocumento;

import br.com.mili.milibackend.fornecedor.domain.entity.Fornecedor;
import br.com.mili.milibackend.fornecedor.domain.usecases.GetFornecedorByCodOrIdUseCase;
import br.com.mili.milibackend.gfd.application.dto.GfdMUploadDocumentoInputDto;
import br.com.mili.milibackend.gfd.application.dto.GfdMUploadDocumentoOutputDto;
import br.com.mili.milibackend.gfd.application.dto.fileprocess.DocumentoFileData;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoCreateInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoCreateOutputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumentoPeriodo.GfdDocumentoPeriodoCreateInputDto;
import br.com.mili.milibackend.gfd.domain.entity.GfdDocumentoStatusEnum;
import br.com.mili.milibackend.gfd.domain.entity.GfdTipoDocumento;
import br.com.mili.milibackend.gfd.domain.entity.GfdTipoDocumentoTipoEnum;
import br.com.mili.milibackend.gfd.domain.interfaces.FileProcessingService;
import br.com.mili.milibackend.gfd.domain.usecases.CreateDocumentoPeriodoUseCase;
import br.com.mili.milibackend.gfd.domain.usecases.CreateDocumentoUseCase;
import br.com.mili.milibackend.gfd.domain.usecases.UploadGfdDocumentoUseCase;
import br.com.mili.milibackend.gfd.infra.repository.GfdTipoDocumentoRepository;
import br.com.mili.milibackend.shared.enums.MimeTypeEnum;
import br.com.mili.milibackend.shared.exception.types.BadRequestException;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import br.com.mili.milibackend.shared.infra.aws.IS3Service;
import br.com.mili.milibackend.shared.infra.aws.StorageFolderEnum;
import br.com.mili.milibackend.shared.infra.aws.dto.AttachmentDto;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static br.com.mili.milibackend.gfd.adapter.exception.GfdMCodeException.GFD_FORNECEDOR_NAO_ENCONTRADO;
import static br.com.mili.milibackend.gfd.adapter.exception.GfdMCodeException.GFD_TIPO_DOCUMENTO_FUNCIONARIO_BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class UploadGfdDocumentoUseCaseImpl implements UploadGfdDocumentoUseCase {
    private final GfdTipoDocumentoRepository gfdTipoDocumentoRepository;
    private final CreateDocumentoUseCase createDocumentoUseCase;
    private final FileProcessingService fileProcessingService;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final IS3Service s3Service;
    private final GetFornecedorByCodOrIdUseCase getFornecedorByCodOrIdUseCase;
    private final CreateDocumentoPeriodoUseCase createDocumentoPeriodoUseCase;


    @Override
    public GfdMUploadDocumentoOutputDto execute(GfdMUploadDocumentoInputDto inputDto) {
        var gfdDocumentoDto = inputDto.getGfdDocumento();
        var base64File = gfdDocumentoDto.getBase64File().file();
        var base64FileName = gfdDocumentoDto.getBase64File().fileName();

        var fornecedor = getFornecedorByCodOrIdUseCase.execute(inputDto.getCodUsuario(), inputDto.getId());

        var tipoDocumento = recuperarTipoDocumento(inputDto);

        validarCompatibilidadeTipoDocumento(inputDto, tipoDocumento);

        DocumentoFileData documentoFileData = fileProcessingService.processFile(base64File, base64FileName, MimeTypeEnum.PDF);

        var gfdDocumentoCreateOutputDto = createGfdDocumento(inputDto, tipoDocumento, fornecedor, documentoFileData, gfdDocumentoDto, base64File);

        uploadFile(base64File, documentoFileData);

        createPeriodos(inputDto, gfdDocumentoCreateOutputDto.getId(), tipoDocumento);

        return modelMapper.map(gfdDocumentoCreateOutputDto, GfdMUploadDocumentoOutputDto.class);
    }

    private void createPeriodos(GfdMUploadDocumentoInputDto inputDto, Integer id, GfdTipoDocumento tipoDocumento) {
        var periodoCreateDto = GfdDocumentoPeriodoCreateInputDto.builder()
                .periodo(inputDto.getGfdDocumentoPeriodo() != null ? inputDto.getGfdDocumentoPeriodo().getPeriodo() : null)
                .documento(id, inputDto.getGfdDocumento().getDataEmissao(),  inputDto.getGfdDocumento().getDataValidade())
                .tipoDocumento(tipoDocumento.getId(), tipoDocumento.getClassificacao(), tipoDocumento.getDiasValidade())
                .build();

        createDocumentoPeriodoUseCase.execute(periodoCreateDto);
    }

    private void uploadFile(String base64File, DocumentoFileData documentoFileData) {
        var attachmentDtoModified = new AttachmentDto(base64File, documentoFileData.getNomeArquivo());
        var json = gson.toJson(attachmentDtoModified);

        s3Service.upload(StorageFolderEnum.GFD, json);
    }

    private GfdDocumentoCreateOutputDto createGfdDocumento(
            GfdMUploadDocumentoInputDto inputDto,
            GfdTipoDocumento tipoDocumento,
            Fornecedor fornecedor,
            DocumentoFileData documentoFileData,
            GfdMUploadDocumentoInputDto.GfdDocumentoDto gfdDocumentoDto,
            String base64File
    ) {
        var createDocumentoInputDto = buildCreateDocumentoInputDto(inputDto, tipoDocumento, fornecedor, documentoFileData, gfdDocumentoDto);

        var gfdDocumentoCreateInputDto = GfdDocumentoCreateInputDto.builder()
                .gfdDocumentoDto(createDocumentoInputDto)
                .base64File(base64File).build();

        return createDocumentoUseCase.execute(gfdDocumentoCreateInputDto);
    }

    private GfdDocumentoCreateInputDto.GfdDocumentoDto buildCreateDocumentoInputDto(
            GfdMUploadDocumentoInputDto inputDto,
            GfdTipoDocumento tipoDocumento,
            Fornecedor fornecedor,
            DocumentoFileData documentoFileData,
            GfdMUploadDocumentoInputDto.GfdDocumentoDto gfdDocumentoDto
    ) {
        var gfdTipoDocumentoDto = new GfdDocumentoCreateInputDto.GfdDocumentoDto.GfdTipoDocumentoDto(tipoDocumento.getId());

        var gfdDocumentoInputDto = GfdDocumentoCreateInputDto.GfdDocumentoDto.builder()
                .ctforCodigo(fornecedor.getCodigo())
                .nomeArquivo(documentoFileData.getNomeArquivo()).nomeArquivoPath("gfd/" + documentoFileData.getNomeArquivo())
                .tamanhoArquivo(documentoFileData.getTamanho())
                .dataCadastro(LocalDate.now())
                .tipoArquivo(documentoFileData.getMimeType())
                .dataEmissao(gfdDocumentoDto.getDataEmissao())
                .dataValidade(gfdDocumentoDto.getDataValidade())
                .usuario(inputDto.getUsuario())
                .status(GfdDocumentoStatusEnum.ENVIADO)
                .gfdTipoDocumento(gfdTipoDocumentoDto);

        if (inputDto.getFuncionario() != null) {
            gfdDocumentoInputDto.gfdFuncionario(new GfdDocumentoCreateInputDto.GfdDocumentoDto.GfdFuncionarioDto(inputDto.getFuncionario().getId()));
        }

        return gfdDocumentoInputDto.build();
    }

    private void validarCompatibilidadeTipoDocumento(GfdMUploadDocumentoInputDto inputDto, GfdTipoDocumento tipoDocumento) {
        if (inputDto.getFuncionario() != null && tipoDocumento.getTipo() == GfdTipoDocumentoTipoEnum.FORNECEDOR) {
            throw new BadRequestException(
                    GFD_TIPO_DOCUMENTO_FUNCIONARIO_BAD_REQUEST.getMensagem(),
                    GFD_TIPO_DOCUMENTO_FUNCIONARIO_BAD_REQUEST.getCode()
            );
        }
    }

    private GfdTipoDocumento recuperarTipoDocumento(GfdMUploadDocumentoInputDto inputDto) {
        return gfdTipoDocumentoRepository.findById(inputDto.getGfdTipoDocumento().getId())
                .orElseThrow(() ->
                        new NotFoundException(GFD_FORNECEDOR_NAO_ENCONTRADO.getMensagem(), GFD_FORNECEDOR_NAO_ENCONTRADO.getCode())
                );
    }
}
