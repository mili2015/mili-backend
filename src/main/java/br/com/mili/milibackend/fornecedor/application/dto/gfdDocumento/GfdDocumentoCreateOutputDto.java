package br.com.mili.milibackend.fornecedor.application.dto.gfdDocumento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GfdDocumentoCreateOutputDto {
    private Integer id;

    private Integer ctforCodigo;

    private String tipoDocumento;

    private String nomeArquivo;

    private String nomeArquivoPath;
    private LocalDate dataEmissao;

    private Long tamanhoArquivo;

    private byte[] arquivo;

    private LocalDate dataCadastro;

    private LocalDate dataValidade;

    private String tipoArquivo;

    private String usuario;

    private String maquina;

    private String status;

    private String observacao;

}
