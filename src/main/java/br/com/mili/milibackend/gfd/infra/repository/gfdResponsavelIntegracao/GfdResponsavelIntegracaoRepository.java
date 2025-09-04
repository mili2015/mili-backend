package br.com.mili.milibackend.gfd.infra.repository.gfdResponsavelIntegracao;

import br.com.mili.milibackend.gfd.domain.entity.GfdResponsavelIntegracao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GfdResponsavelIntegracaoRepository extends JpaRepository<GfdResponsavelIntegracao, Integer>,
        JpaSpecificationExecutor<GfdResponsavelIntegracao> {
    List<GfdResponsavelIntegracao> findByCtempCodigo(Integer empresa);
}
