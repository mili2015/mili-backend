package br.com.mili.milibackend.gfd.application.usecases.GfdDocumento;

import br.com.mili.milibackend.fornecedor.domain.entity.Fornecedor;
import br.com.mili.milibackend.fornecedor.domain.usecases.GetFornecedorByCodOrIdUseCase;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdTipoDocumento.GfdTipoDocumentoGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdTipoDocumento.GfdTipoDocumentoGetAllOutputDto;
import br.com.mili.milibackend.gfd.application.dto.manager.documentos.GfdMDocumentosGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.manager.documentos.GfdMDocumentosGetAllOutputDto;
import br.com.mili.milibackend.gfd.application.helpers.GfdTipoDocumentoNavigationHelper;
import br.com.mili.milibackend.gfd.domain.entity.GfdFuncionario;
import br.com.mili.milibackend.gfd.domain.entity.GfdTipoDocumento;
import br.com.mili.milibackend.gfd.domain.entity.GfdTipoDocumentoTipoClassificacaoEnum;
import br.com.mili.milibackend.gfd.domain.usecases.GetAllGfdDocumentosUseCase;
import br.com.mili.milibackend.gfd.domain.usecases.GetAllSupplierDocumentsUseCase;
import br.com.mili.milibackend.gfd.domain.usecases.GetAllTipoDocumentoUseCase;
import br.com.mili.milibackend.gfd.infra.repository.GfdDocumentoPeriodoRepository;
import br.com.mili.milibackend.gfd.infra.repository.GfdTipoDocumentoRepository;
import br.com.mili.milibackend.gfd.infra.repository.gfdFuncionario.GfdFuncionarioRepository;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import br.com.mili.milibackend.shared.page.pagination.PageBaseImpl;
import br.com.mili.milibackend.shared.util.CleanFileName;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.mili.milibackend.gfd.adapter.exception.GfdFuncionarioCodeException.GFD_FUNCIONARIO_NAO_ENCONTRADO;
import static br.com.mili.milibackend.gfd.adapter.exception.GfdMCodeException.GFD_TIPO_DOCUMENTO_NAO_ENCONTRADO;

@Service
@RequiredArgsConstructor
public class GetAllSupplierDocumentsUseCaseImpl implements GetAllSupplierDocumentsUseCase {
    private final GfdFuncionarioRepository funcionarioRepository;
    private final GfdDocumentoPeriodoRepository gfdDocumentoPeriodoRepository;
    private final GfdTipoDocumentoRepository gfdTipoDocumentoRepository;
    private final GetAllGfdDocumentosUseCase getAllGfdDocumentosUseCase;
    private final GetAllTipoDocumentoUseCase getAllTipoDocumentoUseCase;
    private final ModelMapper modelMapper;
    private final GetFornecedorByCodOrIdUseCase getFornecedorByCodOrIdUseCase;


    @Override
    public GfdMDocumentosGetAllOutputDto execute(GfdMDocumentosGetAllInputDto inputDto) {
        var fornecedor = getFornecedorByCodOrIdUseCase.execute(inputDto.getCodUsuario(), inputDto.getId());

        var tipoDocumento = recuperarTipoDocumento(inputDto);

        var pageDocumentos = recuperarDocumentos(inputDto, fornecedor);

        var recuperarTiposDocumentoFornecedor = getFornecedorTipoDocumentos(inputDto.getFuncionario() != null ? inputDto.getFuncionario().getId() : null, fornecedor);

        var navigation = GfdTipoDocumentoNavigationHelper.calculateNavigation(recuperarTiposDocumentoFornecedor, inputDto.getTipoDocumentoId());

        // adicionar na dto o periodo caso seja tipo documento competencia
        adicionarPeriodoDeDocumentoCompetencia(tipoDocumento, pageDocumentos);

        // adiciona os tipos documento
        var gfdTipoDocumentoDto = criarDtoTipoDocumentoParaResposta(tipoDocumento);

        return GfdMDocumentosGetAllOutputDto.builder().gfdTipoDocumento(gfdTipoDocumentoDto).gfdDocumento(pageDocumentos).nextDoc(navigation.nextDoc()).previousDoc(navigation.prevDoc()).build();
    }

    private void adicionarPeriodoDeDocumentoCompetencia(GfdTipoDocumento tipoDocumento, PageBaseImpl<GfdMDocumentosGetAllOutputDto.GfdDocumentoDto> pageDocumentos) {
        if (tipoDocumento.getClassificacao() != null && tipoDocumento.getClassificacao().equals(GfdTipoDocumentoTipoClassificacaoEnum.COMPETENCIA.name())) {
            var ids = pageDocumentos.getContent().stream().map(GfdMDocumentosGetAllOutputDto.GfdDocumentoDto::getId).toList();

            var periodos = gfdDocumentoPeriodoRepository.findByGfdDocumento_IdIn(ids);

            pageDocumentos.getContent().forEach(documento -> periodos.stream()
                    .filter(gfdPeriodo -> gfdPeriodo.getGfdDocumento().getId().equals(documento.getId()))
                    .findFirst()
                    .ifPresent(periodo -> {
                        var gfdDocumentoPeriodoDto = new GfdMDocumentosGetAllOutputDto.GfdDocumentoDto.GfdDocumentoPeriodoDto();
                        gfdDocumentoPeriodoDto.setPeriodo(periodo.getPeriodoFinal());
                        gfdDocumentoPeriodoDto.setId(periodo.getId());

                        documento.setGfdDocumentoPeriodo(gfdDocumentoPeriodoDto);
                    }));
        }
    }

    private PageBaseImpl<GfdMDocumentosGetAllOutputDto.GfdDocumentoDto> recuperarDocumentos(GfdMDocumentosGetAllInputDto inputDto, Fornecedor fornecedor) {
        var gfdDocumentoGetAllInputDto = modelMapper.map(inputDto, GfdDocumentoGetAllInputDto.class);
        gfdDocumentoGetAllInputDto.setCtforCodigo(fornecedor.getCodigo());

        var pageGfdDocumentoService = getAllGfdDocumentosUseCase.execute(gfdDocumentoGetAllInputDto);

        var gfdDocumentoDto = pageGfdDocumentoService.getContent().stream().map(gfdDocumento -> {
            gfdDocumento.setNomeArquivo(CleanFileName.clear(gfdDocumento.getNomeArquivo()));
            return modelMapper.map(gfdDocumento, GfdMDocumentosGetAllOutputDto.GfdDocumentoDto.class);
        }).toList();

        return new PageBaseImpl<>(gfdDocumentoDto, pageGfdDocumentoService.getPage(), pageGfdDocumentoService.getSize(), pageGfdDocumentoService.getTotalElements()) {
        };
    }

    private GfdTipoDocumento recuperarTipoDocumento(GfdMDocumentosGetAllInputDto inputDto) {
        return gfdTipoDocumentoRepository.findById(inputDto.getTipoDocumentoId()).orElseThrow(() ->
                new NotFoundException(
                        GFD_TIPO_DOCUMENTO_NAO_ENCONTRADO.getMensagem(),
                        GFD_TIPO_DOCUMENTO_NAO_ENCONTRADO.getCode()
                )
        );
    }

    private GfdMDocumentosGetAllOutputDto.GfdTipoDocumentoDto criarDtoTipoDocumentoParaResposta
            (GfdTipoDocumento tipoDocumento) {
        var gfdTipoDocumentoDto = new GfdMDocumentosGetAllOutputDto.GfdTipoDocumentoDto();

        gfdTipoDocumentoDto.setId(tipoDocumento.getId());
        gfdTipoDocumentoDto.setNome(tipoDocumento.getNome());
        gfdTipoDocumentoDto.setDiasValidade(tipoDocumento.getDiasValidade());
        gfdTipoDocumentoDto.setClassificacao(tipoDocumento.getClassificacao());

        return gfdTipoDocumentoDto;
    }

    List<GfdTipoDocumentoGetAllOutputDto> getFornecedorTipoDocumentos(Integer funcionarioId, Fornecedor fornecedor) {
        var tipoDocumentoInputDto = new GfdTipoDocumentoGetAllInputDto();
        var gfdCategoriaDocumentoDto = new GfdTipoDocumentoGetAllInputDto.GfdCategoriaDocumentoDto();
        gfdCategoriaDocumentoDto.setId(fornecedor.getTipoFornecedor().getCategoriaDocumento().getId());

        tipoDocumentoInputDto.setCategoriaDocumento(gfdCategoriaDocumentoDto);

        if (funcionarioId != null) {
            // pega as informacoes de funcionario
            var funcionario = getGfdFuncionarioGetByIdOutputDto(funcionarioId);

            var idCategoriaDocumento = funcionario.getTipoContratacao().getCategoriaDocumento().getId();
            gfdCategoriaDocumentoDto.setId(idCategoriaDocumento);

            tipoDocumentoInputDto.setCategoriaDocumento(gfdCategoriaDocumentoDto);
        }

        return getAllTipoDocumentoUseCase.execute(tipoDocumentoInputDto);
    }

    private GfdFuncionario getGfdFuncionarioGetByIdOutputDto(Integer inputDto) {
        return funcionarioRepository.findById(inputDto).orElseThrow(
                () -> new NotFoundException(GFD_FUNCIONARIO_NAO_ENCONTRADO.getMensagem(), GFD_FUNCIONARIO_NAO_ENCONTRADO.getCode()
                ));
    }
}
