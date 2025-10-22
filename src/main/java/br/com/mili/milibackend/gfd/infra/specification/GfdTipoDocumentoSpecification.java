package br.com.mili.milibackend.gfd.infra.specification;

import br.com.mili.milibackend.gfd.domain.entity.GfdTipoDocumento;
import br.com.mili.milibackend.gfd.domain.entity.GfdTipoDocumentoTipoClassificacaoEnum;
import br.com.mili.milibackend.gfd.domain.entity.GfdTipoDocumentoTipoEnum;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class GfdTipoDocumentoSpecification {

    public static Specification<GfdTipoDocumento> filtroId(Integer id) {
        return (root, query, cb) -> {
            if (id == null) return null;
            return cb.equal(root.get("id"), id);
        };
    }

    public static Specification<GfdTipoDocumento> filtroCategoriaId(Integer id) {
        return (root, query, cb) -> {
            if (id == null) return null;
            var joinDocumentoCategoria = root.join("categoriaDocumento", JoinType.LEFT);

            return cb.equal(joinDocumentoCategoria.get("id"), id);
        };
    }

    public static Specification<GfdTipoDocumento> filtroNome(String nome) {
        return (root, query, cb) -> {
            if (nome == null) return null;
            return cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
        };
    }

    public static Specification<GfdTipoDocumento> filtroAtivo(Boolean ativo) {
        return (root, query, cb) -> {
            if (ativo == null) return null;
            return cb.equal(root.get("ativo"), ativo);
        };
    }

    public static Specification<GfdTipoDocumento> filtroClassificacao(String classificao) {
        return (root, query, cb) -> {
            if (classificao == null) return null;

            return classificao.startsWith("!")
                    ? cb.notEqual(root.get("classificacao"), classificao.substring(1))
                    : cb.equal(root.get("classificacao"), classificao);
        };
    }

    public static Specification<GfdTipoDocumento> filtroSetor(String setor) {
        return (root, query, cb) -> {
            if (setor == null) return null;

            return cb.equal(root.get("setor"), setor);
        };
    }
}
