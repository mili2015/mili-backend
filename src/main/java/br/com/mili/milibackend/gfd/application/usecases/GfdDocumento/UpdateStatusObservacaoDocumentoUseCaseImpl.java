package br.com.mili.milibackend.gfd.application.usecases.GfdDocumento;

import br.com.mili.milibackend.gfd.adapter.exception.GfdDocumentoCodeException;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoUpdateStatusObservacaoInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoUpdateStatusObservacaoOutputDto;
import br.com.mili.milibackend.gfd.domain.entity.GfdDocumentoStatusEnum;
import br.com.mili.milibackend.gfd.domain.usecases.UpdateStatusObservacaoDocumentoUseCase;
import br.com.mili.milibackend.gfd.infra.repository.gfdDocumento.GfdDocumentoRepository;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateStatusObservacaoDocumentoUseCaseImpl implements UpdateStatusObservacaoDocumentoUseCase {
    private final GfdDocumentoRepository gfdDocumentoRepository;
    private final ModelMapper modelMapper;

    public GfdDocumentoUpdateStatusObservacaoOutputDto execute(GfdDocumentoUpdateStatusObservacaoInputDto inputDto) {
        var gfdDocumentoFound = gfdDocumentoRepository.findById(inputDto.getId()).orElseThrow(() -> new NotFoundException(GfdDocumentoCodeException.GFD_DOCUMENTO_NAO_ENCONTRADO.getMensagem(), GfdDocumentoCodeException.GFD_DOCUMENTO_NAO_ENCONTRADO.getCode()));

        gfdDocumentoFound.setStatus(GfdDocumentoStatusEnum.valueOf(inputDto.getStatus()));
        gfdDocumentoFound.setObservacao(inputDto.getObservacao());

        return modelMapper.map(gfdDocumentoRepository.save(gfdDocumentoFound), GfdDocumentoUpdateStatusObservacaoOutputDto.class);
    }
}
