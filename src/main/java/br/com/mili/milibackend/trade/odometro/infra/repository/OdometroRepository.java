package br.com.mili.milibackend.trade.odometro.infra.repository;

import br.com.mili.milibackend.trade.odometro.domain.entity.Odometro;
import br.com.mili.milibackend.trade.odometro.domain.repository.OdometroCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OdometroRepository extends JpaRepository<Odometro, Integer>, JpaSpecificationExecutor<Odometro>, OdometroCustomRepository {

}
