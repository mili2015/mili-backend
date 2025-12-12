package br.com.mili.milibackend.fornecedor.infra.specification;

import br.com.mili.milibackend.fornecedor.application.dto.FornecedorGetAllInputDto;
import br.com.mili.milibackend.fornecedor.domain.entity.Fornecedor;
import org.springframework.data.jpa.domain.Specification;

public class FornecedorSpecificationFactory {

    private FornecedorSpecificationFactory() {}

    public static Specification<Fornecedor> build(FornecedorGetAllInputDto inputDto) {
        Specification<Fornecedor> spec = Specification.where(null);

        if (inputDto == null) return spec;

        if (inputDto.getCodigo() != null) {
            spec = spec.and(FornecedorSpecification.filtroCodigo(inputDto.getCodigo()));
        }

        if (inputDto.getRazaoSocial() != null) {
            spec = spec.and(FornecedorSpecification.filtroRazaoSocial(inputDto.getRazaoSocial()));
        }

        if (inputDto.getCgcCpf() != null) {
            spec = spec.and(FornecedorSpecification.filtroCgcCpf(inputDto.getCgcCpf()));
        }

        if (inputDto.getAtivoGfd() != null) {
            spec = spec.and(FornecedorSpecification.filtroAtivoGfd(inputDto.getAtivoGfd()));
        }

        return spec;
    }
}
