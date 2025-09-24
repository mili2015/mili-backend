package br.com.mili.milibackend.trade.odometro.domain.repository;

import br.com.mili.milibackend.trade.odometro.domain.entity.Odometro;
import br.com.mili.milibackend.trade.odometro.infra.repository.dto.OdometroResumoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface OdometroCustomRepository {
    Page<OdometroResumoDto> getAll(Specification<Odometro> spec, Pageable pageable);
}
