package br.com.mili.milibackend.gfd.application.usecases.GfdFuncionarioLiberacao;

import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.gfdFuncionarioLiberacao.GfdFuncionarioLiberacaoGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.gfdFuncionarioLiberacao.GfdFuncionarioLiberacaoGetAllOutputDto;
import br.com.mili.milibackend.gfd.domain.entity.GfdFuncionarioLiberacao;
import br.com.mili.milibackend.gfd.domain.usecases.gfdFuncionario.GetAllGfdFuncionarioLiberacaoUseCase;
import br.com.mili.milibackend.gfd.infra.repository.gfdFuncionario.GfdFuncionarioLiberacaoRepository;
import br.com.mili.milibackend.gfd.infra.specification.GfdFuncionarioLiberacaoSpecification;
import org.modelmapper.ModelMapper;
import lombok.RequiredArgsConstructor;
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

        var resultPage = repository.getAll(spec, pageRequest);

        return resultPage.getContent().stream()
                .map(item -> modelMapper.map(item, GfdFuncionarioLiberacaoGetAllOutputDto.class))
                .toList();
    }
}

