package br.com.mili.milibackend.gfd.application.usecases.GfdDocumento;

import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoCreateInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoCreateOutputDto;
import br.com.mili.milibackend.gfd.domain.entity.GfdDocumento;
import br.com.mili.milibackend.gfd.domain.entity.GfdDocumentoHistorico;
import br.com.mili.milibackend.gfd.domain.usecases.gfdDocumento.CreateDocumentoUseCase;
import br.com.mili.milibackend.gfd.infra.repository.gfdDocumento.GfdDocumentoHistoricoRepository;
import br.com.mili.milibackend.gfd.infra.repository.gfdDocumento.GfdDocumentoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateGfdDocumentoUseCaseImpl implements CreateDocumentoUseCase {
    private final GfdDocumentoRepository gfdDocumentoRepository;
    private final GfdDocumentoHistoricoRepository gfdDocumentoHistoricoRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public GfdDocumentoCreateOutputDto execute(GfdDocumentoCreateInputDto inputDto) {

        var gfdDocumento = modelMapper.map(inputDto.getGfdDocumentoDto(), GfdDocumento.class);

        var gfdDocumentoCreated = gfdDocumentoRepository.save(gfdDocumento);


        //criar o historico
        var funcionarioId = inputDto.getGfdDocumentoDto().getGfdFuncionario() != null ? inputDto.getGfdDocumentoDto().getGfdFuncionario().getId() : null;
        var gfdDocumentoHistorico = GfdDocumentoHistorico.builder()
                .documentoId(gfdDocumentoCreated.getId())
                .ctusuCodigo(inputDto.getCodUsuario())
                .ctforCodigo(gfdDocumentoCreated.getCtforCodigo())
                .data(LocalDateTime.now())
                .status(gfdDocumento.getStatus().getDescricao())
                .funcionarioId(funcionarioId)
                .build();

        gfdDocumentoHistoricoRepository.save(gfdDocumentoHistorico);

        return modelMapper.map(gfdDocumentoCreated, GfdDocumentoCreateOutputDto.class);
    }


}
