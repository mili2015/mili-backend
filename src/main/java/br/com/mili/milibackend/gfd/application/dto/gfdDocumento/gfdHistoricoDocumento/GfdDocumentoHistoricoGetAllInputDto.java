package br.com.mili.milibackend.gfd.application.dto.gfdDocumento.gfdHistoricoDocumento;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Setter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Builder
@Schema(description = "Filtro de busca para o histórico de documentos")
public class GfdDocumentoHistoricoGetAllInputDto {
    @Schema(description = "Campo é automaticamente preenchido quando o usuario é um fornecedor, esse campo impossibilita o usuário de acessar o histórico de outros fornecedores")
    private Integer usuarioId;

    private GfdDocumentoHistoricoDto gfdDocumentoHistorico;

    @Getter
    @AllArgsConstructor
    @Setter
    @NoArgsConstructor
    @Builder
    @Valid
    public static class GfdDocumentoHistoricoDto {
        @Schema(description = "Identificador do funcionário relacionado ao histórico")
        private Integer funcionarioId;

        @Schema(description = "Identificador do documento (definido internamente pelo sistema)", hidden = true)
        private Integer documentoId;

        @Schema(description = "Código do fornecedor associado ao documento")
        private Integer ctforCodigo;

        @Schema(description = "Código do usuário responsável pela ação, CAMPO UTILIZAVEL APENAS PARA ANALISTAS")
        private Integer ctusuCodigo;

        @Schema(description = "Data inicial do período de busca (formato: yyyy-MM-dd)")
        @PastOrPresent(message = "O período inicial não pode ser uma data futura.")
        private LocalDate periodoInicio;

        @Schema(description = "Data final do período de busca (formato: yyyy-MM-dd)")
        private LocalDate periodoFim;

        @Schema(description = "Status do documento (ex: ENVIADO, EM_ANALISE, NAO_CONFORME, CONFORME, EXPIRADO)")
        private String status;
    }


    public GfdDocumentoHistoricoDto getGfdDocumentoHistorico() {
        if (gfdDocumentoHistorico == null) gfdDocumentoHistorico = new GfdDocumentoHistoricoDto();

        return gfdDocumentoHistorico;
    }
}