package br.com.mili.milibackend.gfd.infra.repository;

import br.com.mili.milibackend.gfd.domain.entity.GfdTipoFornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface GfdTipoFornecedorRepository extends JpaRepository<GfdTipoFornecedor, Integer>, JpaSpecificationExecutor<GfdTipoFornecedor> {
}
