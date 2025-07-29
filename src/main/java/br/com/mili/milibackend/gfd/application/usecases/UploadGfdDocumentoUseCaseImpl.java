package br.com.mili.milibackend.gfd.application.usecases;

import br.com.mili.milibackend.fornecedor.domain.entity.Fornecedor;
import br.com.mili.milibackend.fornecedor.infra.repository.fornecedorRepository.FornecedorRepository;
import br.com.mili.milibackend.gfd.application.dto.GfdMUploadDocumentoInputDto;
import br.com.mili.milibackend.gfd.application.dto.GfdMUploadDocumentoOutputDto;
import br.com.mili.milibackend.gfd.application.dto.fileprocess.DocumentoFileData;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoCreateInputDto;
import br.com.mili.milibackend.gfd.domain.entity.GfdDocumentoStatusEnum;
import br.com.mili.milibackend.gfd.domain.entity.GfdTipoDocumento;
import br.com.mili.milibackend.gfd.domain.entity.GfdTipoDocumentoTipoEnum;
import br.com.mili.milibackend.gfd.domain.interfaces.FileProcessingService;
import br.com.mili.milibackend.gfd.domain.usecases.CreateDocumentoUseCase;
import br.com.mili.milibackend.gfd.domain.usecases.UploadGfdDocumentoUseCase;
import br.com.mili.milibackend.gfd.infra.repository.GfdTipoDocumentoRepository;
import br.com.mili.milibackend.shared.exception.types.BadRequestException;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static br.com.mili.milibackend.gfd.adapter.exception.GfdMCodeException.GFD_FORNECEDOR_NAO_ENCONTRADO;
import static br.com.mili.milibackend.gfd.adapter.exception.GfdMCodeException.GFD_TIPO_DOCUMENTO_FUNCIONARIO_BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class UploadGfdDocumentoUseCaseImpl implements UploadGfdDocumentoUseCase {
    private final FornecedorRepository fornecedorRepository;
    private final GfdTipoDocumentoRepository gfdTipoDocumentoRepository;
    private final CreateDocumentoUseCase createDocumentoUseCase;
    private final FileProcessingService fileProcessingService;
    private final ModelMapper modelMapper;

    @Override
    public GfdMUploadDocumentoOutputDto execute(GfdMUploadDocumentoInputDto inputDto) {
        var listGfdDocumentoOutputDto = new ArrayList<GfdMUploadDocumentoOutputDto.GfdTipoDocumentoDto>();

        var fornecedor = recuperarFornecedor(inputDto.getCodUsuario(), inputDto.getId());

        // recupera o tipo do documento
        var tipoDocumento = gfdTipoDocumentoRepository.findById(inputDto.getGfdTipoDocumento().getId())
                .orElseThrow(() ->
                        new NotFoundException(GFD_FORNECEDOR_NAO_ENCONTRADO.getMensagem(), GFD_FORNECEDOR_NAO_ENCONTRADO.getCode())
                );

        //valida se caso o usuario realmente enviou corretamente o tipo de documento
        if (inputDto.getFuncionario() != null && tipoDocumento.getTipo() == GfdTipoDocumentoTipoEnum.FORNECEDOR) {
            throw new BadRequestException(
                    GFD_TIPO_DOCUMENTO_FUNCIONARIO_BAD_REQUEST.getMensagem(),
                    GFD_TIPO_DOCUMENTO_FUNCIONARIO_BAD_REQUEST.getCode()
            );
        }

        //salva o arquivo no banco
        var listGfdDocumentoDto = inputDto.getListGfdDocumento();

        salvarListaDocumento(inputDto, listGfdDocumentoDto, tipoDocumento, fornecedor, listGfdDocumentoOutputDto);

        return new GfdMUploadDocumentoOutputDto(listGfdDocumentoOutputDto);
    }

    private void salvarListaDocumento(GfdMUploadDocumentoInputDto inputDto, List<GfdMUploadDocumentoInputDto.GfdDocumentoDto> listGfdDocumentoDto, GfdTipoDocumento tipoDocumento, Fornecedor fornecedor, ArrayList<GfdMUploadDocumentoOutputDto.GfdTipoDocumentoDto> listGfdDocumentoOutputDto) {
        for (GfdMUploadDocumentoInputDto.GfdDocumentoDto gfdDocumentoDto : listGfdDocumentoDto) {
            var base64File = gfdDocumentoDto.getBase64File().file();
            var base64FileName = gfdDocumentoDto.getBase64File().fileName();

            DocumentoFileData documentoFileData = fileProcessingService.processFile(base64File, base64FileName);

            //salva no banco
            var gfdDocumentoInputDto = buildDocumentoInput(inputDto, gfdDocumentoDto, tipoDocumento, fornecedor, documentoFileData);

            // adiciona o funcionario
            if (inputDto.getFuncionario() != null) {
                gfdDocumentoInputDto.gfdFuncionario(new GfdDocumentoCreateInputDto.GfdDocumentoDto.GfdFuncionarioDto(inputDto.getFuncionario().getId()));
            }

            var gfdDocumentoCreateInputDto = GfdDocumentoCreateInputDto.builder().gfdDocumentoDto(gfdDocumentoInputDto.build()).base64File(base64File).build();

            var gfdDocumentoCreateOutputDto = createDocumentoUseCase.execute(gfdDocumentoCreateInputDto);

            listGfdDocumentoOutputDto.add(modelMapper.map(gfdDocumentoCreateOutputDto, GfdMUploadDocumentoOutputDto.GfdTipoDocumentoDto.class));
        }
    }

    private GfdDocumentoCreateInputDto.GfdDocumentoDto.GfdDocumentoDtoBuilder buildDocumentoInput(GfdMUploadDocumentoInputDto inputDto, GfdMUploadDocumentoInputDto.GfdDocumentoDto gfdDocumentoDto, GfdTipoDocumento tipoDocumento, Fornecedor fornecedor, DocumentoFileData documentoFileData) {
        var gfdTipoDocumentoDto = new GfdDocumentoCreateInputDto.GfdDocumentoDto.GfdTipoDocumentoDto(tipoDocumento.getId());

        return GfdDocumentoCreateInputDto.GfdDocumentoDto.builder()
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
    }

    private Fornecedor recuperarFornecedor(Integer codUsuario, Integer id) {
        Fornecedor fornecedor = null;

        if (id != null) {
            fornecedor = fornecedorRepository.findById(id).orElse(null);
        } else if (codUsuario != null) {
            fornecedor = fornecedorRepository.findByCodUsuario(codUsuario).orElse(null);
        }

        if (fornecedor == null) {
            throw new NotFoundException(GFD_FORNECEDOR_NAO_ENCONTRADO.getMensagem(), GFD_FORNECEDOR_NAO_ENCONTRADO.getCode());
        }

        return fornecedor;
    }
}
