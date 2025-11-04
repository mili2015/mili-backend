package br.com.mili.milibackend.gfd.application.dto.gfdDocumento;

import br.com.mili.milibackend.gfd.domain.entity.GfdDocumentoStatusEnum;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class GfdDocumentoCreateInputDto {
    private GfdDocumentoDto gfdDocumentoDto;
    private Integer codUsuario;
    private String base64File;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
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


        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class GfdFuncionarioDto {
            private Integer id;
        }

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class GfdTipoDocumentoDto {
            private Integer id;
        }

        public GfdDocumentoDto withFuncionario(Integer funcionarioId) {
            return this.toBuilder()
                    .gfdFuncionario(new GfdFuncionarioDto(funcionarioId))
                    .build();
        }

        public static GfdTipoDocumentoDto tipoDocumentoOf(Integer id) {
            return new GfdTipoDocumentoDto(id);
        }

        public static GfdFuncionarioDto funcionarioOf(Integer id) {
            return new GfdFuncionarioDto(id);
        }


    }
}
