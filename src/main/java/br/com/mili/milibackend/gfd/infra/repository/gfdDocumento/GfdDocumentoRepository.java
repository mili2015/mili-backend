package br.com.mili.milibackend.gfd.infra.repository.gfdDocumento;


import br.com.mili.milibackend.gfd.domain.entity.GfdDocumento;
import br.com.mili.milibackend.gfd.domain.interfaces.IGfdDocumentoCustomRepository;
import br.com.mili.milibackend.gfd.infra.projections.GfdDocumentCountProjection;
import br.com.mili.milibackend.gfd.infra.projections.GfdFuncionarioDocumentsProjection;
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


    String QUERY_GET_ALL_COUNT_DOCUMENTOS = """
            SELECT
                    COALESCE(SUM(CASE WHEN C.STATUS = 'ENVIADO' THEN 1 ELSE 0 END), 0) AS total_enviado,
                    COALESCE(SUM(CASE WHEN C.STATUS = 'CONFORME' THEN 1 ELSE 0 END), 0) AS total_conforme,
                    COALESCE(SUM(CASE WHEN C.STATUS = 'NAO CONFORME' THEN 1 ELSE 0 END), 0) AS total_nao_conforme,
                    COALESCE(SUM(CASE WHEN C.STATUS = 'EM ANALISE' THEN 1 ELSE 0 END), 0) AS total_em_analise,
                    COALESCE(SUM(CASE WHEN C.STATUS IS NULL AND B.OBRIGATORIEDADE = 1 THEN 1 ELSE 0 END), 0) AS nao_enviado
                 FROM CT_FORNECEDOR A
                 JOIN GFD_TIPO_FORNECEDOR TCF ON TCF.ID = A.ID_TIPO_FORNECEDOR
                 JOIN GFD_TIPO_DOCUMENTO B ON B.ID_CATEGORIA_DOC = TCF.ID_CATEGORIA_DOC AND B.ATIVO = 1
                 LEFT JOIN (
                     SELECT *
                     FROM (
                         SELECT C.*,
                                ROW_NUMBER() OVER (
                                    PARTITION BY C.CTFOR_CODIGO, C.ID_TIPO_DOCUMENTO
                                    ORDER BY C.ID DESC
                                ) AS RN
                         FROM CT_FORNECEDOR_DOCUMENTOS C

                         --PERIODO s
                         JOIN GFD_DOCUMENTO_PERIODO GDP
                            ON C.ID = GDP.ID_DOCUMENTO

                         WHERE (:periodo IS null
                            OR :periodo between GDP.PERIODO_INICIAL and GDP.PERIODO_FINAL)
                     ) SUB
                     WHERE SUB.RN = 1
                 ) C
                    ON C.ID_TIPO_DOCUMENTO = B.ID
                    AND C.CTFOR_CODIGO     = A.CTFOR_CODIGO

                 WHERE  A.CTFOR_CODIGO = :ctforCodigo
            """;
    @Query(value = QUERY_GET_ALL_COUNT_DOCUMENTOS, nativeQuery = true)
    GfdDocumentCountProjection getAllCount(
            Integer ctforCodigo,
            LocalDate periodo
    );

}
