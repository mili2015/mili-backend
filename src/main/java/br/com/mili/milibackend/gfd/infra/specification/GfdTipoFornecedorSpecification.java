package br.com.mili.milibackend.gfd.infra.specification;

import br.com.mili.milibackend.gfd.domain.entity.GfdTipoFornecedor;
import org.springframework.data.jpa.domain.Specification;

public class GfdTipoFornecedorSpecification {
    public static Specification<GfdTipoFornecedor> filtroDescricaoContem(String descricao) {
        return (root, query, cb) -> {
            if (descricao == null) return null;
            return cb.like(cb.lower(root.get("descricao")), "%" + descricao.toLowerCase() + "%");
        };
    }

    public static Specification<GfdTipoFornecedor> filtroId(Integer id) {
        return (root, query, cb) -> {
            if (id == null) return null;
            return cb.equal(root.get("id"), id);
        };
    }
}
