package br.com.mili.milibackend.fornecedor.infra.repository.gfdDocumento;

import br.com.mili.milibackend.fornecedor.domain.entity.GfdDocumento;
import br.com.mili.milibackend.fornecedor.domain.interfaces.repository.IGfdDocumentoCustomRepository;
import br.com.mili.milibackend.fornecedor.infra.dto.GfdDocumentoResumoDto;
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
public class GfdDocumentoRepositoryImpl implements IGfdDocumentoCustomRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<GfdDocumentoResumoDto> getAll(Specification<GfdDocumento> spec, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<GfdDocumentoResumoDto> cq = cb.createQuery(GfdDocumentoResumoDto.class);
        Root<GfdDocumento> root = cq.from(GfdDocumento.class);

        //adição de joins
        Join<?, ?> tipoDocumentoJoin = root.join("gfdTipoDocumento", JoinType.LEFT);


        // select só com os campos que interessam pro DTO
        cq.select(cb.construct(
                GfdDocumentoResumoDto.class,
                root.get("id"),
                root.get("ctforCodigo"),
                root.get("tipoDocumento"),
                root.get("nomeArquivo"),
                root.get("nomeArquivoPath"),
                root.get("tamanhoArquivo"),
                root.get("dataCadastro"),
                root.get("dataEmissao"),
                root.get("dataValidade"),
                root.get("tipoArquivo"),
                root.get("observacao"),
                root.get("status"),
                cb.construct(GfdDocumentoResumoDto.GfdTipoDocumentoDto.class, tipoDocumentoJoin.get("id"), tipoDocumentoJoin.get("nome"), tipoDocumentoJoin.get("diasValidade"))

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
        TypedQuery<GfdDocumentoResumoDto> query = em.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        // conta total para a Page
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<GfdDocumento> countRoot = countQuery.from(GfdDocumento.class);
        countQuery.select(cb.count(countRoot));
        if (spec != null) {
            Predicate countPredicate = spec.toPredicate(countRoot, countQuery, cb);
            if (countPredicate != null) countQuery.where(countPredicate);
        }

        long total = em.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(query.getResultList(), pageable, total);
    }
}
