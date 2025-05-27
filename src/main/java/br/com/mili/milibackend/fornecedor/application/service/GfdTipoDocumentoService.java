package br.com.mili.milibackend.fornecedor.application.service;

import br.com.mili.milibackend.fornecedor.application.dto.GfdTipoDocumentoGetByIdOutputDto;
import br.com.mili.milibackend.fornecedor.application.dto.gfdTipoDocumento.GfdTipoDocumentoGetAllInputDto;
import br.com.mili.milibackend.fornecedor.application.dto.gfdTipoDocumento.GfdTipoDocumentoGetAllOutputDto;
import br.com.mili.milibackend.fornecedor.domain.entity.GfdTipoDocumento;
import br.com.mili.milibackend.fornecedor.domain.interfaces.service.IGfdTipoDocumentoService;
import br.com.mili.milibackend.fornecedor.domain.specification.GfdTipoDocumentoSpecification;
import br.com.mili.milibackend.fornecedor.infra.repository.GfdTipoDocumentoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GfdTipoDocumentoService implements IGfdTipoDocumentoService {
    private final GfdTipoDocumentoRepository gfdTipoDocumentoRepository;
    private final ModelMapper modelMapper;

    public GfdTipoDocumentoService(GfdTipoDocumentoRepository gfdTipoDocumentoRepository, ModelMapper modelMapper) {
        this.gfdTipoDocumentoRepository = gfdTipoDocumentoRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<GfdTipoDocumentoGetAllOutputDto> getAll(GfdTipoDocumentoGetAllInputDto inputDto) {
        Specification<GfdTipoDocumento> spec = Specification.where(null);

        //filtra por fornecedor
        if (inputDto.getTipo() != null) {
            spec = spec.and(GfdTipoDocumentoSpecification.filtroTipo(inputDto.getTipo()));
        }

        //filtra apenas os ativos
        spec = spec.and(GfdTipoDocumentoSpecification.filtroAtivo(true));

        var listTipoDocumento = gfdTipoDocumentoRepository.findAll(spec, Sort.by("id"));

        return listTipoDocumento.stream().map(tipoDocumento -> modelMapper.map(tipoDocumento, GfdTipoDocumentoGetAllOutputDto.class)).toList();
    }


    @Override
    public GfdTipoDocumentoGetByIdOutputDto getById(Integer id) {
        var gfdTipoDocumento = gfdTipoDocumentoRepository.findById(id);

        if (gfdTipoDocumento.isEmpty()) {
            return null;
        }

        return modelMapper.map(gfdTipoDocumento.get(), GfdTipoDocumentoGetByIdOutputDto.class);
    }

}
