package br.com.mili.milibackend.gfd.application.dto.gfdDocumento;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class GfdDocumentoUpdateInputDto {
    private GfdDocumentoPeriodoDto gfdDocumentoPeriodo;
    private Integer codUsuario;

    @NotNull
    @Valid
    private GfdDocumentoDto gfdDocumentoDto;

    public static Builder builder() {
        return new Builder();
    }

    @NoArgsConstructor
    public static class Builder {
        private LocalDate periodo;
        private Integer codUsuario;
        private Integer idDocumento;
        private LocalDate dataEmissao;
        private LocalDate dataValidade;
        private String status;
        private String observacao;

        public Builder periodo(LocalDate periodo) {
            this.periodo = periodo;
            return this;
        }

        public Builder codUsuario(Integer codUsuario) {
            this.codUsuario = codUsuario;
            return this;
        }

        public Builder documento(Integer id,
                                 LocalDate dataEmissao,
                                 LocalDate dataValidade,
                                 String status,
                                 String observacao) {
            this.idDocumento = id;
            this.dataEmissao = dataEmissao;
            this.dataValidade = dataValidade;
            this.status = status;
            this.observacao = observacao;
            return this;
        }

        public GfdDocumentoUpdateInputDto build() {
            return new GfdDocumentoUpdateInputDto(
                    new GfdDocumentoPeriodoDto(periodo),
                    codUsuario,
                    new GfdDocumentoDto(idDocumento, dataEmissao, dataValidade, status, observacao)
            );
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class GfdDocumentoDto {
        @NotNull
        private Integer id;

        @NotNull
        private LocalDate dataEmissao;

        private LocalDate dataValidade;

        @NotNull
        private String status;

        private String observacao;
    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class GfdDocumentoPeriodoDto {
        private LocalDate periodo;
    }
}
