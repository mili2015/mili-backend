package br.com.mili.milibackend.gfd.infra.repository;


import br.com.mili.milibackend.gfd.domain.entity.GfdTipoDocumento;
import br.com.mili.milibackend.gfd.infra.projections.GfdClassificaoRescisaoProjection;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GfdTipoDocumentoRepository extends JpaRepository<GfdTipoDocumento, Integer>, JpaSpecificationExecutor<GfdTipoDocumento> {

    @Override
    Page<GfdTipoDocumento> findAll(Specification<GfdTipoDocumento> spec, Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE GfdTipoDocumento g SET g.ativo = false WHERE g.id = :id")
    void inactive(@Param("id") Integer id);


    @Query("""
    SELECT
        cd.nome AS nome,
        (CASE WHEN COUNT(CASE WHEN g.classificacao = 'RESCISAO' THEN 1 END) > 0
              THEN true ELSE false
         END) AS possuiRescisao
    FROM GfdTipoDocumento g
    JOIN g.categoriaDocumento cd
    WHERE g.categoriaDocumento.tipo != 'FORNECEDOR'
    GROUP BY cd.nome
""")
    List<GfdClassificaoRescisaoProjection> listCategoriaPossuemRescisao();
}
