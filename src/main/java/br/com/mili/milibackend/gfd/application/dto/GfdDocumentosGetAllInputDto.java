package br.com.mili.milibackend.gfd.application.dto;

import br.com.mili.milibackend.fornecedor.domain.entity.GfdDocumentoStatusEnum;
import br.com.mili.milibackend.shared.annotation.ValidaIntervaloData;
import br.com.mili.milibackend.shared.page.Filtro;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ValidaIntervaloData(inicio = "dataCadastroInic", fim = "dataCadastroFinal")
@ValidaIntervaloData(inicio = "dataValidadeInic", fim = "dataValidadeFinal")
@ValidaIntervaloData(inicio = "dataEmissaoInic", fim = "dataEmissaoFinal")
public class GfdDocumentosGetAllInputDto extends Filtro {
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

    private Integer tipoDocumentoId;

    private GfdDocumentoStatusEnum status;
}
