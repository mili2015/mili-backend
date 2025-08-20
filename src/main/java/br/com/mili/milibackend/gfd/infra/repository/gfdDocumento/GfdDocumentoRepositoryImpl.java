package br.com.mili.milibackend.gfd.infra.repository.gfdDocumento;

import br.com.mili.milibackend.gfd.domain.entity.GfdDocumento;
import br.com.mili.milibackend.gfd.domain.entity.GfdDocumentoPeriodo;
import br.com.mili.milibackend.gfd.domain.entity.GfdFuncionario;
import br.com.mili.milibackend.gfd.domain.entity.GfdTipoDocumento;
import br.com.mili.milibackend.gfd.domain.interfaces.IGfdDocumentoCustomRepository;
import br.com.mili.milibackend.gfd.infra.dto.GfdDocumentoResumoDto;
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

        // 1 - Consulta para pegar só os IDs da página (com filtro, ordenação e paginação)
        CriteriaQuery<Integer> idQuery = cb.createQuery(Integer.class);
        Root<GfdDocumento> idRoot = idQuery.from(GfdDocumento.class);
        idQuery.select(idRoot.get("id"));

        if (spec != null) {
            Predicate predicate = spec.toPredicate(idRoot, idQuery, cb);
            if (predicate != null) {
                idQuery.where(predicate);
            }
        }

        //order esta fixo por conta que coloquei o idroot apenas para pegar o id
        idQuery.orderBy(cb.desc(idRoot.get("id")));

        TypedQuery<Integer> typedIdQuery = em.createQuery(idQuery);
        typedIdQuery.setFirstResult((int) pageable.getOffset());
        typedIdQuery.setMaxResults(pageable.getPageSize());
        List<Integer> ids = typedIdQuery.getResultList();

        if (ids.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, 0);
        }

        // 2 - Consulta para buscar os dados completos dos documentos pelos IDs
        CriteriaQuery<GfdDocumentoResumoDto> dtoQuery = cb.createQuery(GfdDocumentoResumoDto.class);
        Root<GfdDocumento> root = dtoQuery.from(GfdDocumento.class);
        Join<GfdDocumento, GfdTipoDocumento> tipoDocumentoJoin = root.join("gfdTipoDocumento", JoinType.LEFT);
        Join<GfdDocumento, GfdFuncionario> funcionarioJoin = root.join("gfdFuncionario", JoinType.LEFT);

        dtoQuery.select(cb.construct(
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
                cb.construct(GfdDocumentoResumoDto.GfdTipoDocumentoDto.class,
                        tipoDocumentoJoin.get("id"),
                        tipoDocumentoJoin.get("nome"),
                        tipoDocumentoJoin.get("diasValidade")),
                cb.construct(GfdDocumentoResumoDto.FuncionarioDto.class,
                        funcionarioJoin.get("id"),
                        funcionarioJoin.get("nome"),
                        funcionarioJoin.get("cpf")
                )
        ));

        dtoQuery.where(root.get("id").in(ids));
        dtoQuery.distinct(true);
        dtoQuery.orderBy(cb.desc(root.get("id")));

        List<GfdDocumentoResumoDto> results = em.createQuery(dtoQuery).getResultList();

        // 3 - conta total para a página (mesma lógica que já tinha)
        long total = getTotalCount(spec, cb);

        return new PageImpl<>(results, pageable, total);
    }

    private long getTotalCount(Specification<GfdDocumento> spec, CriteriaBuilder cb) {
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<GfdDocumento> countRoot = countQuery.from(GfdDocumento.class);

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