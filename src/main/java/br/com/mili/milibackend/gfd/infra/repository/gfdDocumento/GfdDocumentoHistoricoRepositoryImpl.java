package br.com.mili.milibackend.gfd.infra.repository.gfdDocumento;

import br.com.mili.milibackend.gfd.domain.entity.GfdDocumentoHistorico;
import br.com.mili.milibackend.gfd.domain.interfaces.IGfdDocumentoHistoricoCustomRepository;
import br.com.mili.milibackend.gfd.infra.dto.GfdDocumentoHistoricoResumoDto;
import br.com.mili.milibackend.shared.util.GenericRepositorySupport;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GfdDocumentoHistoricoRepositoryImpl extends GenericRepositorySupport<GfdDocumentoHistorico> implements IGfdDocumentoHistoricoCustomRepository {

    public GfdDocumentoHistoricoRepositoryImpl() {
        super(GfdDocumentoHistorico.class);
    }

    @Override
    public Page<GfdDocumentoHistoricoResumoDto> getAll(Specification<GfdDocumentoHistorico> spec, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        List<Integer> ids = obterIds(spec, pageable, cb);
        if (ids.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, 0);
        }

        List<GfdDocumentoHistoricoResumoDto> results = obterDtos(cb, ids);
        long total = getTotal(spec, cb);
        return new PageImpl<>(results, pageable, total);
    }

    private List<GfdDocumentoHistoricoResumoDto> obterDtos(CriteriaBuilder cb, List<Integer> ids) {
        CriteriaQuery<GfdDocumentoHistoricoResumoDto> dtoQuery = cb.createQuery(GfdDocumentoHistoricoResumoDto.class);
        Root<GfdDocumentoHistorico> root = dtoQuery.from(GfdDocumentoHistorico.class);
        Join<Object, Object> userJoin = root.join("usuario", JoinType.LEFT);

        dtoQuery.select(cb.construct(
                GfdDocumentoHistoricoResumoDto.class,
                root.get("id"),
                root.get("funcionarioId"),
                root.get("documentoId"),
                root.get("ctforCodigo"),
                cb.construct(GfdDocumentoHistoricoResumoDto.UserDto.class,
                        userJoin.get("id"),
                        userJoin.get("username")
                ),
                root.get("data"),
                root.get("status")
        ));

        dtoQuery.where(root.get("id").in(ids));
        dtoQuery.distinct(true);
        dtoQuery.orderBy(cb.desc(root.get("id")));

        return em.createQuery(dtoQuery).getResultList();
    }

    private List<Integer> obterIds(Specification<GfdDocumentoHistorico> spec, Pageable pageable, CriteriaBuilder cb) {
        CriteriaQuery<Integer> idQuery = cb.createQuery(Integer.class);
        Root<GfdDocumentoHistorico> idRoot = idQuery.from(GfdDocumentoHistorico.class);
        idQuery.select(idRoot.get("id"));

        if (spec != null) {
            Predicate predicate = spec.toPredicate(idRoot, idQuery, cb);
            if (predicate != null) {
                idQuery.where(predicate);
            }
        }

        idQuery.orderBy(cb.desc(idRoot.get("id")));

        TypedQuery<Integer> typedIdQuery = em.createQuery(idQuery);
        typedIdQuery.setFirstResult((int) pageable.getOffset());
        typedIdQuery.setMaxResults(pageable.getPageSize());

        return typedIdQuery.getResultList();
    }
}

