package br.com.mili.milibackend.gfd.infra.repository.gfdFuncionario;

import br.com.mili.milibackend.gfd.domain.entity.GfdFuncionarioLiberacao;
import br.com.mili.milibackend.gfd.domain.interfaces.IGfdFuncionarioLiberacaoCustomRepository;
import br.com.mili.milibackend.gfd.infra.dto.GfdFuncionarioLiberacaoResumoDto;
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
public class GfdFuncionarioLiberacaoRepositoryImpl extends GenericRepositorySupport<GfdFuncionarioLiberacao> implements IGfdFuncionarioLiberacaoCustomRepository {

    public GfdFuncionarioLiberacaoRepositoryImpl() {
        super(GfdFuncionarioLiberacao.class);
    }

    @Override
    public Page<GfdFuncionarioLiberacaoResumoDto> getAll(Specification<GfdFuncionarioLiberacao> spec, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        List<Integer> ids = obterIds(spec, pageable, cb);
        if (ids.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, 0);
        }

        List<GfdFuncionarioLiberacaoResumoDto> results = obterDtos(cb, ids);
        long total = getTotal(spec, cb);
        return new PageImpl<>(results, pageable, total);
    }

    private List<GfdFuncionarioLiberacaoResumoDto> obterDtos(CriteriaBuilder cb, List<Integer> ids) {
        CriteriaQuery<GfdFuncionarioLiberacaoResumoDto> dtoQuery = cb.createQuery(GfdFuncionarioLiberacaoResumoDto.class);
        Root<GfdFuncionarioLiberacao> root = dtoQuery.from(GfdFuncionarioLiberacao.class);
        Join<Object, Object> userJoin = root.join("usuario", JoinType.LEFT);
        Path<Integer> funcionarioIdPath = root.get("funcionario").get("id");

        dtoQuery.select(cb.construct(
                GfdFuncionarioLiberacaoResumoDto.class,
                root.get("id"),
                funcionarioIdPath,
                cb.construct(GfdFuncionarioLiberacaoResumoDto.UserDto.class,
                        userJoin.get("id"),
                        userJoin.get("username")
                ),
                root.get("data"),
                root.get("statusLiberado"),
                root.get("justificativa")
        ));

        dtoQuery.where(root.get("id").in(ids));
        dtoQuery.distinct(true);
        dtoQuery.orderBy(cb.desc(root.get("id")));

        return em.createQuery(dtoQuery).getResultList();
    }

    private List<Integer> obterIds(Specification<GfdFuncionarioLiberacao> spec, Pageable pageable, CriteriaBuilder cb) {
        CriteriaQuery<Integer> idQuery = cb.createQuery(Integer.class);
        Root<GfdFuncionarioLiberacao> idRoot = idQuery.from(GfdFuncionarioLiberacao.class);
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

