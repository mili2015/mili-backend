package br.com.mili.milibackend.gfd.infra.repository.gfdDocumento;

import br.com.mili.milibackend.gfd.domain.entity.GfdDocumentoHistorico;
import br.com.mili.milibackend.gfd.domain.interfaces.IGfdDocumentoHistoricoCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GfdDocumentoHistoricoRepository extends JpaRepository<GfdDocumentoHistorico, Integer>, JpaSpecificationExecutor<GfdDocumentoHistorico>, IGfdDocumentoHistoricoCustomRepository {
    @EntityGraph(attributePaths = {"usuario"})
    Page<GfdDocumentoHistorico> findAll(@Nullable Specification<GfdDocumentoHistorico> spec, Pageable pageable);

    @Query("SELECT g FROM GfdDocumentoHistorico g WHERE g.documento.id = :id")
    List<GfdDocumentoHistorico> getByGfdDocumento_Id(Integer id);
}
