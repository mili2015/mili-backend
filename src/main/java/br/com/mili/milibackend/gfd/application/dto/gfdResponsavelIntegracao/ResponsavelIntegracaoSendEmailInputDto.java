package br.com.mili.milibackend.gfd.application.dto.gfdResponsavelIntegracao;

import lombok.*;

@Getter
@AllArgsConstructor
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Builder
public class ResponsavelIntegracaoSendEmailInputDto{
    private FuncionarioDto funcionario;
    private FornecedorDto fornecedor;
    private Integer ctempCodigo;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class FuncionarioDto {
        private Integer id;
        private String nome;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class FornecedorDto {
        private Integer codigo;
        private String razaoSocial;
    }
}
