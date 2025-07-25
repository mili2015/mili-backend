package br.com.mili.milibackend.gfd.domain.interfaces;

import br.com.mili.milibackend.gfd.domain.entity.GfdDocumento;
import br.com.mili.milibackend.gfd.infra.dto.GfdDocumentoResumoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface IGfdDocumentoCustomRepository {
    Page<GfdDocumentoResumoDto> getAll(Specification<GfdDocumento> spec, Pageable pageable);
}
