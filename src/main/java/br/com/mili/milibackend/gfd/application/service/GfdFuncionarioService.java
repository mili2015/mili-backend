package br.com.mili.milibackend.gfd.application.service;

import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.*;
import br.com.mili.milibackend.gfd.domain.entity.GfdFuncionario;
import br.com.mili.milibackend.gfd.domain.interfaces.IGfdFuncionarioService;
import br.com.mili.milibackend.gfd.infra.repository.gfdFuncionario.GfdFuncionarioRepository;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import static br.com.mili.milibackend.gfd.adapter.exception.GfdFuncionarioCodeException.GFD_FUNCIONARIO_NAO_ENCONTRADO;

@Service
public class GfdFuncionarioService implements IGfdFuncionarioService {
    private final GfdFuncionarioRepository gfdFuncionarioRepository;
    private final ModelMapper modelMapper;

    public GfdFuncionarioService(GfdFuncionarioRepository gfdFuncionarioRepository, ModelMapper modelMapper) {
        this.gfdFuncionarioRepository = gfdFuncionarioRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public GfdFuncionarioCreateOutputDto create(GfdFuncionarioCreateInputDto inputDto) {
        var gfdFuncionario = modelMapper.map(inputDto, GfdFuncionario.class);

        gfdFuncionario.setAtivo(1);

        return modelMapper.map(gfdFuncionarioRepository.save(gfdFuncionario), GfdFuncionarioCreateOutputDto.class);
    }

    @Override
    public GfdFuncionarioUpdateOutputDto update(GfdFuncionarioUpdateInputDto inputDto) {
        var id = inputDto.getId();

        var gfdFuncionario = gfdFuncionarioRepository.findById(id).orElse(null);

        if (gfdFuncionario == null) {
            throw new NotFoundException(GFD_FUNCIONARIO_NAO_ENCONTRADO.getMensagem(), GFD_FUNCIONARIO_NAO_ENCONTRADO.getCode());
        }

        modelMapper.map(inputDto, gfdFuncionario);

        return modelMapper.map(gfdFuncionario, GfdFuncionarioUpdateOutputDto.class);
    }

    @Override
    public GfdFuncionarioGetByIdOutputDto getById(Integer id) {
        var gfdFuncionario = gfdFuncionarioRepository.findById(id).orElse(null);

        if (gfdFuncionario == null) {
            return null;
        }

        return modelMapper.map(gfdFuncionario, GfdFuncionarioGetByIdOutputDto.class);
    }

    @Override
    public void delete(GfdFuncionarioDeleteInputDto inputDto) {
        var id = inputDto.getId();

        var gfdFuncionario = gfdFuncionarioRepository.findById(id).orElse(null);

        if (gfdFuncionario == null) {
            throw new NotFoundException(GFD_FUNCIONARIO_NAO_ENCONTRADO.getMensagem(), GFD_FUNCIONARIO_NAO_ENCONTRADO.getCode());
        }

        gfdFuncionario.setAtivo(0);

        gfdFuncionarioRepository.save(gfdFuncionario);
    }

}
