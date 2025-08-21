package br.com.mili.milibackend.gfd.application.dto.gfdDocumento;

import br.com.mili.milibackend.shared.page.Filtro;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GfdDocumentoGetAllInputDto extends Filtro {
    private Integer ctforCodigo;

    private String nomeArquivo;

    private LocalDate dataCadastroInic;
    private LocalDate dataCadastroFinal;

    private LocalDate dataValidadeInic;
    private LocalDate dataValidadeFinal;

    private LocalDate dataEmissaoInic;
    private LocalDate dataEmissaoFinal;

    private LocalDate periodo;

    private Integer tipoDocumentoId;

    private String usuario;

    private String status;

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
}
