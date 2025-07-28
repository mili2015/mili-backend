package br.com.mili.milibackend.gfd.application.dto.gfdDocumento;

import br.com.mili.milibackend.gfd.domain.entity.GfdDocumentoStatusEnum;
import br.com.mili.milibackend.gfd.domain.entity.GfdFuncionario;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class GfdDocumentoUpdateOutputDto {
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

    private GfdFuncionarioDto gfdFuncionario;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class GfdFuncionarioDto {
        private Integer id;
    }

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
