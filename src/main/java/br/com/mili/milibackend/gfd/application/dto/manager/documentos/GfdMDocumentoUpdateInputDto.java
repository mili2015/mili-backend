package br.com.mili.milibackend.gfd.application.dto.manager.documentos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class GfdMDocumentoUpdateInputDto {
    @Valid
    private GfdDocumentoUpdateInputDto documento;
    private FornecedorDto fornecedor;
    private Integer codUsuario;
    private GfdDocumentoPeriodoDto gfdDocumentoPeriodo;

    private boolean isAnalista = false;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @EqualsAndHashCode
    public static class GfdDocumentoUpdateInputDto {
        @NotNull
        private Integer id;

        @NotNull
        private LocalDate dataEmissao;

        private LocalDate dataValidade;

        @NotNull
        private String status;

        private String observacao;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @EqualsAndHashCode
    public static class FornecedorDto {
        private Integer id;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class GfdDocumentoPeriodoDto {
        private LocalDate periodo;
    }

}
