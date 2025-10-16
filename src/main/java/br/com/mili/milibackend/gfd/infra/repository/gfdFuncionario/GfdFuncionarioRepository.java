package br.com.mili.milibackend.gfd.infra.repository.gfdFuncionario;

import br.com.mili.milibackend.gfd.domain.entity.GfdFuncionario;
import br.com.mili.milibackend.gfd.domain.interfaces.IGfdFuncionarioCustomRepository;
import br.com.mili.milibackend.gfd.infra.projections.GfdFuncionarioDocumentsProjection;
import br.com.mili.milibackend.gfd.infra.projections.GfdFuncionarioStatusProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface GfdFuncionarioRepository extends JpaRepository<GfdFuncionario, Integer>, JpaSpecificationExecutor<GfdFuncionario>, IGfdFuncionarioCustomRepository {

    String QUERY_GFD_FUNCIONARIO = """
                 SELECT
                     A.ATIVO as ATIVO,
                     A.CTFOR_CODIGO,
                     A.ID_FUNCIONARIO AS ID,
                     A.NOME,
                     A.CPF,
                     A.DATA_NASCIMENTO,
                     A.PAIS_NACIONALIDADE,
                     A.FUNCAO,
                     A.TIPO_CONTRATACAO,
                     A.PERIODO_INICIAL,
                     A.PERIODO_FINAL,
                     A.ID_TIPO_CONTRATACAO,
                     A.OBSERVACAO,
                     A.DESLIGADO,
                     A.LIBERADO,
            
                     SUM(CASE WHEN C.STATUS = 'ENVIADO'       THEN 1 ELSE 0 END) AS total_enviado,
                     SUM(CASE WHEN C.STATUS = 'CONFORME'      THEN 1 ELSE 0 END) AS total_conforme,
                     SUM(CASE WHEN C.STATUS = 'NAO CONFORME'  THEN 1 ELSE 0 END) AS total_nao_conforme,
                     SUM(CASE WHEN C.STATUS = 'EM ANALISE'    THEN 1 ELSE 0 END) AS total_em_analise,
                     SUM(CASE WHEN C.STATUS IS NULL AND B.OBRIGATORIEDADE = 1 THEN 1 ELSE 0 END) AS nao_enviado
            
                 FROM GFD_FORNECEDOR_FUNCIONARIO A
                 JOIN GFD_TIPO_CONTRATACAO TC ON TC.ID = A.ID_TIPO_CONTRATACAO
                 JOIN GFD_TIPO_DOCUMENTO B ON B.ID_CATEGORIA_DOC = TC.ID_CATEGORIA_DOC
                 AND B.ATIVO = 1
                 AND (
                          (A.DESLIGADO = 1)
                       OR (A.DESLIGADO <> 1 AND B.CLASSIFICACAO <> 'RESCISAO')
                 )
            
                 LEFT JOIN (
                     SELECT *
                     FROM (
                         SELECT C.*,
                                ROW_NUMBER() OVER (
                                    PARTITION BY C.CTFOR_CODIGO, C.ID_TIPO_DOCUMENTO, C.ID_FUNCIONARIO
                                    ORDER BY C.ID DESC
                                ) AS RN
                         FROM CT_FORNECEDOR_DOCUMENTOS C
                     ) SUB
                     WHERE SUB.RN = 1
                 ) C
                   ON C.CTFOR_CODIGO = A.CTFOR_CODIGO
                  AND C.ID_TIPO_DOCUMENTO = B.ID
                  AND C.ID_FUNCIONARIO = A.ID_FUNCIONARIO
            
                 -- Locais de trabalho
                 LEFT JOIN GFD_LOCAL_TRABALHO LT ON LT.ID_FUNCIONARIO = A.ID_FUNCIONARIO
            
                 WHERE (:idFuncionario IS NULL OR A.ID_FUNCIONARIO = :idFuncionario)
                   AND (:nome IS NULL OR LOWER(A.NOME) LIKE LOWER(:nome))
                   AND A.ATIVO = :ativo
                   AND (:funcao IS NULL OR LOWER(A.FUNCAO) LIKE LOWER(:funcao))
                   AND (:tipoContratacao IS NULL OR A.ID_TIPO_CONTRATACAO = :tipoContratacao)
                   AND (:idFornecedor IS NULL OR A.CTFOR_CODIGO = :idFornecedor)
                   AND (
                         (:periodoInicio IS NULL AND :periodoFim IS NULL)
                      OR (:periodoInicio IS NOT NULL AND :periodoFim IS NOT NULL AND A.PERIODO_INICIAL BETWEEN :periodoInicio AND :periodoFim)
                      OR (:periodoInicio IS NOT NULL AND :periodoFim IS NULL AND A.PERIODO_INICIAL >= :periodoInicio)
                      OR (:periodoInicio IS NULL AND :periodoFim IS NOT NULL AND A.PERIODO_INICIAL <= :periodoFim)
                   )
            
                   -- Filtro Locais de trabalho
                   AND (:idsLocalTrabalho IS NULL OR LT.CTEMP_CODIGO in (:idsLocalTrabalho))
            
                 GROUP BY
                     A.ATIVO,
                     A.CTFOR_CODIGO,
                     A.ID_FUNCIONARIO,
                     A.NOME,
                     A.CPF,
                     A.DATA_NASCIMENTO,
                     A.PAIS_NACIONALIDADE,
                     A.FUNCAO,
                     A.TIPO_CONTRATACAO,
                     A.PERIODO_INICIAL,
                     A.ID_TIPO_CONTRATACAO,
                     A.PERIODO_FINAL,
                     A.LIBERADO,
                     A.OBSERVACAO,
                     A.DESLIGADO
            
                 ORDER BY A.ID_FUNCIONARIO DESC
            """;

    @Query(value = QUERY_GFD_FUNCIONARIO + " OFFSET :offset ROWS FETCH NEXT :pageSize ROWS ONLY", nativeQuery = true)
    List<GfdFuncionarioStatusProjection> getAll(
            Integer idFuncionario,
            String nome,
            String funcao,
            Integer tipoContratacao,
            LocalDate periodoInicio,
            LocalDate periodoFim,
            Integer idFornecedor,
            Integer ativo,
            List<Integer> idsLocalTrabalho,

            //paginação
            Integer offset,
            Integer pageSize
    );

    @Query(value = """
                           SELECT COUNT(*) FROM (
                           """ + QUERY_GFD_FUNCIONARIO + """
                           )
                           """, nativeQuery = true)
    Integer getAllCount(
            Integer idFuncionario,
            String nome,
            String funcao,
            Integer tipoContratacao,
            LocalDate periodoInicio,
            LocalDate periodoFim,
            Integer idFornecedor,
            Integer ativo,
            List<Integer> idsLocalTrabalho
    );


    /*
    * Consulta para verificar se o usuário possui documento pendentes para enviar a justificativa
    * */
    String QUERY_GFD_FUNCIONARIO_DOCUMENTOS = """
                 SELECT
                     SUM(CASE WHEN C.STATUS = 'ENVIADO'       THEN 1 ELSE 0 END) AS total_enviado,
                     SUM(CASE WHEN C.STATUS = 'CONFORME'      THEN 1 ELSE 0 END) AS total_conforme,
                     SUM(CASE WHEN C.STATUS = 'NAO CONFORME'  THEN 1 ELSE 0 END) AS total_nao_conforme,
                     SUM(CASE WHEN C.STATUS = 'EM ANALISE'    THEN 1 ELSE 0 END) AS total_em_analise,
                     SUM(CASE WHEN C.STATUS IS NULL AND B.OBRIGATORIEDADE = 1 THEN 1 ELSE 0 END) AS nao_enviado
                 FROM GFD_FORNECEDOR_FUNCIONARIO A
                 JOIN GFD_TIPO_CONTRATACAO TC ON TC.ID = A.ID_TIPO_CONTRATACAO
                 JOIN GFD_TIPO_DOCUMENTO B ON B.ID_CATEGORIA_DOC = TC.ID_CATEGORIA_DOC AND B.ATIVO = 1
                 AND (
                          (A.DESLIGADO = 1)
                       OR (A.DESLIGADO <> 1 AND B.CLASSIFICACAO <> 'RESCISAO')
                 )
                 LEFT JOIN (
                     SELECT *
                     FROM (
                         SELECT C.*,
                                ROW_NUMBER() OVER (
                                    PARTITION BY C.CTFOR_CODIGO, C.ID_TIPO_DOCUMENTO, C.ID_FUNCIONARIO
                                    ORDER BY C.ID DESC
                                ) AS RN
                         FROM CT_FORNECEDOR_DOCUMENTOS C
                     ) SUB
                     WHERE SUB.RN = 1
                 ) C
                   ON C.CTFOR_CODIGO = A.CTFOR_CODIGO
                  AND C.ID_TIPO_DOCUMENTO = B.ID
                  AND C.ID_FUNCIONARIO = A.ID_FUNCIONARIO
            
                 WHERE (:idFuncionario IS NULL OR A.ID_FUNCIONARIO = :idFuncionario)
                 ORDER BY A.ID_FUNCIONARIO
            """;

    @Query(value = QUERY_GFD_FUNCIONARIO_DOCUMENTOS, nativeQuery = true)
    GfdFuncionarioDocumentsProjection getAllDocuments(
            Integer idFuncionario
    );
}
