package br.com.mili.milibackend.fornecedor.application.service;


import br.com.mili.milibackend.fornecedor.application.dto.*;
import br.com.mili.milibackend.fornecedor.domain.entity.Fornecedor;
import br.com.mili.milibackend.fornecedor.domain.interfaces.service.IFornecedorService;
import br.com.mili.milibackend.fornecedor.domain.usecases.ValidatePermissionFornecedorUseCase;
import br.com.mili.milibackend.fornecedor.infra.repository.fornecedorRepository.FornecedorRepository;
import br.com.mili.milibackend.fornecedor.infra.specification.FornecedorSpecification;
import br.com.mili.milibackend.fornecedor.application.factory.GetAllFornecedorSpecificationFactory;
import br.com.mili.milibackend.gfd.domain.entity.GfdTipoFornecedor;
import br.com.mili.milibackend.gfd.domain.usecases.gfdTipoFornecedor.GetByIdGfdTipoFornecedorUseCase;
import br.com.mili.milibackend.shared.exception.types.ForbiddenException;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import br.com.mili.milibackend.shared.page.pagination.MyPage;
import br.com.mili.milibackend.shared.page.pagination.PageBaseImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.mili.milibackend.fornecedor.adapter.exception.FornecedorCodeException.FORNECEDOR_NAO_ENCONTRADO;
import static br.com.mili.milibackend.gfd.adapter.exception.GfdMCodeException.GFD_FUNCIONARIO_SEM_PERMISSAO;
import static br.com.mili.milibackend.gfd.adapter.exception.GfdMCodeException.GFD_TIPO_FORNECEDOR_NAO_ENCONTRADO;

@Service
@RequiredArgsConstructor
public class FornecedorService implements IFornecedorService {
    private final FornecedorRepository fornecedorRepository;
    private final ValidatePermissionFornecedorUseCase validatePermissionFornecedorUseCase;
    private final GetByIdGfdTipoFornecedorUseCase getByIdGfdTipoFornecedorUseCase;

    private final ModelMapper modelMapper;
    private final GetAllFornecedorSpecificationFactory getAllFornecedorSpecificationFactory;


    public FornecedorGetByCodUsuarioOutputDto getByCodUsuario(FornecedorGetByCodUsuarioInputDto inputDto) {
        Fornecedor fornecedorFound = fornecedorRepository.findByCodUsuario(inputDto.getCodUsuario()).orElse(null);

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

        if (!validatePermissionFornecedorUseCase.execute(inputDto.getCodUsuario(), fornecedorFound.getCodigo())) {
            throw new ForbiddenException(GFD_FUNCIONARIO_SEM_PERMISSAO.getMensagem(), GFD_FUNCIONARIO_SEM_PERMISSAO.getCode());
        }

        fornecedorFound.setContato(inputDto.getContato());
        fornecedorFound.setEmail(String.join(";", inputDto.getEmails()));
        fornecedorFound.setCelular(inputDto.getCelular());
        fornecedorFound.setAceiteLgpd(inputDto.getAceiteLgpd());

        // busca o tipo fornecedor pelo codigo
        var tipo = getByIdGfdTipoFornecedorUseCase.execute(inputDto.getTipoFornecedor().getId());

        if (tipo == null) {
            throw new NotFoundException(GFD_TIPO_FORNECEDOR_NAO_ENCONTRADO.getMensagem(), GFD_TIPO_FORNECEDOR_NAO_ENCONTRADO.getCode());
        }

        fornecedorFound.setTipoFornecedor(
                GfdTipoFornecedor.builder()
                        .id(tipo.getId()).build()
        );

        fornecedorRepository.save(fornecedorFound);

        return modelMapper.map(fornecedorFound, FornecedorMeusDadosUpdateOutputDto.class);
    }

    public MyPage<FornecedorGetAllOutputDto> getAll(FornecedorGetAllInputDto inputDto) {
        Specification<Fornecedor> spec = getAllFornecedorSpecificationFactory.aplicarFiltro(inputDto);

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
