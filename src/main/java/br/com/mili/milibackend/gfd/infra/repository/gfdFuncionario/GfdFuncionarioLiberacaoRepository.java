package br.com.mili.milibackend.gfd.infra.repository.gfdFuncionario;

import br.com.mili.milibackend.gfd.domain.entity.GfdFuncionarioLiberacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GfdFuncionarioLiberacaoRepository extends JpaRepository<GfdFuncionarioLiberacao, Integer>, JpaSpecificationExecutor<GfdFuncionarioLiberacao> {
}
