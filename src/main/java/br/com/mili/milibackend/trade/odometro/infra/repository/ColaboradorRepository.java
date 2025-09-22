package br.com.mili.milibackend.trade.odometro.infra.repository;

import br.com.mili.milibackend.trade.odometro.domain.entity.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ColaboradorRepository extends JpaRepository<Colaborador, Integer>, JpaSpecificationExecutor<Colaborador> {
}
