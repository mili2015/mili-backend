package br.com.mili.milibackend.gfd.domain.interfaces;

import br.com.mili.milibackend.gfd.domain.entity.GfdFuncionarioLiberacao;
import br.com.mili.milibackend.gfd.infra.dto.GfdFuncionarioLiberacaoResumoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface IGfdFuncionarioLiberacaoCustomRepository {
    Page<GfdFuncionarioLiberacaoResumoDto> getAll(Specification<GfdFuncionarioLiberacao> spec, Pageable pageable);
}
