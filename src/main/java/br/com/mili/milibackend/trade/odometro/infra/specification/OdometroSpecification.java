package br.com.mili.milibackend.trade.odometro.infra.specification;

import br.com.mili.milibackend.trade.odometro.domain.entity.Odometro;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OdometroSpecification {

    public static Specification<Odometro> withId(Integer id) {
        return (root, query, criteriaBuilder) ->
            id != null ?
            criteriaBuilder.equal(root.get("id"), id) :
            criteriaBuilder.conjunction();
    }

    public static Specification<Odometro> withIdColaborador(Integer idColaborador) {
        return (root, query, criteriaBuilder) -> 
            idColaborador != null ? 
            criteriaBuilder.equal(root.get("idColaborador"), idColaborador) : 
            criteriaBuilder.conjunction();
    }

    public static Specification<Odometro> withDataInicioBetween(LocalDateTime dataInicio, LocalDateTime dataFim) {
        return (root, query, criteriaBuilder) -> {
            if (dataInicio != null && dataFim != null) {
                return criteriaBuilder.between(root.get("dataInicio"), dataInicio, dataFim);
            } else if (dataInicio != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("dataInicio"), dataInicio);
            } else if (dataFim != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("dataInicio"), dataFim);
            }
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Odometro> withTipoVeiculo(String tipoVeiculo) {
        return (root, query, criteriaBuilder) -> 
            tipoVeiculo != null && !tipoVeiculo.isEmpty() ? 
            criteriaBuilder.equal(root.get("tipoVeiculo"), tipoVeiculo) : 
            criteriaBuilder.conjunction();
    }

}
