package br.com.mili.milibackend.fornecedor.infra.repository;


import br.com.mili.milibackend.fornecedor.domain.entity.GfdTipoDocumento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface GfdTipoDocumentoRepository extends JpaRepository<GfdTipoDocumento, Integer>, JpaSpecificationExecutor<GfdTipoDocumento> {

    @Override
    Page<GfdTipoDocumento> findAll(Specification<GfdTipoDocumento> spec, Pageable pageable);
}
