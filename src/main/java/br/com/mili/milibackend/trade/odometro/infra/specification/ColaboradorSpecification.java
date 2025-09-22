package br.com.mili.milibackend.trade.odometro.infra.specification;

import br.com.mili.milibackend.trade.odometro.domain.entity.Colaborador;
import org.springframework.data.jpa.domain.Specification;

public class ColaboradorSpecification {

    public static Specification<Colaborador> withId(Integer id) {
        return (root, query, cb) -> id == null ? null : cb.equal(root.get("id"), id);
    }

    public static Specification<Colaborador> withNomeCompletoLike(String filtroNome) {
        if (filtroNome == null || filtroNome.isBlank()) return null;
        String like = "%" + filtroNome.trim().toLowerCase() + "%";
        return (root, query, cb) -> {
            // concat(lower(nome), ' ' + coalesce(lower(sobrenome), ''))
            var coalesceSobrenome = cb.<String>coalesce();
            coalesceSobrenome.value(cb.lower(root.get("sobrenome")));
            coalesceSobrenome.value("");

            var espacoMaisSobrenome = cb.concat(cb.literal(" "), coalesceSobrenome);
            var nomeCompleto = cb.concat(cb.lower(root.get("nome")), espacoMaisSobrenome);
            return cb.like(nomeCompleto, like);
        };
    }

    public static Specification<Colaborador> withEmailLike(String filtroEmail) {
        if (filtroEmail == null || filtroEmail.isBlank()) return null;
        String like = "%" + filtroEmail.trim().toLowerCase() + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get("email")), like);
    }

    public static Specification<Colaborador> withCpf(String cpf) {
        return (root, query, cb) -> cpf == null || cpf.isBlank() ? null : cb.equal(root.get("cpf"), cpf);
    }

    public static Specification<Colaborador> withAtivo(String ativo) {
        return (root, query, cb) -> ativo == null || ativo.isBlank() ? null : cb.equal(root.get("ativo"), ativo);
    }

    public static Specification<Colaborador> withIdColaboradorSuperior(Integer idSuperior) {
        return (root, query, cb) -> idSuperior == null ? null : cb.equal(root.get("idColaboradorSuperior"), idSuperior);
    }
}
