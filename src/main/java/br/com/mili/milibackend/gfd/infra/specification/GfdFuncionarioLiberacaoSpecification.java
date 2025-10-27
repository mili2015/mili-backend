package br.com.mili.milibackend.gfd.infra.specification;

import br.com.mili.milibackend.gfd.domain.entity.GfdFuncionarioLiberacao;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class GfdFuncionarioLiberacaoSpecification {

    public static Specification<GfdFuncionarioLiberacao> filtroFuncionarioId(Integer funcionarioId) {
        return (root, query, cb) -> {
            if (funcionarioId == null) return null;
            return cb.equal(root.get("funcionario").get("id"), funcionarioId);
        };
    }

    public static Specification<GfdFuncionarioLiberacao> filtroStatusLiberado(Integer statusLiberado) {
        return (root, query, cb) -> {
            if (statusLiberado == null) return null;
            return cb.equal(root.get("statusLiberado"), statusLiberado);
        };
    }

    public static Specification<GfdFuncionarioLiberacao> filtroPeriodo(LocalDate inicio, LocalDate fim) {
        return (root, query, cb) -> {
            if (inicio == null && fim == null) return null;
            if (inicio != null && fim != null) {
                LocalDateTime start = inicio.atStartOfDay();
                LocalDateTime end = fim.atTime(23, 59, 59);
                return cb.between(root.get("data"), start, end);
            }
            if (inicio != null) {
                LocalDateTime start = inicio.atStartOfDay();
                return cb.greaterThanOrEqualTo(root.get("data"), start);
            }
            LocalDateTime end = fim.atTime(23, 59, 59);
            return cb.lessThanOrEqualTo(root.get("data"), end);
        };
    }
}
