package br.com.mili.milibackend.trade.odometro.infra.repository;

import br.com.mili.milibackend.trade.odometro.domain.entity.Colaborador;
import br.com.mili.milibackend.trade.odometro.domain.entity.Odometro;
import br.com.mili.milibackend.trade.odometro.domain.repository.OdometroCustomRepository;
import br.com.mili.milibackend.trade.odometro.infra.repository.dto.OdometroResumoDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class OdometroRepositoryImpl implements OdometroCustomRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<OdometroResumoDto> getAll(Specification<Odometro> spec, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        // 1 - Consulta para pegar só os IDs da página (com filtro, ordenação e paginação)
        CriteriaQuery<Integer> idQuery = cb.createQuery(Integer.class);
        Root<Odometro> idRoot = idQuery.from(Odometro.class);
        idQuery.select(idRoot.get("id"));

        if (spec != null) {
            Predicate predicate = spec.toPredicate(idRoot, idQuery, cb);
            if (predicate != null) {
                idQuery.where(predicate);
            }
        }

        //sort
        idQuery.orderBy(
                cb.asc(idRoot.get("idColaborador")),
                cb.asc(idRoot.get("dataInicio"))
        );


        //query
        TypedQuery<Integer> typedIdQuery = em.createQuery(idQuery);
        typedIdQuery.setFirstResult((int) pageable.getOffset());
        typedIdQuery.setMaxResults(pageable.getPageSize());
        List<Integer> ids = typedIdQuery.getResultList();

        if (ids.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, 0);
        }

        // 2 - Consulta para buscar os dados completos dos documentos pelos IDs
        CriteriaQuery<OdometroResumoDto> dtoQuery = cb.createQuery(OdometroResumoDto.class);
        Root<Odometro> root = dtoQuery.from(Odometro.class);
        Join<Odometro, Colaborador> colaborador = root.join("colaborador", JoinType.LEFT);


        // totalkm
        Subquery<BigDecimal> totalKmSubquery = dtoQuery.subquery(BigDecimal.class);
        Root<Odometro> rootTotalKm = totalKmSubquery.from(Odometro.class);

        totalKmSubquery.select(
                cb.coalesce(
                        cb.sum(cb.diff(rootTotalKm.get("kmFim"), rootTotalKm.get("kmInicio")))
                        , BigDecimal.ZERO
                )
        );

        totalKmSubquery.groupBy(rootTotalKm.get("colaborador"));

        totalKmSubquery.where(cb.equal(rootTotalKm.get("colaborador"), colaborador));


        dtoQuery.select(cb.construct(
                OdometroResumoDto.class,
                root.get("id"),
                colaborador.get("id"),
                root.get("dataInicio"),
                root.get("dataFim"),
                root.get("kmInicio"),
                root.get("kmFim"),
                root.get("pathImgInicio"),
                root.get("pathImgFim"),
                root.get("tipoVeiculo"),

                cb.construct(OdometroResumoDto.Colaborador.class,
                        colaborador.get("id"),
                        colaborador.get("idColaboradorSuperior"),
                        colaborador.get("mcdaCodigo"),
                        colaborador.get("ctusuCodigo"),
                        colaborador.get("nome"),
                        colaborador.get("sobrenome")
                ),

                cb.coalesce(
                        cb.diff(root.get("kmFim"), root.get("kmInicio")), // diferença em km
                        BigDecimal.ZERO
                ),

                totalKmSubquery.getSelection()
        ));

        dtoQuery.where(root.get("id").in(ids));
        dtoQuery.distinct(true);

        //sort
        dtoQuery.orderBy(
                cb.asc(colaborador.get("id")),
                cb.asc(root.get("dataInicio"))
        );

        List<OdometroResumoDto> results = em.createQuery(dtoQuery).getResultList();

        // 3 - conta total para a página (mesma lógica que já tinha)
        long total = getTotalCount(spec, cb);

        return new PageImpl<>(results, pageable, total);
    }

    private long getTotalCount(Specification<Odometro> spec, CriteriaBuilder cb) {
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Odometro> countRoot = countQuery.from(Odometro.class);

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