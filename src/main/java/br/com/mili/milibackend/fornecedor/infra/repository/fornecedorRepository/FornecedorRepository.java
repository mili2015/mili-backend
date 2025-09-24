package br.com.mili.milibackend.fornecedor.infra.repository.fornecedorRepository;


import br.com.mili.milibackend.fornecedor.domain.entity.Fornecedor;
import br.com.mili.milibackend.fornecedor.domain.interfaces.repository.IFornecedorCustomRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Integer>, JpaSpecificationExecutor<Fornecedor>, IFornecedorCustomRepository {

    @Override
    @EntityGraph(attributePaths = "tipoFornecedor")
    Optional<Fornecedor> findById(Integer id);

    @EntityGraph(attributePaths = "tipoFornecedor")
    @Query("""
                SELECT f
                FROM GfdFornecedorUsuario gfu
                JOIN gfu.fornecedor f
                WHERE gfu.id.codUsuario = :codUsuario
            """)
    Optional<Fornecedor> findByCodUsuario(@Param("codUsuario") Integer codUsuario);

    @Query("""
        SELECT CASE WHEN COUNT(fu) > 0 THEN true ELSE false END
        FROM GfdFornecedorUsuario fu
        WHERE fu.id.codUsuario = :codUsuario
          AND fu.id.fornecedorId = :fornecedorId
    """)
    boolean existsByUsuarioIdAndFornecedorId(@Param("codUsuario") Integer codUsuario,
                                             @Param("fornecedorId") Integer fornecedorId);
}
