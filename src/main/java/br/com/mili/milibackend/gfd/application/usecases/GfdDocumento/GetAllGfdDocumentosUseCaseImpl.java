package br.com.mili.milibackend.gfd.application.usecases.GfdDocumento;

import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoGetAllOutputDto;
import br.com.mili.milibackend.gfd.domain.entity.GfdDocumento;
import br.com.mili.milibackend.gfd.domain.entity.GfdDocumentoStatusEnum;
import br.com.mili.milibackend.gfd.domain.usecases.GetAllGfdDocumentosUseCase;
import br.com.mili.milibackend.gfd.infra.repository.gfdDocumento.GfdDocumentoRepository;
import br.com.mili.milibackend.gfd.infra.specification.GfdDocumentoSpecification;
import br.com.mili.milibackend.shared.page.pagination.MyPage;
import br.com.mili.milibackend.shared.page.pagination.PageBaseImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllGfdDocumentosUseCaseImpl implements GetAllGfdDocumentosUseCase {
    private final GfdDocumentoRepository repository;
    private final ModelMapper modelMapper;

    @Override
    public MyPage<GfdDocumentoGetAllOutputDto> execute(GfdDocumentoGetAllInputDto inputDto) {
        Specification<GfdDocumento> spec = Specification.where(null);

        spec = aplicarFiltros(inputDto, spec);

        //page
        var pageNumber = inputDto.getPageable().getPage() > 0 ? inputDto.getPageable().getPage() - 1 : 0;
        var pageSize = inputDto.getPageable().getSize() > 0 ? inputDto.getPageable().getSize() : 20;

        var pageGfdDocumentos = repository.getAll(spec, PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "id")));

        List<GfdDocumentoGetAllOutputDto> gfdDocumentoGetAllOutputDto = pageGfdDocumentos.getContent().stream()
                .map(gfdDocumento -> modelMapper.map(gfdDocumento, GfdDocumentoGetAllOutputDto.class))
                .toList();

        return new PageBaseImpl<>(gfdDocumentoGetAllOutputDto, pageGfdDocumentos.getPageable().getPageNumber() + 1, pageGfdDocumentos.getSize(), pageGfdDocumentos.getTotalElements()) {
        };
    }

    private static Specification<GfdDocumento> aplicarFiltros(GfdDocumentoGetAllInputDto inputDto, Specification<GfdDocumento> spec) {
        //filtra por fornecedor
        if (inputDto.getCtforCodigo() != null) {
            spec = spec.and(GfdDocumentoSpecification.filtroPorFornecedor(inputDto.getCtforCodigo()));
        }

        //filtra por funcionario
        spec = filtroFuncionario(inputDto, spec);


        //filtra por nome do arquivo
        if (inputDto.getNomeArquivo() != null) {
            spec = spec.and(GfdDocumentoSpecification.filtroNomeArquivoContem(inputDto.getNomeArquivo()));
        }

        //filtra por status
        if (inputDto.getStatus() != null) {
            spec = spec.and(GfdDocumentoSpecification.filtroStatus(GfdDocumentoStatusEnum.valueOf(inputDto.getStatus())));
        }

        //filtra por tipo
        if (inputDto.getTipoDocumentoId() != null) {
            spec = spec.and(GfdDocumentoSpecification.filtroPorTipo(inputDto.getTipoDocumentoId()));
        }

        //filtra por range dataCadastro
        spec = spec.and(GfdDocumentoSpecification.filtroRangeDataCadastro(inputDto.getDataCadastroInic(), inputDto.getDataCadastroFinal()));

        //filtra por range dataValidade
        spec = spec.and(GfdDocumentoSpecification.filtroRangeDataValidade(inputDto.getDataValidadeInic(), inputDto.getDataValidadeFinal()));

        //filtra por range dataEmissao
        spec = spec.and(GfdDocumentoSpecification.filtroRangeDataEmissao(inputDto.getDataEmissaoInic(), inputDto.getDataEmissaoFinal()));

        //filtra por periodo
        spec = spec.and(GfdDocumentoSpecification.filtroPeriodo(inputDto.getPeriodo()));
        return spec;
    }

    private static Specification<GfdDocumento> filtroFuncionario(GfdDocumentoGetAllInputDto inputDto, Specification<GfdDocumento> spec) {
        var funcionario = inputDto.getFuncionario();

        if (funcionario != null) {
            if (funcionario.getId() != null) {
                spec = spec.and(GfdDocumentoSpecification.filtroFornecedorId(funcionario.getId()));
            }

            // filtra por nome
            if (funcionario.getNome() != null) {
                spec = spec.and(GfdDocumentoSpecification.filtroFornecedorNome(funcionario.getNome()));
            }

            // filtra por cpf
            if (funcionario.getCpf() != null) {
                spec = spec.and(GfdDocumentoSpecification.filtroFornecedorCpf(funcionario.getCpf()));
            }
        }

        return spec;
    }
}
