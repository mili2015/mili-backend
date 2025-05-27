package br.com.mili.milibackend.fornecedor.domain.specification;

import br.com.mili.milibackend.fornecedor.domain.entity.GfdDocumento;
import br.com.mili.milibackend.fornecedor.domain.entity.GfdDocumentoStatusEnum;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class GfdDocumentoSpecification {

    public static Specification<GfdDocumento> filtroNomeArquivoContem(String nomeArquivo) {
        return (root, query, cb) -> {
            if (nomeArquivo == null) return null;
            return cb.like(cb.lower(root.get("nomeArquivo")), "%" + nomeArquivo.toLowerCase() + "%");
        };
    }

    public static Specification<GfdDocumento> filtroStatus(GfdDocumentoStatusEnum status) {
        return (root, query, cb) -> {
            if(status == null) return null;
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<GfdDocumento> filtroPorFornecedor(Integer ctforCodigo ) {
        return (root, query, cb) -> {
            if(ctforCodigo == null) return null;
            return cb.equal(root.get("ctforCodigo"), ctforCodigo);
        };
    }

    public static Specification<GfdDocumento> filtroPorTipo(Integer tipoId ) {
        return (root, query, cb) -> {
            if(tipoId == null) return null;
            return cb.equal(root.get("gfdTipoDocumento").get("id"), tipoId);
        };
    }

    public static Specification<GfdDocumento> filtroRangeDataCadastro(LocalDate dataInicio, LocalDate dataFim){
        return (root, query, cb) -> {
            Path<LocalDate> dataCadastrooPath = root.get("dataCadastro");

            if (dataInicio == null && dataFim == null) {
                return null;
            }

            if (dataInicio != null && dataFim != null) {
                return cb.between(dataCadastrooPath, dataInicio, dataFim);
            }

            if (dataInicio != null) {
                return cb.greaterThanOrEqualTo(dataCadastrooPath, dataInicio);
            }

            return cb.lessThanOrEqualTo(dataCadastrooPath, dataFim);
        };
    }

    public static Specification<GfdDocumento> filtroRangeDataValidade(LocalDate dataInicio, LocalDate dataFim){
        return (root, query, cb) -> {
            Path<LocalDate> dataCadastrooPath = root.get("dataValidade");

            if (dataInicio == null && dataFim == null) {
                return null;
            }

            if (dataInicio != null && dataFim != null) {
                return cb.between(dataCadastrooPath, dataInicio, dataFim);
            }

            if (dataInicio != null) {
                return cb.greaterThanOrEqualTo(dataCadastrooPath, dataInicio);
            }

            return cb.lessThanOrEqualTo(dataCadastrooPath, dataFim);
        };
    }

    public static Specification<GfdDocumento> filtroRangeDataEmissao(LocalDate dataInicio, LocalDate dataFim){
        return (root, query, cb) -> {
            Path<LocalDate> dataCadastrooPath = root.get("dataEmissao");

            if (dataInicio == null && dataFim == null) {
                return null;
            }

            if (dataInicio != null && dataFim != null) {
                return cb.between(dataCadastrooPath, dataInicio, dataFim);
            }

            if (dataInicio != null) {
                return cb.greaterThanOrEqualTo(dataCadastrooPath, dataInicio);
            }

            return cb.lessThanOrEqualTo(dataCadastrooPath, dataFim);
        };
    }

}
