package br.com.mili.milibackend.gfd.application.usecases.GfdDocumento;

import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoUpdateInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoUpdateOutputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumentoPeriodo.GfdDocumentoPeriodoCreateInputDto;
import br.com.mili.milibackend.gfd.domain.entity.GfdDocumento;
import br.com.mili.milibackend.gfd.domain.entity.GfdDocumentoStatusEnum;
import br.com.mili.milibackend.gfd.domain.usecases.CreateDocumentoPeriodoUseCase;
import br.com.mili.milibackend.gfd.domain.usecases.UpdateGfdDocumentoUseCase;
import br.com.mili.milibackend.gfd.infra.repository.gfdDocumento.GfdDocumentoRepository;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import static br.com.mili.milibackend.gfd.adapter.exception.GfdDocumentoCodeException.GFD_DOCUMENTO_NAO_ENCONTRADO;

@Service
@RequiredArgsConstructor
public class UpdateGfdDocumentoUseCaseImpl implements UpdateGfdDocumentoUseCase {
    private final GfdDocumentoRepository gfdDocumentoRepository;
    private final CreateDocumentoPeriodoUseCase createDocumentoPeriodoUseCase;
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

        gfdDocumentoRepository.save(gfdDocumento);

        return modelMapper.map(gfdDocumento, GfdDocumentoUpdateOutputDto.class);
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
