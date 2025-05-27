package br.com.mili.milibackend.fornecedor.domain.interfaces.repository;

import br.com.mili.milibackend.fornecedor.domain.entity.Fornecedor;
import br.com.mili.milibackend.fornecedor.infra.dto.FornecedorResumoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface IFornecedorCustomRepository {
    Page<FornecedorResumoDto> getAll(Specification<Fornecedor> spec, Pageable pageable);
}
