package br.com.mili.milibackend.gfd.application.usecases.GfdFuncionario;

import br.com.mili.milibackend.gfd.adapter.exception.GfdFuncionarioCodeException;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioUpdateObservacaoInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioUpdateObservacaoOutputDto;
import br.com.mili.milibackend.gfd.domain.usecases.gfdFuncionario.UpdateObservacaoFuncionarioUseCase;
import br.com.mili.milibackend.gfd.infra.repository.gfdFuncionario.GfdFuncionarioRepository;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateObservacaoFuncionarioUseCaseImpl implements UpdateObservacaoFuncionarioUseCase {
    private final GfdFuncionarioRepository gfdFuncionarioRepository;
    private final ModelMapper modelMapper;

    @Override
    public GfdFuncionarioUpdateObservacaoOutputDto execute(GfdFuncionarioUpdateObservacaoInputDto inputDto) {
        var gfdFuncionarioFound = gfdFuncionarioRepository.findById(inputDto.getId()).orElseThrow(() -> new NotFoundException(
                GfdFuncionarioCodeException.GFD_FUNCIONARIO_NAO_ENCONTRADO.getMensagem(),
                GfdFuncionarioCodeException.GFD_FUNCIONARIO_NAO_ENCONTRADO.getCode()));

        gfdFuncionarioFound.setObservacao(inputDto.getObservacao());

        gfdFuncionarioRepository.save(gfdFuncionarioFound);

        return modelMapper.map(gfdFuncionarioFound, GfdFuncionarioUpdateObservacaoOutputDto.class);
    }
}
