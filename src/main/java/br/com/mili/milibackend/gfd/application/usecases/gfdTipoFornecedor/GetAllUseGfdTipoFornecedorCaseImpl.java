package br.com.mili.milibackend.gfd.application.usecases.gfdTipoFornecedor;

import br.com.mili.milibackend.gfd.application.dto.gfdTipoFornecedor.GfdTipoFornecedorGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdTipoFornecedor.GfdTipoFornecedorGetAllOutputDto;
import br.com.mili.milibackend.gfd.domain.entity.GfdTipoFornecedor;
import br.com.mili.milibackend.gfd.domain.usecases.gfdTipoFornecedor.GetAllUseGfdTipoFornecedorCase;
import br.com.mili.milibackend.gfd.infra.repository.GfdTipoFornecedorRepository;
import br.com.mili.milibackend.gfd.infra.specification.GfdTipoFornecedorSpecification;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllUseGfdTipoFornecedorCaseImpl implements GetAllUseGfdTipoFornecedorCase {
    private final GfdTipoFornecedorRepository gfdTipoFornecedorRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<GfdTipoFornecedorGetAllOutputDto> execute(GfdTipoFornecedorGetAllInputDto inputDto) {
        Specification<GfdTipoFornecedor> spec = Specification.where(null);

        if (inputDto.getId() != null) {
            spec = spec.and(GfdTipoFornecedorSpecification.filtroId(inputDto.getId()));
        }

        if (inputDto.getDescricao() != null) {
            spec = spec.and(GfdTipoFornecedorSpecification.filtroDescricaoContem(inputDto.getDescricao()));
        }

        var result = gfdTipoFornecedorRepository.findAll(spec);

        return result.stream().map(tipoFornecedor -> modelMapper.map(tipoFornecedor, GfdTipoFornecedorGetAllOutputDto.class)).toList();
    }
}
