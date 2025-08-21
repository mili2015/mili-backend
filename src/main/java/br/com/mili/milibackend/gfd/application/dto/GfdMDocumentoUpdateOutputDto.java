package br.com.mili.milibackend.gfd.application.dto;

import br.com.mili.milibackend.gfd.domain.entity.GfdDocumentoStatusEnum;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class GfdMDocumentoUpdateOutputDto {
    private GfdDocumentoUpdateOutputDto documento;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @EqualsAndHashCode
    public static class GfdDocumentoUpdateOutputDto {
        private Integer id;
        private Integer ctforCodigo;
        private String tipoDocumento;
        private String nomeArquivo;
        private String nomeArquivoPath;
        private Integer tamanhoArquivo;
        private LocalDate dataCadastro;
        private LocalDate dataValidade;
        private String tipoArquivo;
        private LocalDate dataEmissao;
        private String observacao;
        private GfdDocumentoStatusEnum status;
        private GfdTipoDocumentoDto gfdTipoDocumento;
        private GfdDocumentoPeriodoDto gfdDocumentoPeriodo;

        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @Setter
        public static class GfdTipoDocumentoDto {
            private Integer id;
            private String nome;
            private Integer diasValidade;
        }

        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @Setter
        public static class GfdDocumentoPeriodoDto {
            private Integer id;
            private LocalDate periodo;
        }
    }
}
