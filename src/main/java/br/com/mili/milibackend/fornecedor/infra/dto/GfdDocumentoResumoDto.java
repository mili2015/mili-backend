package br.com.mili.milibackend.fornecedor.infra.dto;

import br.com.mili.milibackend.fornecedor.domain.entity.GfdDocumentoStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GfdDocumentoResumoDto {
    private Integer id;
    private Integer ctforCodigo;
    private String tipoDocumento;
    private String nomeArquivo;
    private String nomeArquivoPath;
    private Integer tamanhoArquivo;
    private LocalDate dataCadastro;
    private LocalDate dataEmissao;
    private LocalDate dataValidade;
    private String tipoArquivo;
    private String observacao;
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
