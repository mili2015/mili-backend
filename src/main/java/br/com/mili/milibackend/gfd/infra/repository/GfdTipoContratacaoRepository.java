package br.com.mili.milibackend.gfd.infra.repository;

import br.com.mili.milibackend.gfd.domain.entity.GfdTipoContratacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface GfdTipoContratacaoRepository extends JpaRepository<GfdTipoContratacao, Integer>, JpaSpecificationExecutor<GfdTipoContratacao> {
}
