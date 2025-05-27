package br.com.mili.milibackend.fornecedor.infra.repository.fornecedorRepository;


import br.com.mili.milibackend.fornecedor.domain.entity.Fornecedor;
import br.com.mili.milibackend.fornecedor.domain.interfaces.repository.IFornecedorCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Integer>, JpaSpecificationExecutor<Fornecedor>, IFornecedorCustomRepository {

    @Query("SELECT f FROM Fornecedor f LEFT JOIN FETCH f.documentos d LEFT JOIN FETCH d.gfdTipoDocumento WHERE f.codUsuario = :codUsuario")
    Optional<Fornecedor> findByCodUsuario(Integer codUsuario);
}
