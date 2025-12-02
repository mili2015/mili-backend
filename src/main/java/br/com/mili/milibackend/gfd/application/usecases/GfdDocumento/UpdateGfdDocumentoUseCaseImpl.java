package br.com.mili.milibackend.gfd.application.usecases.GfdDocumento;

import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoUpdateInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoUpdateOutputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumentoPeriodo.GfdDocumentoPeriodoCreateInputDto;
import br.com.mili.milibackend.gfd.domain.entity.GfdDocumento;
import br.com.mili.milibackend.gfd.domain.entity.GfdDocumentoHistorico;
import br.com.mili.milibackend.gfd.domain.entity.GfdDocumentoStatusEnum;
import br.com.mili.milibackend.gfd.domain.usecases.gfdDocumento.CreateDocumentoPeriodoUseCase;
import br.com.mili.milibackend.gfd.domain.usecases.gfdDocumento.UpdateGfdDocumentoUseCase;
import br.com.mili.milibackend.gfd.infra.projections.GfdDocumentCountProjection;
import br.com.mili.milibackend.gfd.infra.repository.gfdDocumento.GfdDocumentoHistoricoRepository;
import br.com.mili.milibackend.gfd.infra.repository.gfdDocumento.GfdDocumentoRepository;
import br.com.mili.milibackend.gfd.infra.repository.gfdFuncionario.GfdFuncionarioRepository;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static br.com.mili.milibackend.gfd.adapter.exception.GfdDocumentoCodeException.GFD_DOCUMENTO_NAO_ENCONTRADO;

@Service
@RequiredArgsConstructor
public class UpdateGfdDocumentoUseCaseImpl implements UpdateGfdDocumentoUseCase {
    private final GfdDocumentoRepository gfdDocumentoRepository;
    private final CreateDocumentoPeriodoUseCase createDocumentoPeriodoUseCase;
    private final GfdDocumentoHistoricoRepository gfdDocumentoHistoricoRepository;
    private final GfdFuncionarioRepository gfdFuncionarioRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public GfdDocumentoUpdateOutputDto execute(GfdDocumentoUpdateInputDto inputDto) {
        var gfdDocumentoDto = inputDto.getGfdDocumentoDto();

        GfdDocumento gfdDocumento = gfdDocumentoRepository.findById(gfdDocumentoDto.getId())
                .orElseThrow(() ->
                        new NotFoundException(GFD_DOCUMENTO_NAO_ENCONTRADO.getMensagem(), GFD_DOCUMENTO_NAO_ENCONTRADO.getCode())
                );

        modelMapper.map(inputDto.getGfdDocumentoDto(), gfdDocumento);

        createPeriodo(gfdDocumentoDto, gfdDocumento, inputDto.getGfdDocumentoPeriodo());

        gfdDocumento.setStatus(GfdDocumentoStatusEnum.valueOf(gfdDocumentoDto.getStatus()));

        var gfdDocumentoSaved = gfdDocumentoRepository.save(gfdDocumento);

        //criar o historico
        var funcionarioId = gfdDocumentoSaved.getGfdFuncionario() != null ? gfdDocumentoSaved.getGfdFuncionario().getId() : null;
        var gfdDocumentoHistorico = GfdDocumentoHistorico.builder()
                .documentoId(gfdDocumentoSaved.getId())
                .ctusuCodigo(inputDto.getCodUsuario())
                .ctforCodigo(gfdDocumentoSaved.getCtforCodigo())
                .data(LocalDateTime.now())
                .status(gfdDocumento.getStatus().getDescricao())
                .funcionarioId(funcionarioId)
                .build();

        gfdDocumentoHistoricoRepository.save(gfdDocumentoHistorico);

        liberarFuncionario(funcionarioId, gfdDocumentoSaved.getCtforCodigo());

        return modelMapper.map(gfdDocumento, GfdDocumentoUpdateOutputDto.class);
    }

    private void liberarFuncionario(Integer funcionarioId, Integer ctforCodigo) {
        LocalDate inicioMesAnterior = LocalDate.now()
                .minusMonths(1)
                .withDayOfMonth(1);

        var documentosStatusFornecedor = gfdDocumentoRepository.getAllCount(ctforCodigo, inicioMesAnterior);

        // se o documento for de um funcionario
        // pesquisa todos os documentos do funcionario do mes anterior exemplo: estamos no mes 11 -> procurar mes 10
        // os documentos da empresa também precisam estar em conforme
        // se tudo tiver conforme automaticamente atualiza o liberado do funcionario
        if (funcionarioId != null) {
            updateStatusLiberadoFuncionario(funcionarioId, inicioMesAnterior, documentosStatusFornecedor);

            return;
        }

/*        // caso o documento for da empresa
        // procura a empresa do documento e verifica se todos os documentos da empresa estão em conforme
        // caso contrario todos os funcionários vão automaticamente estar com status não liberado
        updateStatusLiberadoPorFornecedor(ctforCodigo, documentosStatusFornecedor);*/
    }

    private void updateStatusLiberadoPorFornecedor(Integer ctforCodigo, GfdDocumentCountProjection documentosStatusFornecedor) {
        var naoEnviado = documentosStatusFornecedor.getNaoEnviado();
        var totalEnviado = documentosStatusFornecedor.getTotalEnviado();
        var totalEmAnalise = documentosStatusFornecedor.getTotalEmAnalise();
        var totalNaoConforme = documentosStatusFornecedor.getTotalNaoConforme();

        boolean documentosPendentes = naoEnviado > 0
                || totalEnviado > 0
                || totalEmAnalise > 0
                || totalNaoConforme > 0;

        if (documentosPendentes) {
            gfdFuncionarioRepository.updateLiberadoFornecedor(ctforCodigo, 0);
        }
    }

    private void updateStatusLiberadoFuncionario(Integer funcionarioId, LocalDate inicioMesAnterior, GfdDocumentCountProjection documentosStatusFornecedor) {
        var documentosStatus = gfdFuncionarioRepository.getAllDocuments(funcionarioId, inicioMesAnterior);

        var naoEnviado = documentosStatusFornecedor.getNaoEnviado() + documentosStatus.getNaoEnviado();
        var totalEnviado = documentosStatusFornecedor.getTotalEnviado() + documentosStatus.getTotalEnviado();
        var totalEmAnalise = documentosStatusFornecedor.getTotalEmAnalise() + documentosStatus.getTotalEmAnalise();
        var totalNaoConforme = documentosStatusFornecedor.getTotalNaoConforme() + documentosStatus.getTotalNaoConforme();

        if (naoEnviado == 0
                && totalEnviado == 0
                && totalEmAnalise == 0
                && totalNaoConforme == 0
        ) {
            gfdFuncionarioRepository.updateLiberado(funcionarioId, 1);
        } else {
            gfdFuncionarioRepository.updateLiberado(funcionarioId, 0);
        }
    }

    private void createPeriodo(
            GfdDocumentoUpdateInputDto.GfdDocumentoDto gfdDocumentoDto,
            GfdDocumento gfdDocumento,
            GfdDocumentoUpdateInputDto.GfdDocumentoPeriodoDto gfdDocumentoPeriodoDto
    ) {
        var periodoCreateInputDto = GfdDocumentoPeriodoCreateInputDto.builder()
                .periodo(gfdDocumentoPeriodoDto != null ? gfdDocumentoPeriodoDto.getPeriodo() : null)
                .documento(gfdDocumento.getId(), gfdDocumentoDto.getDataEmissao(), gfdDocumento.getDataValidade())
                .tipoDocumento(gfdDocumento.getId(), gfdDocumento.getGfdTipoDocumento().getClassificacao(), gfdDocumento.getGfdTipoDocumento().getDiasValidade())
                .update()
                .build();

        createDocumentoPeriodoUseCase.execute(periodoCreateInputDto);
    }
}
