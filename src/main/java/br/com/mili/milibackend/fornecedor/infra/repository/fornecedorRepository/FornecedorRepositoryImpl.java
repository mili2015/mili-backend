package br.com.mili.milibackend.fornecedor.infra.repository.fornecedorRepository;

import br.com.mili.milibackend.fornecedor.domain.entity.Fornecedor;
import br.com.mili.milibackend.fornecedor.domain.interfaces.repository.IFornecedorCustomRepository;
import br.com.mili.milibackend.fornecedor.infra.dto.FornecedorResumoDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FornecedorRepositoryImpl implements IFornecedorCustomRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<FornecedorResumoDto> getAll(Specification<Fornecedor> spec, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<FornecedorResumoDto> cq = cb.createQuery(FornecedorResumoDto.class);
        Root<Fornecedor> root = cq.from(Fornecedor.class);

        // select só com os campos que interessam pro DTO
        cq.select(cb.construct(
                FornecedorResumoDto.class,
                root.get("codigo"),
                root.get("cgcCpf"),
                root.get("razaoSocial"),
                root.get("nomeFantasia"),
                root.get("celular"),
                root.get("contato"),
                root.get("email"),
                root.get("aceiteLgpd")
        ));

        // aplica a Specification (predicados)
        if (spec != null) {
            Predicate predicate = spec.toPredicate(root, cq, cb);
            if (predicate != null) cq.where(predicate);
        }

        // ordenação
        if (pageable.getSort().isSorted()) {
            List<Order> orders = pageable.getSort().stream()
                    .map(order -> order.isAscending() ? cb.asc(root.get(order.getProperty())) : cb.desc(root.get(order.getProperty())))
                    .toList();
            cq.orderBy(orders);
        }

        // executa com paginação
        TypedQuery<FornecedorResumoDto> query = em.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        // conta total para a Page
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Fornecedor> countRoot = countQuery.from(Fornecedor.class);
        countQuery.select(cb.count(countRoot));
        if (spec != null) {
            Predicate countPredicate = spec.toPredicate(countRoot, countQuery, cb);
            if (countPredicate != null) countQuery.where(countPredicate);
        }

        long total = em.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(query.getResultList(), pageable, total);
    }
}
