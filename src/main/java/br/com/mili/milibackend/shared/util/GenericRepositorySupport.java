package br.com.mili.milibackend.shared.util;


import br.com.mili.milibackend.shared.page.Filtro;
import br.com.mili.milibackend.shared.page.pagination.MyPageable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public abstract class GenericRepositorySupport<TEntity> {

    @jakarta.persistence.PersistenceContext
    protected EntityManager em;

    private final Class<TEntity> entityClass;

    protected GenericRepositorySupport(Class<TEntity> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * Conta o total de registros que satisfazem o Specification.
     */
    protected long getTotal(Specification<TEntity> spec, CriteriaBuilder cb) {
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<TEntity> root = countQuery.from(entityClass);
        countQuery.select(cb.count(root));

        if (spec != null) {
            Predicate predicate = spec.toPredicate(root, countQuery, cb);
            if (predicate != null) countQuery.where(predicate);
        }

        return em.createQuery(countQuery).getSingleResult();
    }

    /**
     * Aplica paginação em uma query com base no Filtro.
     */
    protected Pageable aplicarPaginacao(Filtro filtro, TypedQuery<?> query) {
        Pageable pageable;

        if (!filtro.getAllRows()) {
            MyPageable mp = filtro.getPageable();
            query.setFirstResult(filtro.getPageable().getOffset());
            query.setMaxResults(filtro.getPageable().getSize());

            pageable = PageRequest.of(mp.getPage() - 1, mp.getSize());
        } else {
            pageable = Pageable.unpaged();
        }

        return pageable;
    }
}