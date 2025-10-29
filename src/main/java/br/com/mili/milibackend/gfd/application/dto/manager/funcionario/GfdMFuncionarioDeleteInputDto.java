package br.com.mili.milibackend.gfd.application.dto.manager.funcionario;

import lombok.*;


@Getter
@AllArgsConstructor
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class GfdMFuncionarioDeleteInputDto {
    private Integer codUsuario;
    private GfdFuncionarioDto funcionario;
    private boolean isAnalista;



    @Getter
    @AllArgsConstructor
    @Setter
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class GfdFuncionarioDto {
        private Integer id;
        private FornecedorDto fornecedor;

        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @Setter
        public static class FornecedorDto {
            private Integer codigo;
        }
    }
}
