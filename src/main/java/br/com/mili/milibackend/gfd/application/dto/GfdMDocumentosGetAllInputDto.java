package br.com.mili.milibackend.gfd.application.dto;

import br.com.mili.milibackend.gfd.domain.entity.GfdDocumentoStatusEnum;
import br.com.mili.milibackend.shared.page.Filtro;
import br.com.mili.milibackend.shared.validation.annotation.ValidaIntervaloData;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ValidaIntervaloData(inicio = "dataCadastroInic", fim = "dataCadastroFinal")
@ValidaIntervaloData(inicio = "dataValidadeInic", fim = "dataValidadeFinal")
@ValidaIntervaloData(inicio = "dataEmissaoInic", fim = "dataEmissaoFinal")
public class GfdMDocumentosGetAllInputDto extends Filtro {

    private String usuario;

    private Integer codUsuario;

    private Integer id;

    private String nomeArquivo;

    private LocalDate dataCadastroInic;
    private LocalDate dataCadastroFinal;

    private LocalDate dataValidadeInic;
    private LocalDate dataValidadeFinal;

    private LocalDate dataEmissaoInic;
    private LocalDate dataEmissaoFinal;

    private LocalDate periodo;

    private Integer tipoDocumentoId;
    private FuncionarioDto funcionario;

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

    private GfdDocumentoStatusEnum status;
}
