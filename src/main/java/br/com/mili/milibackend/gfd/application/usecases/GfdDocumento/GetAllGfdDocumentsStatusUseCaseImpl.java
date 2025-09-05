package br.com.mili.milibackend.gfd.application.usecases.GfdDocumento;

import br.com.mili.milibackend.fornecedor.domain.usecases.GetFornecedorByCodOrIdUseCase;
import br.com.mili.milibackend.gfd.application.dto.manager.documentos.GfdMVerificarDocumentosInputDto;
import br.com.mili.milibackend.gfd.application.dto.manager.documentos.GfdMVerificarDocumentosOutputDto;
import br.com.mili.milibackend.gfd.domain.entity.GfdDocumento;
import br.com.mili.milibackend.gfd.application.dto.gfdTipoDocumento.GfdTipoDocumentoGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdTipoDocumento.GfdTipoDocumentoGetAllOutputDto;
import br.com.mili.milibackend.gfd.domain.entity.GfdFuncionario;
import br.com.mili.milibackend.gfd.domain.entity.GfdFuncionarioTipoContratacaoEnum;
import br.com.mili.milibackend.gfd.domain.entity.GfdTipoDocumentoTipoEnum;
import br.com.mili.milibackend.gfd.domain.usecases.GetAllGfdDocumentsStatusUseCase;
import br.com.mili.milibackend.gfd.domain.usecases.GetAllTipoDocumentoUseCase;
import br.com.mili.milibackend.gfd.infra.repository.gfdDocumento.GfdDocumentoRepository;
import br.com.mili.milibackend.gfd.infra.repository.gfdFuncionario.GfdFuncionarioRepository;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static br.com.mili.milibackend.gfd.adapter.exception.GfdFuncionarioCodeException.GFD_FUNCIONARIO_NAO_ENCONTRADO;

@RequiredArgsConstructor
@Service
public class GetAllGfdDocumentsStatusUseCaseImpl implements GetAllGfdDocumentsStatusUseCase {
    private final GetFornecedorByCodOrIdUseCase getFornecedorByCodOrIdUseCase;
    private final GfdFuncionarioRepository gfdFuncionarioRepository;
    private final GetAllTipoDocumentoUseCase getAllTipoDocumentoUseCase;
    private final GfdDocumentoRepository gfdDocumentoRepository;

    @Override
    public GfdMVerificarDocumentosOutputDto execute(@Valid GfdMVerificarDocumentosInputDto inputDto) {
        var output = new GfdMVerificarDocumentosOutputDto();
        var documentos = new LinkedHashSet<GfdMVerificarDocumentosOutputDto.DocumentoDto>();

        var fornecedor = getFornecedorByCodOrIdUseCase.execute(inputDto.getCodUsuario(), inputDto.getId());

        // adiciona no title o nome do fornecedor
        output.setTitle(fornecedor.getRazaoSocial());

        /* Aqui pegamos todos os documentos recentes do fornecedor ou funcionario,
        de cada tipo, para podermos exibir os status na tela*/
        var latestDocuments = gfdDocumentoRepository.findLatestDocumentsByPeriodoAndFornecedorOrFuncionario(fornecedor.getCodigo(), inputDto.getIdFuncionario(), inputDto.getPeriodo());

        /* extrair */
        var tipoDocumentoInputDto = new GfdTipoDocumentoGetAllInputDto();
        tipoDocumentoInputDto.setTipo(GfdTipoDocumentoTipoEnum.FORNECEDOR);

        if (inputDto.getIdFuncionario() != null) {
            // pega as informacoes de funcionario
            var funcionario = getGfdFuncionarioGetByIdOutputDto(inputDto.getIdFuncionario());

            // adiciona no title o nome do funcionario
            output.setTitle(fornecedor.getRazaoSocial() + " - " + funcionario.getNome());

            if (funcionario.getTipoContratacao().equals(GfdFuncionarioTipoContratacaoEnum.CLT.getDescricao())) {
                tipoDocumentoInputDto.setTipo(GfdTipoDocumentoTipoEnum.FUNCIONARIO_CLT);
            } else if (funcionario.getTipoContratacao().equals(GfdFuncionarioTipoContratacaoEnum.CLT_SEGURANCA.getDescricao())) {
                tipoDocumentoInputDto.setTipo(GfdTipoDocumentoTipoEnum.FUNCIONARIO_CLT_SEGURANCA);
            } else {
                tipoDocumentoInputDto.setTipo(GfdTipoDocumentoTipoEnum.FUNCIONARIO_MEI);
            }
        }
        /**/

        var tipoDocumentos = getAllTipoDocumentoUseCase.execute(tipoDocumentoInputDto);

        addNonMandatoryDocuments(documentos, latestDocuments, tipoDocumentos);
        addMandatoryDocuments(documentos, latestDocuments, tipoDocumentos);

        output.setDocumentos(documentos.stream().toList());

        return output;
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
        tipoDocumentos.stream().filter(GfdTipoDocumentoGetAllOutputDto::getObrigatoriedade).forEach(tipoDoc -> {
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
