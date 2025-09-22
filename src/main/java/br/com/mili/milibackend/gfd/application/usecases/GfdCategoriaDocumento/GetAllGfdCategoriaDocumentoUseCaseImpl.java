package br.com.mili.milibackend.gfd.application.usecases.GfdCategoriaDocumento;

import br.com.mili.milibackend.gfd.application.dto.gfdCategoriaDocumento.GfdCategoriaDocumentoGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdCategoriaDocumento.GfdCategoriaDocumentoGetAllOutputDto;
import br.com.mili.milibackend.gfd.domain.entity.GfdCategoriaDocumento;
import br.com.mili.milibackend.gfd.domain.usecases.gfdCategoriaDocumento.GetAllGfdCategoriaDocumentoUseCase;
import br.com.mili.milibackend.gfd.infra.repository.GfdCategoriaDocumentoRepository;
import br.com.mili.milibackend.gfd.infra.specification.GfdCategoriaDocumentoSpecification;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllGfdCategoriaDocumentoUseCaseImpl implements GetAllGfdCategoriaDocumentoUseCase {
    private final GfdCategoriaDocumentoRepository gfdCategoriaDocumentoRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<GfdCategoriaDocumentoGetAllOutputDto> execute(GfdCategoriaDocumentoGetAllInputDto inputDto) {

        Specification<GfdCategoriaDocumento> spec = Specification.where(null);

        if (inputDto.getId() != null) {
            spec = spec.and(GfdCategoriaDocumentoSpecification.filtroId(inputDto.getId()));
        }

        if (inputDto.getTipo() != null) {
            spec = spec.and(GfdCategoriaDocumentoSpecification.filtroTipo(inputDto.getTipo()));
        }

        if (inputDto.getNome() != null) {
            spec = spec.and(GfdCategoriaDocumentoSpecification.filtrNomeContem(inputDto.getNome()));
        }

        var result = gfdCategoriaDocumentoRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "id"));

        return result.stream().map(tipoContratacao -> modelMapper.map(tipoContratacao, GfdCategoriaDocumentoGetAllOutputDto.class)).toList();
    }
}
