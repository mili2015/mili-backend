package br.com.mili.milibackend.shared.infra.security.repository;

import br.com.mili.milibackend.shared.infra.security.repository.dummy.AuthDummy;
import br.com.mili.milibackend.shared.infra.security.repository.projections.SystemAuthProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthRepository extends JpaRepository<AuthDummy, Long> {

    @Query(value = """
        SELECT DISTINCT
            CTSIS_CODIGO AS systemId,
            CTOAC_DESCRICAO AS authority
        FROM (                         
            SELECT
                C.CTSIS_CODIGO,
                C.CTOAC_DESCRICAO
            FROM
                CT_USUARIO A,
                CT_ACESSO_USUARIO B,
                CT_OPCAO_ACESSO C
            WHERE
                A.CTUSU_CODIGO = :userId
                AND B.CTSIS_CODIGO IN (39, 38, 53, 65, 81)
                AND A.CTUSU_CODIGO = B.CTUSU_CODIGO
                AND B.CTSIS_CODIGO = C.CTSIS_CODIGO
                AND B.CTOAC_CODIGO = C.CTOAC_CODIGO
         
            UNION ALL                     
         
            SELECT
                C.CTSIS_CODIGO,
                C.CTOAC_DESCRICAO
            FROM
                CT_USUARIO A,
                CT_ACESSO_USUARIO B,
                CT_OPCAO_ACESSO C
            WHERE
                A.CTUSU_CODIGO IN (
                    SELECT DISTINCT
                        A.CTUSU_CODIGO
                    FROM
                        CT_USUARIO A,
                        CT_GRUPO_PERFIL_USUARIO B,
                        CT_ACESSO_USUARIO C,
                        CT_OPCAO_ACESSO D
                    WHERE
                        B.CTUSU_CODIGO = :userId
                        AND A.CTUSU_CODIGO = B.CODIGO_GRUPO_PERFIL
                        AND A.CTUSU_CODIGO = C.CTUSU_CODIGO
                        AND C.CTSIS_CODIGO = D.CTSIS_CODIGO
                        AND C.CTOAC_CODIGO = D.CTOAC_CODIGO
                        AND C.CTSIS_CODIGO IN (39, 38, 53, 65, 81)
                )
                AND B.CTSIS_CODIGO IN (39, 38, 53, 65, 81)
                AND A.CTUSU_CODIGO = B.CTUSU_CODIGO
                AND B.CTSIS_CODIGO = C.CTSIS_CODIGO
                AND B.CTOAC_CODIGO = C.CTOAC_CODIGO
        )
        ORDER BY 1, 2
        """, nativeQuery = true)
    List<SystemAuthProjection> findUserAuthorities(@Param("userId") Integer userId);
}
