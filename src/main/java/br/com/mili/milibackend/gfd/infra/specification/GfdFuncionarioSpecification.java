package br.com.mili.milibackend.gfd.infra.specification;

import br.com.mili.milibackend.fornecedor.domain.entity.Fornecedor;
import br.com.mili.milibackend.gfd.domain.entity.GfdFuncionario;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class GfdFuncionarioSpecification {

    public static Specification<GfdFuncionario> filtroFornecedorCodigo(Integer codigo) {
        return (root, query, cb) -> {
            if (codigo == null) return null;
            Join<GfdFuncionario, Fornecedor> fornecedor = root.join("fornecedor");

            return cb.equal(fornecedor.get("codigo"), codigo);
        };
    }

    public static Specification<GfdFuncionario> filtroAtivo(Integer ativo) {
        return (root, query, cb) -> {
            if (ativo == null) return null;
            return cb.equal(root.get("ativo"), ativo);
        };
    }

}
