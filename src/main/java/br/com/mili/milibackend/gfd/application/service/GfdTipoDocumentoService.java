package br.com.mili.milibackend.gfd.application.service;

import br.com.mili.milibackend.gfd.application.dto.gfdTipoDocumento.*;
import br.com.mili.milibackend.gfd.domain.entity.GfdTipoDocumento;
import br.com.mili.milibackend.gfd.domain.interfaces.IGfdTipoDocumentoService;
import br.com.mili.milibackend.gfd.infra.repository.GfdTipoDocumentoRepository;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import static br.com.mili.milibackend.gfd.adapter.exception.GfdMCodeException.GFD_TIPO_DOCUMENTO_NAO_ENCONTRADO;

@Service
@RequiredArgsConstructor
public class GfdTipoDocumentoService implements IGfdTipoDocumentoService {
    private final GfdTipoDocumentoRepository gfdTipoDocumentoRepository;
    private final GfdDocumentoService gfdDocumentoService;
    private final ModelMapper modelMapper;


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
