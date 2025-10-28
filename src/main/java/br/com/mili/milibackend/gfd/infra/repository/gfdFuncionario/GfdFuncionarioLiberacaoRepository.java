package br.com.mili.milibackend.gfd.infra.repository.gfdFuncionario;

import br.com.mili.milibackend.gfd.domain.entity.GfdFuncionarioLiberacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;

public interface GfdFuncionarioLiberacaoRepository extends JpaRepository<GfdFuncionarioLiberacao, Integer>, JpaSpecificationExecutor<GfdFuncionarioLiberacao> {
    @EntityGraph(attributePaths = {"usuario"})
    Page<GfdFuncionarioLiberacao> findAll(@Nullable Specification<GfdFuncionarioLiberacao> spec, Pageable pageable);
}
