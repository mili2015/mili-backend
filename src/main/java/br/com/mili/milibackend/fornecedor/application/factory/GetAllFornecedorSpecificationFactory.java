package br.com.mili.milibackend.fornecedor.application.factory;

import br.com.mili.milibackend.fornecedor.application.dto.FornecedorGetAllInputDto;
import br.com.mili.milibackend.fornecedor.domain.entity.Fornecedor;
import br.com.mili.milibackend.fornecedor.infra.specification.FornecedorSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class GetAllFornecedorSpecificationFactory {

    public Specification<Fornecedor> aplicarFiltro(FornecedorGetAllInputDto inputDto) {
        Specification<Fornecedor> spec = Specification.where(null);
        if (inputDto == null) return spec;

        spec = spec
                .and(FornecedorSpecification.filtroCodigo(inputDto.getCodigo()))
                .and(FornecedorSpecification.filtroRazaoSocial(inputDto.getRazaoSocial()))
                .and(FornecedorSpecification.filtroCgcCpf(inputDto.getCgcCpf()))
                .and(FornecedorSpecification.filtroAtivoGfd(inputDto.getAtivoGfd()));

        return spec;
    }
}
