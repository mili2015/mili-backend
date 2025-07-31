package br.com.mili.milibackend.gfd.application.service;

import br.com.mili.milibackend.gfd.application.dto.gfdTipoDocumento.*;
import br.com.mili.milibackend.gfd.domain.entity.GfdTipoDocumento;
import br.com.mili.milibackend.gfd.domain.interfaces.IGfdTipoDocumentoService;
import br.com.mili.milibackend.gfd.infra.specification.GfdTipoDocumentoSpecification;
import br.com.mili.milibackend.gfd.infra.repository.GfdTipoDocumentoRepository;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.mili.milibackend.gfd.adapter.exception.GfdMCodeException.GFD_TIPO_DOCUMENTO_NAO_ENCONTRADO;

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

    @Override
    public GfdTipoDocumentoGetByIdOutputDto getById(Integer id) {
        var gfdTipoDocumento = gfdTipoDocumentoRepository.findById(id);

        return gfdTipoDocumento.map(tipoDocumento -> modelMapper.map(tipoDocumento, GfdTipoDocumentoGetByIdOutputDto.class)).orElse(null);

    }

    @Override
    public void delete(Integer id) {
        gfdTipoDocumentoRepository.inactive(id);
    }

    @Override
    public GfdTipoDocumentoCreateOutputDto create(GfdTipoDocumentoCreateInputDto inputDto) {
        var gfdTipoDocumento = modelMapper.map(inputDto, GfdTipoDocumento.class);

        gfdTipoDocumento.setAtivo(true);

        var gfdTipoDocumentoCreated = gfdTipoDocumentoRepository.save(gfdTipoDocumento);

        return modelMapper.map(gfdTipoDocumentoCreated, GfdTipoDocumentoCreateOutputDto.class);
    }

    @Override
    public GfdTipoDocumentoUpdateOutputDto update(GfdTipoDocumentoUpdateInputDto inputDto) {
        var gfdTipoDocumento = gfdTipoDocumentoRepository.findById(inputDto.getId()).orElseThrow(
                () -> new NotFoundException(GFD_TIPO_DOCUMENTO_NAO_ENCONTRADO.getMensagem(), GFD_TIPO_DOCUMENTO_NAO_ENCONTRADO.getCode())
        );

        modelMapper.map(inputDto, gfdTipoDocumento);

        gfdTipoDocumentoRepository.save(gfdTipoDocumento);

        return modelMapper.map(gfdTipoDocumento, GfdTipoDocumentoUpdateOutputDto.class);
    }

}
