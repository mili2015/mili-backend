package br.com.mili.milibackend.gfd.infra.specification;

import br.com.mili.milibackend.gfd.domain.entity.GfdDocumentoHistorico;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class GfdDocumentoHistoricoSpecification {

    public static Specification<GfdDocumentoHistorico> filtroFuncionarioId(Integer id) {
        return (root, query, cb) -> {
            if (id == null) return null;
            return cb.equal(root.get("funcionario").get("id"), id);
        };
    }

    public static Specification<GfdDocumentoHistorico> filtroDocumentoId(Integer id) {
        return (root, query, cb) -> {
            if (id == null) return null;
            return cb.equal(root.get("documento").get("id"), id);
        };
    }

    public static Specification<GfdDocumentoHistorico> filtroFornecedorId(Integer id) {
        return (root, query, cb) -> {
            if (id == null) return null;
            return cb.equal(root.get("fornecedor").get("codigo"), id);
        };
    }

    public static Specification<GfdDocumentoHistorico> filtroStatus(String status) {
        return (root, query, cb) -> {
            if (status == null) return null;
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<GfdDocumentoHistorico> filtroPeriodo(LocalDate inicio, LocalDate fim) {
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
