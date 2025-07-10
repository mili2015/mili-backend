package br.com.mili.milibackend.fornecedor.application.service;

import br.com.mili.milibackend.fornecedor.application.dto.gfdFuncionario.*;
import br.com.mili.milibackend.fornecedor.domain.entity.GfdFuncionario;
import br.com.mili.milibackend.fornecedor.domain.interfaces.service.IGfdFuncionarioService;
import br.com.mili.milibackend.fornecedor.infra.repository.gfdFuncionario.GfdFuncionarioRepository;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import br.com.mili.milibackend.shared.page.pagination.MyPage;
import br.com.mili.milibackend.shared.page.pagination.PageBaseImpl;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.mili.milibackend.fornecedor.adapter.exception.GfdFuncionarioCodeException.GFD_FUNCIONARIO_NAO_ENCONTRADO;

@Service
public class GfdFuncionarioService implements IGfdFuncionarioService {
    private final GfdFuncionarioRepository gfdFuncionarioRepository;
    private final ModelMapper modelMapper;

    public GfdFuncionarioService(GfdFuncionarioRepository gfdFuncionarioRepository, ModelMapper modelMapper) {
        this.gfdFuncionarioRepository = gfdFuncionarioRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public MyPage<GfdFuncionarioGetAllOutputDto> getAll(GfdFuncionarioGetAllInputDto inputDto) {
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
                pageNumber,
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
