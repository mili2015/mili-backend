package br.com.mili.milibackend.gfd.application.dto.gfdDocumento;

import br.com.mili.milibackend.gfd.domain.entity.GfdDocumentoStatusEnum;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class GfdDocumentoCreateInputDto {
    private GfdDocumentoDto gfdDocumentoDto;
    private String base64File;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class GfdDocumentoDto {
        private Integer ctforCodigo;

        private String nomeArquivo;

        private String nomeArquivoPath;

        private Integer tamanhoArquivo;

        private LocalDate dataCadastro;

        private LocalDate dataEmissao;

        private LocalDate dataValidade;

        private String tipoArquivo;

        private String usuario;
        private GfdDocumentoStatusEnum status;

        private GfdTipoDocumentoDto gfdTipoDocumento;

        private GfdFuncionarioDto gfdFuncionario;

        @NoArgsConstructor
        @AllArgsConstructor
        @Getter
        @Setter
        public static class GfdFuncionarioDto {
            private Integer id;
        }

        @NoArgsConstructor
        @AllArgsConstructor
        @Getter
        @Setter
        public static class GfdTipoDocumentoDto {
            private Integer id;
        }
    }
}
