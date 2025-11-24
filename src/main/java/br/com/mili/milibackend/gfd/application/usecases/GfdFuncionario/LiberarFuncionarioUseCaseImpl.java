package br.com.mili.milibackend.gfd.application.usecases.GfdFuncionario;

import br.com.mili.milibackend.gfd.adapter.exception.GfdFuncionarioCodeException;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.gfdFuncionarioLiberacao.GfdFuncionarioLiberarInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.gfdFuncionarioLiberacao.GfdFuncionarioLiberarOutputDto;
import br.com.mili.milibackend.gfd.domain.entity.GfdFuncionarioLiberacao;
import br.com.mili.milibackend.gfd.domain.usecases.gfdFuncionario.LiberarFuncionarioUseCase;
import br.com.mili.milibackend.gfd.infra.repository.gfdFuncionario.GfdFuncionarioLiberacaoRepository;
import br.com.mili.milibackend.gfd.infra.repository.gfdFuncionario.GfdFuncionarioRepository;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LiberarFuncionarioUseCaseImpl implements LiberarFuncionarioUseCase {
    private final GfdFuncionarioRepository gfdFuncionarioRepository;
    private final GfdFuncionarioLiberacaoRepository gfdFuncionarioLiberacaoRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public GfdFuncionarioLiberarOutputDto execute(GfdFuncionarioLiberarInputDto inputDto) {
        var gfdFuncionarioFound = gfdFuncionarioRepository.findById(inputDto.getId()).orElseThrow(() -> new NotFoundException(
                GfdFuncionarioCodeException.GFD_FUNCIONARIO_NAO_ENCONTRADO.getMensagem(),
                GfdFuncionarioCodeException.GFD_FUNCIONARIO_NAO_ENCONTRADO.getCode()));

        // verifica se o funcionario tem algum documento pendente
        // caso afirmativo, é obrigado a enviar a justificativa
        var documents = gfdFuncionarioRepository.getAllDocuments(inputDto.getId(), null);

        var documentosPendentes = documents.getTotalEnviado() > 0 ||
                                  documents.getTotalNaoConforme() > 0 ||
                                  documents.getTotalEmAnalise() > 0 ||
                                  documents.getNaoEnviado() > 0;

        var funcionarioNaoLiberado = gfdFuncionarioFound.getLiberado() == 0;

        if (documentosPendentes && funcionarioNaoLiberado) {
            if (inputDto.getJustificativa() == null || inputDto.getJustificativa().isEmpty()) {
                throw new NotFoundException(
                        GfdFuncionarioCodeException.GFD_FUNCIONARIO_LIBERACAO_COM_DOCUMENTO_PENDENTE.getMensagem(),
                        GfdFuncionarioCodeException.GFD_FUNCIONARIO_LIBERACAO_COM_DOCUMENTO_PENDENTE.getCode());
            }
        }

        gfdFuncionarioFound.setLiberado(inputDto.getLiberado());

        // salva log desse liberação
        var gfdFuncionarioLiberacao = GfdFuncionarioLiberacao.builder()
                .funcionario(gfdFuncionarioFound)
                .data(LocalDateTime.now())
                .statusLiberado(inputDto.getLiberado())
                .justificativa(inputDto.getJustificativa())
                .usuarioCodigo(inputDto.getUsuario())
                .build();

        gfdFuncionarioLiberacaoRepository.save(gfdFuncionarioLiberacao);

        return modelMapper.map(gfdFuncionarioRepository.save(gfdFuncionarioFound), GfdFuncionarioLiberarOutputDto.class);
    }
}
