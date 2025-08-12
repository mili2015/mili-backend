package br.com.mili.milibackend.gfd.application.dto.gfdDocumento;

import br.com.mili.milibackend.gfd.domain.entity.GfdDocumentoStatusEnum;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GfdDocumentoUpdateStatusObservacaoOutputDto {
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
    private GfdDocumentoGetAllOutputDto.GfdTipoDocumentoDto gfdTipoDocumento;
    private GfdDocumentoGetAllOutputDto.FuncionarioDto funcionario;


    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class GfdTipoDocumentoDto {
        private Integer id;
        private String nome;
        private Integer diasValidade;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class FuncionarioDto {
        private Integer id;
        private String nome;
        private String cpf;
    }
}
