package br.com.mili.milibackend.fornecedor.domain.specification;

import br.com.mili.milibackend.fornecedor.domain.entity.GfdTipoDocumento;
import br.com.mili.milibackend.fornecedor.domain.entity.GfdTipoDocumentoTipoEnum;
import org.springframework.data.jpa.domain.Specification;

public class GfdTipoDocumentoSpecification {
    public static Specification<GfdTipoDocumento> filtroTipo(GfdTipoDocumentoTipoEnum tipo) {
        return (root, query, cb) -> {
            if (tipo == null) return null;
            return cb.equal(root.get("tipo"), tipo);
        };
    }

    public static Specification<GfdTipoDocumento> filtroAtivo(Boolean ativo) {
        return (root, query, cb) -> {
            if (ativo == null) return null;
            return cb.equal(root.get("ativo"), ativo);
        };
    }
}
