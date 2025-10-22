package br.com.mili.milibackend.gfd.infra.repository.gfdDocumento;


import br.com.mili.milibackend.gfd.domain.entity.GfdDocumento;
import br.com.mili.milibackend.gfd.domain.interfaces.IGfdDocumentoCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface GfdDocumentoRepository extends JpaRepository<GfdDocumento, Integer>, JpaSpecificationExecutor<GfdDocumento>, IGfdDocumentoCustomRepository {

    @Query("""
                SELECT d
                FROM GfdDocumento d
                     LEFT JOIN FETCH d.gfdTipoDocumento gtd
                     LEFT JOIN FETCH d.gfdDocumentoPeriodo gdp
                WHERE d.id IN (
                    SELECT MAX(d2.id)
                    FROM GfdDocumento d2
                         LEFT JOIN d2.gfdDocumentoPeriodo gdp2
                    WHERE d2.ctforCodigo = :codFornecedor
                          AND (:idFuncionario IS NULL OR d2.gfdFuncionario.id = :idFuncionario)
                          AND (:periodo IS NULL OR :periodo BETWEEN gdp2.periodoInicial AND gdp2.periodoFinal)
                    GROUP BY d2.gfdTipoDocumento.id
                )
               AND  (:setor IS NULL OR gtd.setor = :setor)
            """)
    List<GfdDocumento> findLatestDocumentsByPeriodoAndFornecedorOrFuncionario(
            @Param("codFornecedor") Integer codFornecedor,
            @Param("idFuncionario") Integer idFuncionario,
            @Param("periodo") LocalDate periodo,
            @Param("setor") String setor);

}
