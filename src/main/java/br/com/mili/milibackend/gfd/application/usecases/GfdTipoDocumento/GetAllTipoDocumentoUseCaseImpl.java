package br.com.mili.milibackend.gfd.application.usecases.GfdTipoDocumento;

import br.com.mili.milibackend.gfd.application.dto.gfdTipoDocumento.GfdTipoDocumentoGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdTipoDocumento.GfdTipoDocumentoGetAllOutputDto;
import br.com.mili.milibackend.gfd.domain.entity.GfdTipoDocumento;
import br.com.mili.milibackend.gfd.domain.usecases.GetAllTipoDocumentoUseCase;
import br.com.mili.milibackend.gfd.infra.repository.GfdTipoDocumentoRepository;
import br.com.mili.milibackend.gfd.infra.specification.GfdTipoDocumentoSpecification;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GetAllTipoDocumentoUseCaseImpl implements GetAllTipoDocumentoUseCase {
    private final GfdTipoDocumentoRepository gfdTipoDocumentoRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<GfdTipoDocumentoGetAllOutputDto> execute(GfdTipoDocumentoGetAllInputDto inputDto) {
        Specification<GfdTipoDocumento> spec = Specification.where(null);

        //filtra por tipo
        if (inputDto.getTipo() != null) {
            spec = spec.and(GfdTipoDocumentoSpecification.filtroTipo(inputDto.getTipo()));
        }

        //filtra por nome
        if (inputDto.getNome() != null) {
            spec = spec.and(GfdTipoDocumentoSpecification.filtroNome(inputDto.getNome()));
        }

        //filtra por id
        if (inputDto.getId() != null) {
            spec = spec.and(GfdTipoDocumentoSpecification.filtroId(inputDto.getId()));
        }

        //filtra apenas os ativos
        spec = spec.and(GfdTipoDocumentoSpecification.filtroAtivo(true));

        var listTipoDocumento = gfdTipoDocumentoRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "id"));

        return listTipoDocumento.stream().map(tipoDocumento -> modelMapper.map(tipoDocumento, GfdTipoDocumentoGetAllOutputDto.class)).toList();
    }
}
