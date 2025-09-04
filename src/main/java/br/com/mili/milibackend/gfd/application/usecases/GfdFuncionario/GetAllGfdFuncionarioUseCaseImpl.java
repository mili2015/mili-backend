package br.com.mili.milibackend.gfd.application.usecases.GfdFuncionario;

import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioGetAllOutputDto;
import br.com.mili.milibackend.gfd.domain.entity.GfdLocalTrabalho;
import br.com.mili.milibackend.gfd.domain.usecases.GetAllGfdFuncionarioUseCase;
import br.com.mili.milibackend.gfd.infra.projections.GfdFuncionarioStatusProjection;
import br.com.mili.milibackend.gfd.infra.repository.GfdLocalTrabalhoRepository;
import br.com.mili.milibackend.gfd.infra.repository.gfdFuncionario.GfdFuncionarioRepository;
import br.com.mili.milibackend.shared.page.pagination.MyPage;
import br.com.mili.milibackend.shared.page.pagination.PageBaseImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetAllGfdFuncionarioUseCaseImpl implements GetAllGfdFuncionarioUseCase {
    private final GfdFuncionarioRepository gfdFuncionarioRepository;
    private final GfdLocalTrabalhoRepository gfdLocalTrabalhoRepository;
    private final ModelMapper modelMapper;

    @Override
    public MyPage<GfdFuncionarioGetAllOutputDto> execute(GfdFuncionarioGetAllInputDto inputDto) {
        var pageNumber = inputDto.getPageable().getPage() > 0 ? inputDto.getPageable().getPage() - 1 : 0;
        var pageSize = inputDto.getPageable().getSize() > 0 ? inputDto.getPageable().getSize() : 20;

        String nome = wrapLike(inputDto.getNome());
        String funcao = wrapLike(inputDto.getFuncao());

        var gfdFuncionarioStatusProjection = gfdFuncionarioRepository.getAll(
                inputDto.getId(),
                nome,
                funcao,
                inputDto.getTipoContratacao(),
                inputDto.getPeriodoInicio(),
                inputDto.getPeriodoFim(),
                inputDto.getFornecedor() != null ? inputDto.getFornecedor().getCodigo() : null,
                pageNumber * pageSize,
                pageSize,
                inputDto.getAtivo() != null ? inputDto.getAtivo() : 1
        );

        var totalCount = gfdFuncionarioRepository.getAllCount(
                inputDto.getId(),
                inputDto.getNome(),
                inputDto.getFuncao(),
                inputDto.getTipoContratacao(),
                inputDto.getPeriodoInicio(),
                inputDto.getPeriodoFim(),
                inputDto.getFornecedor() != null ? inputDto.getFornecedor().getCodigo() : null,
                inputDto.getAtivo() != null ? inputDto.getAtivo() : 1
        );

        var locaisPorFuncionarioMap = buildLocaisPorFuncionarioMap(gfdFuncionarioStatusProjection);

        // Mapeia para DTOs
        List<GfdFuncionarioGetAllOutputDto> outputDtos = gfdFuncionarioStatusProjection.stream()
                .map(proj -> mapToDto(proj, locaisPorFuncionarioMap))
                .toList();

        return new PageBaseImpl<>(outputDtos, pageNumber + 1, pageSize, totalCount) {
        };
    }

    private Map<Integer, List<GfdFuncionarioGetAllOutputDto.LocalTrabalhoDto>> buildLocaisPorFuncionarioMap(List<GfdFuncionarioStatusProjection> gfdFuncionarioStatusProjection) {
        var ids = gfdFuncionarioStatusProjection.stream()
                .map(GfdFuncionarioStatusProjection::getId)
                .toList();

        var locaisTrabalhoFuncionarios = gfdLocalTrabalhoRepository.findByInIdFuncionario(ids);

        return locaisTrabalhoFuncionarios.stream()
                .collect(Collectors.groupingBy(
                                GfdLocalTrabalho::getIdFuncionario,
                                Collectors.mapping(
                                        local -> new GfdFuncionarioGetAllOutputDto.LocalTrabalhoDto(
                                                local.getCtempCodigo()), Collectors.toList()
                                )
                        )
                );
    }

    private GfdFuncionarioGetAllOutputDto mapToDto(
            GfdFuncionarioStatusProjection projection,
            Map<Integer, List<GfdFuncionarioGetAllOutputDto.LocalTrabalhoDto>> locaisPorFuncionario
    ) {
        var dto = modelMapper.map(projection, GfdFuncionarioGetAllOutputDto.class);

        var fornecedorDto = new GfdFuncionarioGetAllOutputDto.FornecedorDto();
        fornecedorDto.setCodigo(projection.getCtforCodigo());
        dto.setFornecedor(fornecedorDto);

        dto.setLocaisTrabalho(locaisPorFuncionario.getOrDefault(projection.getId(), List.of()));

        return dto;
    }

    private String wrapLike(String value) {
        return value != null ? "%" + value + "%" : null;
    }

}
