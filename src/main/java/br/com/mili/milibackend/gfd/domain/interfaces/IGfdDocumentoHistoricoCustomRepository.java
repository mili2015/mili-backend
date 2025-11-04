package br.com.mili.milibackend.gfd.domain.interfaces;

import br.com.mili.milibackend.gfd.domain.entity.GfdDocumentoHistorico;
import br.com.mili.milibackend.gfd.infra.dto.GfdDocumentoHistoricoResumoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface IGfdDocumentoHistoricoCustomRepository {
    Page<GfdDocumentoHistoricoResumoDto> getAll(Specification<GfdDocumentoHistorico> spec, Pageable pageable);
}
