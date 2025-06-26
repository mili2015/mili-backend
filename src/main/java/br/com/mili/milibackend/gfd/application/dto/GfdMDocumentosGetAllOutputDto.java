package br.com.mili.milibackend.gfd.application.dto;

import br.com.mili.milibackend.fornecedor.domain.entity.GfdDocumentoStatusEnum;
import br.com.mili.milibackend.shared.page.pagination.MyPage;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class GfdMDocumentosGetAllOutputDto {
    private MyPage<GfdDocumentoDto> gfdDocumento;
    private GfdTipoDocumentoDto gfdTipoDocumento;
    private Integer nextDoc;
    private Integer previousDoc;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class  GfdTipoDocumentoDto {
        private Integer id;
        private String nome;
        private Integer diasValidade;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class GfdDocumentoDto {
        private Integer id;
        private Integer ctforCodigo;
        private String tipoDocumento;
        private String nomeArquivo;
        private String nomeArquivoPath;
        private String observacao;
        private Integer tamanhoArquivo;
        private LocalDate dataCadastro;
        private LocalDate dataValidade;
        private LocalDate dataEmissao;
        private String tipoArquivo;
        private GfdDocumentoStatusEnum status;
        private GfdTipoDocumentoDto gfdTipoDocumento;

        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @Setter
        public static class GfdTipoDocumentoDto {
            private Integer id;
            private String nome;
            private Integer diasValidade;
        }
    }
}
