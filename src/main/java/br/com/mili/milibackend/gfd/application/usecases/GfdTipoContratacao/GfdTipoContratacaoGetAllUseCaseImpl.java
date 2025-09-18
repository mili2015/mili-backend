package br.com.mili.milibackend.gfd.application.usecases.GfdTipoContratacao;

import br.com.mili.milibackend.gfd.application.dto.tipoContratacao.GfdTipoContratacaoGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.tipoContratacao.GfdTipoContratacaoGetAllOutputDto;
import br.com.mili.milibackend.gfd.domain.entity.GfdTipoContratacao;
import br.com.mili.milibackend.gfd.domain.usecases.gfdTipoContratacao.GfdTipoContratacaoGetAllUseCase;
import br.com.mili.milibackend.gfd.infra.repository.GfdTipoContratacaoRepository;
import br.com.mili.milibackend.gfd.infra.specification.GfdTipoContratacaoSpecification;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GfdTipoContratacaoGetAllUseCaseImpl implements GfdTipoContratacaoGetAllUseCase {
    private final GfdTipoContratacaoRepository gfdTipoContratacaoRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<GfdTipoContratacaoGetAllOutputDto> execute(GfdTipoContratacaoGetAllInputDto inputDto) {
        Specification<GfdTipoContratacao> spec = Specification.where(null);

        if (inputDto.getId() != null) {
            spec = spec.and(GfdTipoContratacaoSpecification.filtroId(inputDto.getId()));
        }

        if (inputDto.getDescricao() != null) {
            spec = spec.and(GfdTipoContratacaoSpecification.filtroDescricaoContem(inputDto.getDescricao()));
        }

        var result = gfdTipoContratacaoRepository.findAll(spec);

        return result.stream().map(tipoContratacao -> modelMapper.map(tipoContratacao, GfdTipoContratacaoGetAllOutputDto.class)).toList();
    }
}
