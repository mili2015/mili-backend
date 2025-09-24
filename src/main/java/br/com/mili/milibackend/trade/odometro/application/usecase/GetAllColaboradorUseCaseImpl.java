package br.com.mili.milibackend.trade.odometro.application.usecase;

import br.com.mili.milibackend.shared.page.pagination.MyPage;
import br.com.mili.milibackend.shared.page.pagination.PageBaseImpl;
import br.com.mili.milibackend.trade.odometro.application.dto.TradeColaboradorGetAllInputDto;
import br.com.mili.milibackend.trade.odometro.application.dto.TradeColaboradorGetAllOutputDto;
import br.com.mili.milibackend.trade.odometro.domain.entity.Colaborador;
import br.com.mili.milibackend.trade.odometro.domain.usecase.GetAllColaboradorUseCase;
import br.com.mili.milibackend.trade.odometro.infra.repository.ColaboradorRepository;
import br.com.mili.milibackend.trade.odometro.infra.specification.ColaboradorSpecification;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAllColaboradorUseCaseImpl implements GetAllColaboradorUseCase {

    private final ColaboradorRepository colaboradorRepository;
    private final ModelMapper modelMapper;

    @Override
    public MyPage<TradeColaboradorGetAllOutputDto> execute(TradeColaboradorGetAllInputDto inputDto) {
        Specification<Colaborador> spec = Specification.where(null);

        int pageNumber = inputDto.getPageable().getPage() > 0 ? inputDto.getPageable().getPage() - 1 : 0;
        int pageSize = inputDto.getPageable().getSize() > 0 ? inputDto.getPageable().getSize() : 20;

        spec = aplicarFiltro(inputDto, spec);

        var page = colaboradorRepository.findAll(spec, PageRequest.of(pageNumber, pageSize));

        var dto = page.getContent().stream()
                .map(content -> modelMapper.map(content, TradeColaboradorGetAllOutputDto.class))
                .toList();

        return new PageBaseImpl<>(dto, page.getPageable().getPageNumber() + 1, page.getSize(), page.getTotalElements()) {};
    }

    private static Specification<Colaborador> aplicarFiltro(TradeColaboradorGetAllInputDto inputDto, Specification<Colaborador> spec) {
        spec = spec
                .and(ColaboradorSpecification.withId(inputDto.getId()))
                .and(ColaboradorSpecification.withNomeCompletoLike(inputDto.getNome()))
                .and(ColaboradorSpecification.withEmailLike(inputDto.getEmail()))
                .and(ColaboradorSpecification.withCpf(inputDto.getCpf()))
                .and(ColaboradorSpecification.withAtivo(inputDto.getAtivo()))
                .and(ColaboradorSpecification.withIdColaboradorSuperior(inputDto.getIdColaboradorSuperior()));

        return spec;
    }
}
