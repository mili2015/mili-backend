package br.com.mili.milibackend.gfd.application.usecases.DocumentoPeriodo;

import br.com.mili.milibackend.gfd.application.dto.gfdDocumentoPeriodo.GfdDocumentoPeriodoCreateInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumentoPeriodo.GfdDocumentoPeriodoCreateOutputDto;
import br.com.mili.milibackend.gfd.domain.entity.GfdDocumento;
import br.com.mili.milibackend.gfd.domain.entity.GfdDocumentoPeriodo;
import br.com.mili.milibackend.gfd.domain.usecases.gfdDocumento.CreateDocumentoPeriodoUseCase;
import br.com.mili.milibackend.gfd.infra.repository.GfdDocumentoPeriodoRepository;
import br.com.mili.milibackend.shared.exception.types.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import static br.com.mili.milibackend.gfd.adapter.exception.GfdMCodeException.GFD_PERIODO_VAZIO;
import static br.com.mili.milibackend.gfd.domain.entity.GfdTipoDocumentoTipoClassificacaoEnum.COMPETENCIA;

@Service
@RequiredArgsConstructor
public class CreateDocumentoPeriodoUseCaseImpl implements CreateDocumentoPeriodoUseCase {
    private final GfdDocumentoPeriodoRepository gfdDocumentoPeriodoRepository;
    private final ModelMapper modelMapper;

    @Override
    public GfdDocumentoPeriodoCreateOutputDto execute(GfdDocumentoPeriodoCreateInputDto inputDto) {
        var tipoDocumentoDto = inputDto.getTipoDocumento();
        var gfdDocumentoPeriodoDto = inputDto.getGfdDocumentoPeriodo();
        var gfdDocumentoDto = inputDto.getGfdDocumentoDto();

        GfdDocumentoPeriodoCreateOutputDto gfdDocumentoPeriodosCreatedDto = null;

        //geral
        // cria um periodo de documento
        // - pega o valor por tipo validade e cria os registros
        var gfdDocumento = new GfdDocumento();
        gfdDocumento.setId(gfdDocumentoDto.getId());

        boolean isGeral = tipoDocumentoDto.getClassificacao() == null || !tipoDocumentoDto.getClassificacao().equals(COMPETENCIA.name());

        if (isGeral) {
            gfdDocumentoPeriodosCreatedDto = criarDocumentoPeriodoGeral(inputDto, tipoDocumentoDto, gfdDocumentoDto, gfdDocumento);

        } else {
            gfdDocumentoPeriodosCreatedDto = criarDocumentoPeriodoCompetencia(inputDto, gfdDocumentoPeriodoDto, gfdDocumento, gfdDocumentoDto);
        }

        return gfdDocumentoPeriodosCreatedDto;
    }

    private GfdDocumentoPeriodoCreateOutputDto criarDocumentoPeriodoCompetencia(GfdDocumentoPeriodoCreateInputDto inputDto, GfdDocumentoPeriodoCreateInputDto.GfdDocumentoPeriodoDto gfdDocumentoPeriodoDto, GfdDocumento gfdDocumento, GfdDocumentoPeriodoCreateInputDto.GfdDocumentoDto gfdDocumentoDto) {
        GfdDocumentoPeriodoCreateOutputDto gfdDocumentoPeriodosCreatedDto;
        if (gfdDocumentoPeriodoDto == null || gfdDocumentoPeriodoDto.getPeriodo() == null) {
            throw new BadRequestException(GFD_PERIODO_VAZIO.getMensagem(), GFD_PERIODO_VAZIO.getCode());
        }
        var primeiroPeriodo = gfdDocumentoPeriodoDto.getPeriodo().withDayOfMonth(1);
        var ultimoPeriodo = gfdDocumentoPeriodoDto.getPeriodo().withDayOfMonth(gfdDocumentoPeriodoDto.getPeriodo().lengthOfMonth());

        var gfdDocumentoPeriodo = GfdDocumentoPeriodo.builder()
                .periodoInicial(primeiroPeriodo)
                .periodoFinal(ultimoPeriodo)
                .gfdDocumento(gfdDocumento)
                .build();

        // apaga o periodo quando for atualização
        if (inputDto.isUpdate()) {
            gfdDocumentoPeriodoRepository.deleteByGfdDocumento_Id(gfdDocumentoDto.getId());
        }

        var gfdDocumentoPeriodoCreated = gfdDocumentoPeriodoRepository.save(gfdDocumentoPeriodo);

        gfdDocumentoPeriodosCreatedDto = (modelMapper.map(gfdDocumentoPeriodoCreated, GfdDocumentoPeriodoCreateOutputDto.class));
        return gfdDocumentoPeriodosCreatedDto;
    }

    private GfdDocumentoPeriodoCreateOutputDto criarDocumentoPeriodoGeral(GfdDocumentoPeriodoCreateInputDto inputDto, GfdDocumentoPeriodoCreateInputDto.TipoDocumentoDto tipoDocumentoDto, GfdDocumentoPeriodoCreateInputDto.GfdDocumentoDto gfdDocumentoDto, GfdDocumento gfdDocumento) {
        GfdDocumentoPeriodoCreateOutputDto gfdDocumentoPeriodosCreatedDto;
        final int UM_MES_EN_DIAS = 30;
        final int UM_ANO = 30 * 12;
        final int DEZ_ANOS = 10 * UM_ANO;

        int qtdMeses = tipoDocumentoDto.getDiasValidade() != null
                ? tipoDocumentoDto.getDiasValidade() / UM_MES_EN_DIAS
                : DEZ_ANOS / UM_MES_EN_DIAS;

        int qtdPeriodos = (int) Math.ceil(qtdMeses <= 0 ? (double) DEZ_ANOS / UM_MES_EN_DIAS : qtdMeses);

        var primeiroPeriodo = gfdDocumentoDto.getDataEmissao().withDayOfMonth(1);
        var ultimoPeriodo = ultimoPeriodo(inputDto, gfdDocumentoDto, qtdPeriodos);

        // apaga os periodos iguais para o mesmo tipo de documento
        // quando for update
        if (inputDto.isUpdate()) {
            gfdDocumentoPeriodoRepository.deleteByGfdDocumento_Id(gfdDocumentoDto.getId());
        }

        var gfdDocumentoPeriodo = GfdDocumentoPeriodo.builder()
                .periodoInicial(primeiroPeriodo)
                .periodoFinal(ultimoPeriodo)
                .gfdDocumento(gfdDocumento)
                .build();

        gfdDocumentoPeriodosCreatedDto = modelMapper.map(gfdDocumentoPeriodoRepository.save(gfdDocumentoPeriodo), GfdDocumentoPeriodoCreateOutputDto.class);
        return gfdDocumentoPeriodosCreatedDto;
    }

    private LocalDate ultimoPeriodo(GfdDocumentoPeriodoCreateInputDto inputDto, GfdDocumentoPeriodoCreateInputDto.GfdDocumentoDto gfdDocumentoDto, int qtdPeriodos) {
        final int PROXIMO_MES = 1;

        var ultimoPeriodo = gfdDocumentoDto.getDataEmissao()
                .plusMonths(qtdPeriodos == 1 ? qtdPeriodos : qtdPeriodos + PROXIMO_MES)
                .with(TemporalAdjusters.lastDayOfMonth());

        if (inputDto.getGfdDocumentoDto().getDataValidade() != null) {
            ultimoPeriodo = inputDto.getGfdDocumentoDto().getDataValidade().withDayOfMonth(inputDto.getGfdDocumentoDto().getDataValidade().lengthOfMonth());
        }

        return ultimoPeriodo;
    }
}
