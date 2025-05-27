package br.com.mili.milibackend.fornecedor.domain.specification;

import br.com.mili.milibackend.fornecedor.domain.entity.Fornecedor;
import org.springframework.data.jpa.domain.Specification;

public class FornecedorSpecification {

    public static Specification<Fornecedor> filtroRazaoSocial(String razaoSocial) {
        return (root, query, cb) -> {
            if (razaoSocial == null) return null;
            return cb.like(cb.lower(root.get("razaoSocial")), "%" + razaoSocial.toLowerCase() + "%");
        };
    }

    public static Specification<Fornecedor> filtroCgcCpf(String cgcCpf) {
        return (root, query, cb) -> {
            if (cgcCpf == null) return null;
            return cb.equal(root.get("cgcCpf"), cgcCpf);
        };
    }

    public static Specification<Fornecedor> filtroCodigo(Integer codigo) {
        return (root, query, cb) -> {
            if(codigo == null) return null;
            return cb.equal(root.get("codigo"), codigo);
        };
    }
}
