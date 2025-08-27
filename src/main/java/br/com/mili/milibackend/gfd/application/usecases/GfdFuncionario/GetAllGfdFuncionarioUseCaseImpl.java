package br.com.mili.milibackend.gfd.application.usecases.GfdFuncionario;

import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioGetAllOutputDto;
import br.com.mili.milibackend.gfd.domain.usecases.GetAllGfdFuncionarioUseCase;
import br.com.mili.milibackend.gfd.infra.repository.gfdFuncionario.GfdFuncionarioRepository;
import br.com.mili.milibackend.shared.page.pagination.MyPage;
import br.com.mili.milibackend.shared.page.pagination.PageBaseImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllGfdFuncionarioUseCaseImpl implements GetAllGfdFuncionarioUseCase {
    private final GfdFuncionarioRepository gfdFuncionarioRepository;
    private final ModelMapper modelMapper;

    @Override
    public MyPage<GfdFuncionarioGetAllOutputDto> execute(GfdFuncionarioGetAllInputDto inputDto) {
        var pageNumber = inputDto.getPageable().getPage() > 0 ? inputDto.getPageable().getPage() - 1 : 0;
        var pageSize = inputDto.getPageable().getSize() > 0 ? inputDto.getPageable().getSize() : 20;

        var nome = inputDto.getNome() != null ? "%" + inputDto.getNome() + "%" : null;
        var funcao = inputDto.getFuncao() != null ? "%" + inputDto.getFuncao() + "%" : null;

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

        List<GfdFuncionarioGetAllOutputDto> gfdFuncionarioGetAllOutputDto = gfdFuncionarioStatusProjection.stream()
                .map(gfdDocumento -> {
                    var dto = modelMapper.map(gfdDocumento, GfdFuncionarioGetAllOutputDto.class);

                    var fornecedorDto = new GfdFuncionarioGetAllOutputDto.FornecedorDto();
                    fornecedorDto.setCodigo(gfdDocumento.getCtforCodigo());
                    dto.setFornecedor(fornecedorDto);

                    return dto;
                })
                .toList();

        return new PageBaseImpl<>(gfdFuncionarioGetAllOutputDto, pageNumber + 1, pageSize, totalCount) {
        };
    }


}
