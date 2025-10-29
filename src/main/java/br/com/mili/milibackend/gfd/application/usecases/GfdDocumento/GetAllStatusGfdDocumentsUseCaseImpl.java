package br.com.mili.milibackend.gfd.application.usecases.GfdDocumento;

import br.com.mili.milibackend.fornecedor.domain.entity.Fornecedor;
import br.com.mili.milibackend.fornecedor.domain.usecases.GetFornecedorByCodOrIdUseCase;
import br.com.mili.milibackend.gfd.application.dto.manager.documentos.GfdMVerificarDocumentosInputDto;
import br.com.mili.milibackend.gfd.application.dto.manager.documentos.GfdMVerificarDocumentosOutputDto;
import br.com.mili.milibackend.gfd.domain.entity.GfdDocumento;
import br.com.mili.milibackend.gfd.application.dto.gfdTipoDocumento.GfdTipoDocumentoGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdTipoDocumento.GfdTipoDocumentoGetAllOutputDto;
import br.com.mili.milibackend.gfd.domain.entity.GfdFuncionario;
import br.com.mili.milibackend.gfd.domain.entity.GfdTipoDocumentoTipoClassificacaoEnum;
import br.com.mili.milibackend.gfd.domain.usecases.gfdDocumento.GetAllStatusGfdDocumentsUseCase;
import br.com.mili.milibackend.gfd.domain.usecases.gfdDocumento.GetAllTipoDocumentoUseCase;
import br.com.mili.milibackend.gfd.infra.repository.gfdDocumento.GfdDocumentoRepository;
import br.com.mili.milibackend.gfd.infra.repository.gfdFuncionario.GfdFuncionarioRepository;
import br.com.mili.milibackend.shared.exception.types.ConflictException;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static br.com.mili.milibackend.gfd.adapter.exception.GfdFuncionarioCodeException.GFD_FUNCIONARIO_NAO_ENCONTRADO;
import static br.com.mili.milibackend.gfd.adapter.exception.GfdMCodeException.GFD_LEI_LGPD_NAO_ACEITA;

@RequiredArgsConstructor
@Service
public class GetAllStatusGfdDocumentsUseCaseImpl implements GetAllStatusGfdDocumentsUseCase {
    private final GetFornecedorByCodOrIdUseCase getFornecedorByCodOrIdUseCase;
    private final GfdFuncionarioRepository gfdFuncionarioRepository;
    private final GetAllTipoDocumentoUseCase getAllTipoDocumentoUseCase;
    private final GfdDocumentoRepository gfdDocumentoRepository;

    @Override
    public GfdMVerificarDocumentosOutputDto execute(@Valid GfdMVerificarDocumentosInputDto inputDto) {
        var output = new GfdMVerificarDocumentosOutputDto();
        var documentos = new LinkedHashSet<GfdMVerificarDocumentosOutputDto.DocumentoDto>();

        var fornecedor = getFornecedorByCodOrIdUseCase.execute(inputDto.getCodUsuario(), inputDto.getId(), inputDto.isAnalista());

       if(fornecedor.getAceiteLgpd() == null || fornecedor.getAceiteLgpd() == 0){
            throw new ConflictException(GFD_LEI_LGPD_NAO_ACEITA.getMensagem(), GFD_LEI_LGPD_NAO_ACEITA.getCode());
        }

        // adiciona no title o nome do fornecedor
        output.setTitle(fornecedor.getRazaoSocial());

        /* Aqui pegamos todos os documentos recentes do fornecedor ou funcionario,
        de cada tipo, para podermos exibir os status na tela*/
        var latestDocuments = gfdDocumentoRepository.findLatestDocumentsByPeriodoAndFornecedorOrFuncionario(
                fornecedor.getCodigo(),
                inputDto.getIdFuncionario(),
                inputDto.getPeriodo(),
                inputDto.getSetor()
        );

        /* busca o tipo do documento enviado */
        var tipoDocumentoInputDto = new GfdTipoDocumentoGetAllInputDto();
        var gfdCategoriaDocumentoDto = new GfdTipoDocumentoGetAllInputDto.GfdCategoriaDocumentoDto();
        gfdCategoriaDocumentoDto.setId(fornecedor.getTipoFornecedor().getCategoriaDocumento().getId());

        tipoDocumentoInputDto.setCategoriaDocumento(gfdCategoriaDocumentoDto);
        /**/

        adicionarFiltroFuncionario(inputDto, output, fornecedor, gfdCategoriaDocumentoDto, tipoDocumentoInputDto);
        tipoDocumentoInputDto.setSetor( inputDto.getSetor());

        var tipoDocumentos = getAllTipoDocumentoUseCase.execute(tipoDocumentoInputDto);


        addNonMandatoryDocuments(documentos, latestDocuments, tipoDocumentos);
        addMandatoryDocuments(documentos, latestDocuments, tipoDocumentos);

        output.setDocumentos(documentos.stream().toList());

        return output;
    }

    private void adicionarFiltroFuncionario(
            GfdMVerificarDocumentosInputDto inputDto,
            GfdMVerificarDocumentosOutputDto output,
            Fornecedor fornecedor,
            GfdTipoDocumentoGetAllInputDto.GfdCategoriaDocumentoDto gfdCategoriaDocumentoDto,
            GfdTipoDocumentoGetAllInputDto tipoDocumentoInputDto
    ) {
        if (inputDto.getIdFuncionario() != null) {

            // pega as informacoes de funcionario
            var funcionario = getGfdFuncionarioGetByIdOutputDto(inputDto.getIdFuncionario());
            output.setTitle(fornecedor.getRazaoSocial() + " - " + funcionario.getNome());

            var idCategoriaDocumento = funcionario.getTipoContratacao().getCategoriaDocumento().getId();
            gfdCategoriaDocumentoDto.setId(idCategoriaDocumento);

            tipoDocumentoInputDto.setCategoriaDocumento(gfdCategoriaDocumentoDto);

            // verifica se o funcionario foi desligado
            if(funcionario.getDesligado() == null || funcionario.getDesligado() == 0)
            {
                tipoDocumentoInputDto.setClassificacao("!" + GfdTipoDocumentoTipoClassificacaoEnum.RESCISAO.name());
            }

        }
    }

    private GfdFuncionario getGfdFuncionarioGetByIdOutputDto(Integer inputDto) {
        return gfdFuncionarioRepository.findById(inputDto).orElseThrow(
                () -> new NotFoundException(GFD_FUNCIONARIO_NAO_ENCONTRADO.getMensagem(), GFD_FUNCIONARIO_NAO_ENCONTRADO.getCode()
                ));
    }

    private void addNonMandatoryDocuments(
            Set<GfdMVerificarDocumentosOutputDto.DocumentoDto> outputDtoList,
            List<GfdDocumento> latestDocuments,
            List<GfdTipoDocumentoGetAllOutputDto> tipoDocumentos
    ) {
        tipoDocumentos.stream().filter(tipoDoc -> !tipoDoc.getObrigatoriedade()).forEach(tipoDoc -> {
            var outputDto = buildGfdMVerificarDocumentoOutpDto("OPCIONAL", tipoDoc.getNome(), tipoDoc.getId());
            outputDtoList.add(outputDto);
        });
    }

    private void addMandatoryDocuments(
            Set<GfdMVerificarDocumentosOutputDto.DocumentoDto> outputDtoList,
            List<GfdDocumento> latestDocuments,
            List<GfdTipoDocumentoGetAllOutputDto> tipoDocumentos
    ) {
        tipoDocumentos.stream()
                .filter(GfdTipoDocumentoGetAllOutputDto::getObrigatoriedade)
                .forEach(tipoDoc -> {
            if (fornecedorHasDocument(latestDocuments, tipoDoc)) {
                addExistingDocument(outputDtoList, latestDocuments, tipoDoc);
            } else {
                var outputDto = buildGfdMVerificarDocumentoOutpDto("NÃO ENVIADO", tipoDoc.getNome(), tipoDoc.getId());
                outputDtoList.add(outputDto);
            }
        });
    }

    private void addExistingDocument(
            Set<GfdMVerificarDocumentosOutputDto.DocumentoDto> outputDtoList,
            List<GfdDocumento> latestDocuments,
            GfdTipoDocumentoGetAllOutputDto tipoDoc
    ) {
        latestDocuments.stream().filter(doc -> doc.getGfdTipoDocumento().getId().equals(tipoDoc.getId())).forEach(doc -> {
            var outputDto = buildGfdMVerificarDocumentoOutpDto(doc.getStatus().getDescricao(), tipoDoc.getNome(), tipoDoc.getId());
            outputDtoList.add(outputDto);
        });
    }

    private boolean fornecedorHasDocument(
            List<GfdDocumento> documentos,
            GfdTipoDocumentoGetAllOutputDto tipoDoc
    ) {
        return documentos.stream().anyMatch(doc -> doc.getGfdTipoDocumento().getId().equals(tipoDoc.getId()));
    }

    private GfdMVerificarDocumentosOutputDto.DocumentoDto buildGfdMVerificarDocumentoOutpDto(
            String status,
            String nome,
            Integer idTipoDocumento
    ) {
        var outputDto = new GfdMVerificarDocumentosOutputDto.DocumentoDto();
        outputDto.setStatus(status);
        outputDto.setNome(nome);
        outputDto.setIdTipoDocumento(idTipoDocumento);
        return outputDto;
    }
}
