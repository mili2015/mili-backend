package br.com.mili.milibackend.user.infra.repository;

import br.com.mili.milibackend.user.domain.entity.User;
import br.com.mili.milibackend.user.infra.repository.projection.UserFindUserByUsername;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

@org.springframework.stereotype.Repository
public interface UserRepository extends Repository<User, Integer> {

    @Query(value = """
        SELECT  
            CTUSU_CODIGO AS id,
            CTUSU_NOME AS username,
            CASE 
                WHEN IS_HEX(CTUSU_SENHA) = 'T' THEN TOOLKIT.DECRYPT(CTUSU_SENHA)
                ELSE UPPER(CTUSU_SENHA)
            END AS password,
            EMAIL AS email,
            IDSETOR AS idSetor,
            IS_HEX(CTUSU_SENHA) AS isHex
        FROM CT_USUARIO
        WHERE upper(CTUSU_NOME) = upper(:username)
          AND NVL(ATIVO, 'S') = 'S'
    """, nativeQuery = true)
    UserFindUserByUsername findByUsername(@Param("username") String username);
}