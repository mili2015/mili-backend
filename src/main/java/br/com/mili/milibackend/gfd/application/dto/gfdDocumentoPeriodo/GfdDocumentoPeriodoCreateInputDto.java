package br.com.mili.milibackend.gfd.application.dto.gfdDocumentoPeriodo;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GfdDocumentoPeriodoCreateInputDto {
    private GfdDocumentoPeriodoDto gfdDocumentoPeriodo;
    private GfdDocumentoDto gfdDocumentoDto;
    private TipoDocumentoDto tipoDocumento;
    private boolean isUpdate = false;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private LocalDate periodo;
        private LocalDate dataEmissao;
        private String classificacao;
        private Integer diasValidade;
        private LocalDate dataValidade;
        private Integer idTipoDocumento;
        private Integer idDocumento;
        private boolean isUpdate = false;

        public Builder update() {
            this.isUpdate = true;
            return this;
        }


        public Builder periodo(LocalDate periodo) {
            this.periodo = periodo;
            return this;
        }

        public Builder documento(Integer idDocumento, LocalDate dataEmissao, LocalDate dataValidade) {
            this.idDocumento = idDocumento;
            this.dataEmissao = dataEmissao;
            this.dataValidade = dataValidade;
            return this;
        }

        public Builder tipoDocumento(Integer id ,String classificacao, Integer diasValidade) {
            this.idTipoDocumento = id;
            this.classificacao = classificacao;
            this.diasValidade = diasValidade;
            return this;
        }

        public GfdDocumentoPeriodoCreateInputDto build() {
            return new GfdDocumentoPeriodoCreateInputDto(
                    new GfdDocumentoPeriodoDto(periodo),
                    new GfdDocumentoDto(idDocumento, dataEmissao, dataValidade),
                    new TipoDocumentoDto(idTipoDocumento, classificacao, diasValidade),
                    isUpdate
            );
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TipoDocumentoDto {
        private Integer id;
        private String classificacao;
        private Integer diasValidade;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GfdDocumentoDto {
        private Integer id;
        private LocalDate dataEmissao;
        private LocalDate dataValidade;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GfdDocumentoPeriodoDto {
        private LocalDate periodo;
    }
}
