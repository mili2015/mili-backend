package br.com.mili.milibackend.gfd.infra.repository.gfdFuncionario;


import br.com.mili.milibackend.gfd.domain.entity.GfdFuncionario;
import br.com.mili.milibackend.gfd.domain.interfaces.IGfdFuncionarioCustomRepository;
import br.com.mili.milibackend.gfd.infra.projections.GfdFuncionarioStatusProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface GfdFuncionarioRepository extends JpaRepository<GfdFuncionario, Integer>, JpaSpecificationExecutor<GfdFuncionario>, IGfdFuncionarioCustomRepository {

    String QUERY_GFD_FUNCIONARIO = """
            WITH documento_funcionarios AS (
                        SELECT
                            A.*,
                            C.ID AS CT_FORNECEDOR_DOCUMENTOS_ID,
                            C.STATUS
                        FROM
                            GFD_FORNECEDOR_FUNCIONARIO A
                        CROSS JOIN
                            GFD_TIPO_DOCUMENTO B
                        LEFT JOIN (
                            SELECT *
                            FROM (
                                SELECT C.*,
                                       ROW_NUMBER() OVER (
                                           PARTITION BY CTFOR_CODIGO, ID_TIPO_DOCUMENTO
                                           ORDER BY ID DESC
                                       ) AS RN
                                FROM CT_FORNECEDOR_DOCUMENTOS C
                            )
                            WHERE RN = 1
                        ) C
                            ON C.CTFOR_CODIGO = A.CTFOR_CODIGO
                           AND C.ID_TIPO_DOCUMENTO = B.ID
                           AND C.ID_FUNCIONARIO= A.ID_FUNCIONARIO
            
                         WHERE B.TIPO = CASE
                             WHEN A.TIPO_CONTRATACAO = 'PJ' THEN 'FUNCIONARIO_MEI'
                             ELSE 'FUNCIONARIO_CLT'
                         END
            )""";

    @Query(value =
            QUERY_GFD_FUNCIONARIO + """
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
                        A.OBSERVACAO,
                        A.LIBERADO,
                    
                        -- contadores de status
                        SUM(CASE WHEN STATUS = 'ENVIADO'       THEN 1 ELSE 0 END) AS total_enviado,
                        SUM(CASE WHEN STATUS = 'CONFORME'      THEN 1 ELSE 0 END) AS total_conforme,
                        SUM(CASE WHEN STATUS = 'NAO CONFORME'  THEN 1 ELSE 0 END) AS total_nao_conforme,
                        SUM(CASE WHEN STATUS = 'EM ANALISE'    THEN 1 ELSE 0 END) AS total_em_analise,
                        SUM(CASE WHEN STATUS IS NULL           THEN 1 ELSE 0 END) AS nao_enviado
                    
                    FROM
                        documento_funcionarios A
                            WHERE  (:idFuncionario IS NULL OR A.ID_FUNCIONARIO = :idFuncionario)
                              AND (:nome IS NULL OR LOWER(A.NOME) LIKE LOWER(:nome))
                              AND A.ATIVO = :ativo
                              AND (:funcao IS NULL OR LOWER(A.FUNCAO) LIKE LOWER(:funcao))
                    
                              AND (:tipoContratacao IS NULL OR A.TIPO_CONTRATACAO = :tipoContratacao)
                    
                              AND (:idFornecedor IS NULL OR A.CTFOR_CODIGO = :idFornecedor)
                    
                              AND (
                                (:periodoInicio IS NULL AND :periodoFim IS NULL)
                                OR (:periodoInicio IS NOT NULL AND :periodoFim IS NOT NULL AND A.PERIODO_INICIAL BETWEEN :periodoInicio AND :periodoFim)
                                OR (:periodoInicio IS NOT NULL AND :periodoFim IS NULL AND A.PERIODO_INICIAL >= :periodoInicio)
                                OR (:periodoInicio IS NULL AND :periodoFim IS NOT NULL AND A.PERIODO_INICIAL <= :periodoFim)
                              )
                    
                    GROUP BY
                        A.CTFOR_CODIGO,
                        A.ID_FUNCIONARIO,
                        A.NOME,
                        A.CPF,
                        A.DATA_NASCIMENTO,
                        A.PAIS_NACIONALIDADE,
                        A.FUNCAO,
                        A.TIPO_CONTRATACAO,
                        A.PERIODO_INICIAL,
                        A.PERIODO_FINAL,
                        A.OBSERVACAO,
                        A.ATIVO,
                        A.LIBERADO
                    
                    
                    ORDER BY
                        A.ID_FUNCIONARIO
                    
                    OFFSET :offset ROWS FETCH FIRST :pageSize ROWS ONLY"""
            , nativeQuery = true)
    List<GfdFuncionarioStatusProjection> getAll(
            Integer idFuncionario,
            String nome,
            String funcao,
            String tipoContratacao,
            LocalDate periodoInicio,
            LocalDate periodoFim,
            Integer idFornecedor,
            Integer offset,
            Integer pageSize,
            Integer ativo
    );

    @Query(value =
            QUERY_GFD_FUNCIONARIO + """
                     SELECT COUNT(*) FROM (
                    
                           SELECT
                               A.ID_FUNCIONARIO AS ID
                           FROM
                               documento_funcionarios A
                                   WHERE  (:idFuncionario IS NULL OR A.ID_FUNCIONARIO = :idFuncionario)
                                     AND (:nome IS NULL OR LOWER(A.NOME) LIKE LOWER(:nome))
                                     AND A.ATIVO = :ativo
                                     AND (:funcao IS NULL OR LOWER(A.FUNCAO) LIKE LOWER(:funcao))
                    
                                     AND (:tipoContratacao IS NULL OR A.TIPO_CONTRATACAO = :tipoContratacao)
                    
                                     AND (:idFornecedor IS NULL OR A.CTFOR_CODIGO = :idFornecedor)
                    
                                     AND (
                                       (:periodoInicio IS NULL AND :periodoFim IS NULL)
                                       OR (:periodoInicio IS NOT NULL AND :periodoFim IS NOT NULL AND A.PERIODO_INICIAL BETWEEN :periodoInicio AND :periodoFim)
                                       OR (:periodoInicio IS NOT NULL AND :periodoFim IS NULL AND A.PERIODO_INICIAL >= :periodoInicio)
                                       OR (:periodoInicio IS NULL AND :periodoFim IS NOT NULL AND A.PERIODO_INICIAL <= :periodoFim)
                                     )
                    
                           GROUP BY
                               A.ID_FUNCIONARIO
                    )
                    """
            , nativeQuery = true)
    Integer getAllCount(
            Integer idFuncionario,
            String nome,
            String funcao,
            String tipoContratacao,
            LocalDate periodoInicio,
            LocalDate periodoFim,
            Integer idFornecedor,
            Integer ativo
    );
}
