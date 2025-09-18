package br.com.mili.milibackend.gfd.infra.specification;

import br.com.mili.milibackend.gfd.domain.entity.GfdCategoriaDocumento;
import br.com.mili.milibackend.gfd.domain.entity.GfdTipoContratacao;
import org.springframework.data.jpa.domain.Specification;

public class GfdCategoriaDocumentoSpecification {

    public static Specification<GfdCategoriaDocumento> filtrNomeContem(String nome) {
        return (root, query, cb) -> {
            if (nome == null) return null;
            return cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
        };
    }

    public static Specification<GfdCategoriaDocumento> filtroTipo(String tipo) {
        return (root, query, cb) -> {
            if (tipo == null) return null;
            return cb.equal(root.get("tipo"), tipo);
        };
    }

    public static Specification<GfdCategoriaDocumento> filtroId(Integer id) {
        return (root, query, cb) -> {
            if (id == null) return null;
            return cb.equal(root.get("id"), id);
        };
    }


}
