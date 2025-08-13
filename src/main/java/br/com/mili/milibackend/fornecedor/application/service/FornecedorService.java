package br.com.mili.milibackend.fornecedor.application.service;


import br.com.mili.milibackend.fornecedor.application.dto.*;
import br.com.mili.milibackend.fornecedor.domain.entity.Fornecedor;
import br.com.mili.milibackend.fornecedor.domain.interfaces.service.IFornecedorService;
import br.com.mili.milibackend.fornecedor.infra.specification.FornecedorSpecification;
import br.com.mili.milibackend.fornecedor.infra.repository.fornecedorRepository.FornecedorRepository;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import br.com.mili.milibackend.shared.page.pagination.MyPage;
import br.com.mili.milibackend.shared.page.pagination.PageBaseImpl;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.mili.milibackend.fornecedor.adapter.exception.FornecedorCodeException.FORNECEDOR_NAO_ENCONTRADO;

@Service
public class FornecedorService implements IFornecedorService {

    private final FornecedorRepository fornecedorRepository;

    private final ModelMapper modelMapper;

    public FornecedorService(FornecedorRepository fornecedorRepository, ModelMapper modelMapper) {
        this.fornecedorRepository = fornecedorRepository;
        this.modelMapper = modelMapper;
    }

    public FornecedorGetByCodUsuarioOutputDto getByCodUsuario(FornecedorGetByCodUsuarioInputDto inputDto) {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setCodUsuario(inputDto.getCodUsuario());

        Fornecedor fornecedorFound = fornecedorRepository.findByCodUsuario(fornecedor.getCodUsuario()).orElse(null);

        if (fornecedorFound == null) {
            return null;
        }

        return modelMapper.map(fornecedorFound, FornecedorGetByCodUsuarioOutputDto.class);
    }

    public FornecedorGetByIdOutputDto getById(Integer id) {

        var fornecedorFound = fornecedorRepository.findById(id);

        if (fornecedorFound.isEmpty()) {
            return null;
        }

        return modelMapper.map(fornecedorFound.get(), FornecedorGetByIdOutputDto.class);
    }

    public FornecedorMeusDadosUpdateOutputDto updateMeusDados(FornecedorMeusDadosUpdateInputDto inputDto) {
        Fornecedor fornecedorFound = recuperarFornecedor(inputDto.getCodUsuario(), inputDto.getId());

        if (fornecedorFound == null) {
            throw new NotFoundException(FORNECEDOR_NAO_ENCONTRADO.getMensagem(), FORNECEDOR_NAO_ENCONTRADO.getCode());
        }

        TypeMap<FornecedorMeusDadosUpdateInputDto, Fornecedor> typeMap =
                modelMapper.getTypeMap(FornecedorMeusDadosUpdateInputDto.class, Fornecedor.class);

        if (typeMap == null) {
            typeMap = modelMapper.createTypeMap(FornecedorMeusDadosUpdateInputDto.class, Fornecedor.class)
                    .addMappings(mapper -> mapper.skip(Fornecedor::setCodUsuario));
        }

        typeMap.map(inputDto, fornecedorFound);

        fornecedorRepository.save(fornecedorFound);

        return modelMapper.map(fornecedorFound, FornecedorMeusDadosUpdateOutputDto.class);
    }

    public MyPage<FornecedorGetAllOutputDto> getAll(FornecedorGetAllInputDto inputDto) {
        Specification<Fornecedor> spec = Specification.where(null);

        //filtra por codigo
        if (inputDto.getCodigo() != null) {
            spec = spec.and(FornecedorSpecification.filtroCodigo(inputDto.getCodigo()));
        }

        //filtra por razao social
        if (inputDto.getRazaoSocial() != null) {
            spec = spec.and(FornecedorSpecification.filtroRazaoSocial(inputDto.getRazaoSocial()));
        }

        //filtra por cgcCpf
        if (inputDto.getCgcCpf() != null) {
            spec = spec.and(FornecedorSpecification.filtroCgcCpf(inputDto.getCgcCpf()));
        }

        //page
        var pageNumber = inputDto.getPageable().getPage() > 0 ? inputDto.getPageable().getPage() - 1 : 0;
        var pageSize = inputDto.getPageable().getSize() > 0 ? inputDto.getPageable().getSize() : 20;

        var pageFornecedor = fornecedorRepository.getAll(spec, PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "codigo")));

        List<FornecedorGetAllOutputDto> fornecedorGetAllOutputDto = pageFornecedor.getContent().stream()
                .map(fornecedor -> modelMapper.map(fornecedor, FornecedorGetAllOutputDto.class))
                .toList();

        return new PageBaseImpl<>(fornecedorGetAllOutputDto, pageFornecedor.getPageable().getPageNumber() + 1, pageFornecedor.getSize(), pageFornecedor.getTotalElements()) {
        };
    }

    private Fornecedor recuperarFornecedor(Integer codUsuario, Integer id) {
        Fornecedor fornecedor = null;

        if (id != null) {
            fornecedor = fornecedorRepository.findById(id).orElse(null);
        } else if (codUsuario != null) {
            fornecedor = fornecedorRepository.findByCodUsuario(codUsuario).orElse(null);
        }

        return fornecedor;
    }
}
