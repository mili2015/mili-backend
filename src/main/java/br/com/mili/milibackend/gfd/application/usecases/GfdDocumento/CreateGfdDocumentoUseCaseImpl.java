package br.com.mili.milibackend.gfd.application.usecases.GfdDocumento;

import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoCreateInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoCreateOutputDto;
import br.com.mili.milibackend.gfd.domain.entity.GfdDocumento;
import br.com.mili.milibackend.gfd.domain.usecases.CreateDocumentoUseCase;
import br.com.mili.milibackend.gfd.infra.repository.gfdDocumento.GfdDocumentoRepository;
import br.com.mili.milibackend.shared.infra.aws.IS3Service;
import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateGfdDocumentoUseCaseImpl implements CreateDocumentoUseCase {
    private final GfdDocumentoRepository gfdDocumentoRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public GfdDocumentoCreateOutputDto execute(GfdDocumentoCreateInputDto inputDto) {

        var gfdDocumento = modelMapper.map(inputDto.getGfdDocumentoDto(), GfdDocumento.class);

        var gfdDocumentoCreated = gfdDocumentoRepository.save(gfdDocumento);

        return modelMapper.map(gfdDocumentoCreated, GfdDocumentoCreateOutputDto.class);
    }
}
