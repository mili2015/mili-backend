package br.com.mili.milibackend.trade.odometro.application.usecase;

import br.com.mili.milibackend.shared.page.pagination.MyPage;
import br.com.mili.milibackend.shared.page.pagination.PageBaseImpl;
import br.com.mili.milibackend.trade.odometro.application.dto.TradeOdometroGetAllInputDto;
import br.com.mili.milibackend.trade.odometro.application.dto.TradeOdometroGetAllOutputDto;
import br.com.mili.milibackend.trade.odometro.domain.entity.Odometro;
import br.com.mili.milibackend.trade.odometro.domain.usecase.GetAllOdometroUseCase;
import br.com.mili.milibackend.trade.odometro.infra.repository.OdometroRepository;
import br.com.mili.milibackend.trade.odometro.infra.specification.OdometroSpecification;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAllOdometroUseCaseImpl implements GetAllOdometroUseCase {

    private final OdometroRepository odometerRepository;
    private final ModelMapper modelMapper;

    @Override
    public MyPage<TradeOdometroGetAllOutputDto> execute(TradeOdometroGetAllInputDto inputDto) {
        Specification<Odometro> spec = Specification.where(null);

        var pageNumber = inputDto.getPageable().getPage() > 0 ? inputDto.getPageable().getPage() - 1 : 0;
        var pageSize = inputDto.getPageable().getSize() > 0 ? inputDto.getPageable().getSize() : 20;

        spec = aplicarFiltro(inputDto, spec);

        var page = odometerRepository.getAll(spec, PageRequest.of(pageNumber, pageSize));

        var dto = page.getContent().stream().map(content -> modelMapper.map(content, TradeOdometroGetAllOutputDto.class)).toList();

        return new PageBaseImpl<>(dto, page.getPageable().getPageNumber() + 1, page.getSize(), page.getTotalElements()) {};
    }

    private static Specification<Odometro> aplicarFiltro(TradeOdometroGetAllInputDto inputDto, Specification<Odometro> spec) {
        spec = spec.and(OdometroSpecification.withDataInicioBetween(inputDto.getDataInicio(), inputDto.getDataFim()));

        spec = spec.and(OdometroSpecification.withIdColaborador(inputDto.getIdColaborador()));

        spec = spec.and(OdometroSpecification.withTipoVeiculo(inputDto.getTipoVeiculo()));

        spec = spec.and(OdometroSpecification.withId(inputDto.getId()));

        return spec;
    }
}