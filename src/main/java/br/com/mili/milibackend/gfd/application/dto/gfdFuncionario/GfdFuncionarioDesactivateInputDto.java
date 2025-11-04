package br.com.mili.milibackend.gfd.application.dto.gfdFuncionario;

import lombok.*;

@Getter
@AllArgsConstructor
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class GfdFuncionarioDesactivateInputDto {
    private Integer codUsuario;
    private GfdFuncionarioDto funcionario;
    private boolean isAnalista = false;

    @Getter
    @AllArgsConstructor
    @Setter
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class GfdFuncionarioDto {
        private Integer id;
        private GfdFuncionarioDto.FornecedorDto fornecedor;

        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @Setter
        public static class FornecedorDto {
            private Integer codigo;
        }
    }

    public GfdFuncionarioDto getFuncionario() {
        if(funcionario == null) {
            funcionario = new GfdFuncionarioDto();
        }

        return funcionario;
    }
}
