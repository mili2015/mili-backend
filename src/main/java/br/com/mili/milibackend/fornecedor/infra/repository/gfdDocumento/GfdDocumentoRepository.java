package br.com.mili.milibackend.fornecedor.infra.repository.gfdDocumento;


import br.com.mili.milibackend.fornecedor.domain.entity.GfdDocumento;
import br.com.mili.milibackend.fornecedor.domain.interfaces.repository.IGfdDocumentoCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GfdDocumentoRepository extends JpaRepository<GfdDocumento, Integer>, JpaSpecificationExecutor<GfdDocumento>, IGfdDocumentoCustomRepository {

    @Query(""" 
            SELECT d
              FROM GfdDocumento d
                      LEFT JOIN FETCH d.gfdTipoDocumento
              WHERE d.id IN (
                  SELECT MAX(d2.id)
                  FROM GfdDocumento d2
                  WHERE d2.ctforCodigo = :codFornecedor
                  GROUP BY d2.gfdTipoDocumento.id
              )""")
    List<GfdDocumento> findLatestDocumentsGroupedByTipoAndFornecedorId(Integer codFornecedor);
}
