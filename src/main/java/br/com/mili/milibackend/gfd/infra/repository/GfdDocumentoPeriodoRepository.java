package br.com.mili.milibackend.gfd.infra.repository;


import br.com.mili.milibackend.gfd.domain.entity.GfdDocumentoPeriodo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface GfdDocumentoPeriodoRepository extends JpaRepository<GfdDocumentoPeriodo, Integer>, JpaSpecificationExecutor<GfdDocumentoPeriodo> {
    @Modifying
    @Query("DELETE FROM GfdDocumentoPeriodo g WHERE g.gfdDocumento.id = :id")
    void deleteByGfdDocumento_Id(Integer id);

    @Query("SELECT g FROM GfdDocumentoPeriodo g WHERE g.gfdDocumento.id = :id")
    Optional<GfdDocumentoPeriodo> getByGfdDocumento_Id(Integer id);

    @Modifying
    @Query(
            value = """
                    DELETE FROM gfd_documento_periodo
                    WHERE ID IN (
                        SELECT gfdp.ID
                        FROM gfd_documento_periodo gfdp
                        INNER JOIN ct_fornecedor_documentos cfd ON cfd.ID = gfdp.ID_DOCUMENTO
                        INNER JOIN gfd_tipo_documento gtp ON gtp.ID = cfd.ID_TIPO_DOCUMENTO
                        WHERE gtp.ID = :idTipoDocumento
                          AND gfdp.periodo BETWEEN :periodoIncio AND :periodoFim
                    )""",
            nativeQuery = true
    )
    void deleteByPeriodoAndTipoDocumento(LocalDate periodoIncio, LocalDate periodoFim, Integer idTipoDocumento);

    List<GfdDocumentoPeriodo> findByGfdDocumento_IdIn(Collection<Integer> gfdDocumentoIds);
}
