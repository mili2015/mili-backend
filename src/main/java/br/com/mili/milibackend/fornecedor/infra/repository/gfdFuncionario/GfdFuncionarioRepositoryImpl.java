package br.com.mili.milibackend.fornecedor.infra.repository.gfdFuncionario;

import br.com.mili.milibackend.fornecedor.domain.entity.Fornecedor;
import br.com.mili.milibackend.fornecedor.domain.entity.GfdFuncionario;
import br.com.mili.milibackend.fornecedor.domain.interfaces.repository.IGfdFuncionarioCustomRepository;
import br.com.mili.milibackend.fornecedor.infra.dto.GfdFuncionarioResumoDto;
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
public class GfdFuncionarioRepositoryImpl implements IGfdFuncionarioCustomRepository {

    @PersistenceContext
    private EntityManager em;

    public Page<GfdFuncionarioResumoDto> getAll(Specification<GfdFuncionario> spec, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        // 1 - Consulta para pegar só os ‘IDs’ da página (com filtro, ordenação e paginação)
        CriteriaQuery<Integer> idQuery = cb.createQuery(Integer.class);
        Root<GfdFuncionario> idRoot = idQuery.from(GfdFuncionario.class);
        idQuery.select(idRoot.get("id"));

        if (spec != null) {
            Predicate predicate = spec.toPredicate(idRoot, idQuery, cb);
            if (predicate != null) {
                idQuery.where(predicate);
            }
        }

        //order este fixo por conta q o idroot esta apenas para pegar o id
        idQuery.orderBy(cb.desc(idRoot.get("id")));

        TypedQuery<Integer> typedIdQuery = em.createQuery(idQuery);
        typedIdQuery.setFirstResult((int) pageable.getOffset());
        typedIdQuery.setMaxResults(pageable.getPageSize());
        List<Integer> ids = typedIdQuery.getResultList();

        if (ids.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, 0);
        }

        // 2 - Consulta para buscar os dados completos dos documentos pelos IDs
        CriteriaQuery<GfdFuncionarioResumoDto> dtoQuery = cb.createQuery(GfdFuncionarioResumoDto.class);
        Root<GfdFuncionario> root = dtoQuery.from(GfdFuncionario.class);
        Join<GfdFuncionario, Fornecedor> tipoFuncionarioJoin = root.join("fornecedor", JoinType.LEFT);

        dtoQuery.select(cb.construct(
                GfdFuncionarioResumoDto.class,
                root.get("id"),
                root.get("nome"),
                cb.construct(
                        GfdFuncionarioResumoDto.FornecedorResumoDto.class,
                        tipoFuncionarioJoin.get("codigo"),
                        tipoFuncionarioJoin.get("cgcCpf")
                )
        ));


        dtoQuery.where(root.get("id").in(ids));
        dtoQuery.distinct(true);
        dtoQuery.orderBy(cb.desc(root.get("id")));

        List<GfdFuncionarioResumoDto> results = em.createQuery(dtoQuery).getResultList();

        // 3 - conta total para a página (mesma lógica que já tinha)
        long total = getTotalCount(spec, cb);

        return new PageImpl<>(results, pageable, total);
    }

    private long getTotalCount(Specification<GfdFuncionario> spec, CriteriaBuilder cb) {
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<GfdFuncionario> countRoot = countQuery.from(GfdFuncionario.class);

        countQuery.select(cb.countDistinct(countRoot));

        if (spec != null) {
            Predicate countPredicate = spec.toPredicate(countRoot, countQuery, cb);
            if (countPredicate != null) {
                countQuery.where(countPredicate);
            }
        }

        return em.createQuery(countQuery).getSingleResult();
    }
}