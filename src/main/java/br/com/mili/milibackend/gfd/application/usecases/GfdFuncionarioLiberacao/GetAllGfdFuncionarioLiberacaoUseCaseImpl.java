package br.com.mili.milibackend.gfd.application.usecases.GfdFuncionarioLiberacao;

import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.gfdFuncionarioLiberacao.GfdFuncionarioLiberacaoGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.gfdFuncionarioLiberacao.GfdFuncionarioLiberacaoGetAllOutputDto;
import br.com.mili.milibackend.gfd.domain.entity.GfdFuncionarioLiberacao;
import br.com.mili.milibackend.gfd.domain.usecases.GetAllGfdFuncionarioLiberacaoUseCase;
import br.com.mili.milibackend.gfd.infra.repository.gfdFuncionario.GfdFuncionarioLiberacaoRepository;
import br.com.mili.milibackend.gfd.infra.specification.GfdFuncionarioLiberacaoSpecification;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllGfdFuncionarioLiberacaoUseCaseImpl implements GetAllGfdFuncionarioLiberacaoUseCase {

    private final GfdFuncionarioLiberacaoRepository repository;
    private final ModelMapper modelMapper;

    @Override
    public List<GfdFuncionarioLiberacaoGetAllOutputDto> execute(GfdFuncionarioLiberacaoGetAllInputDto inputDto) {

        Specification<GfdFuncionarioLiberacao> spec = Specification
                .where(GfdFuncionarioLiberacaoSpecification.filtroFuncionarioId(inputDto.getFuncionarioId()))
                .and(GfdFuncionarioLiberacaoSpecification.filtroStatusLiberado(inputDto.getStatusLiberado()))
                .and(GfdFuncionarioLiberacaoSpecification.filtroPeriodo(inputDto.getPeriodoInicio(), inputDto.getPeriodoFim()));

        PageRequest pageRequest = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "id"));

        var resultPage = repository.findAll(spec, pageRequest);

        return resultPage.getContent().stream()
                .map(this::toDto)
                .toList();
    }

    private GfdFuncionarioLiberacaoGetAllOutputDto toDto(GfdFuncionarioLiberacao entity) {
        var dto = modelMapper.map(entity, GfdFuncionarioLiberacaoGetAllOutputDto.class);
        dto.setFuncionarioId(entity.getFuncionario() != null ? entity.getFuncionario().getId() : null);
        return dto;
    }
}
