package br.com.mili.milibackend.fornecedor.application.dto.gfdDocumento;

import br.com.mili.milibackend.fornecedor.domain.entity.GfdDocumentoStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FindLatestDocumentsGroupedByTipoAndFornecedorIdOutputDto {
    private Integer id;
    private Integer ctforCodigo;
    private String tipoDocumento;
    private String nomeArquivo;
    private String nomeArquivoPath;
    private Integer tamanhoArquivo;
    private LocalDate dataEmissao;
    private LocalDate dataCadastro;
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
