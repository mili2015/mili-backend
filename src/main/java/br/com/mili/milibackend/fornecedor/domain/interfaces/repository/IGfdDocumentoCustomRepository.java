package br.com.mili.milibackend.fornecedor.domain.interfaces.repository;

import br.com.mili.milibackend.fornecedor.domain.entity.GfdDocumento;
import br.com.mili.milibackend.fornecedor.infra.dto.GfdDocumentoResumoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface IGfdDocumentoCustomRepository {
    Page<GfdDocumentoResumoDto> getAll(Specification<GfdDocumento> spec, Pageable pageable);
}
