package br.com.mili.milibackend.gfd.application.dto;

import br.com.mili.milibackend.shared.page.Filtro;
import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class GfdMFuncionarioGetAllInputDto  {
    private Integer codUsuario;
    private GfdMFuncionarioDto funcionario;

    @Getter
    @AllArgsConstructor
    @Setter
    @NoArgsConstructor
    public static class GfdMFuncionarioDto extends Filtro{
        private Integer id;
        private String nome;
        private String funcao;
        private String tipoContratacao;
        private LocalDate periodoInicio;
        private LocalDate periodoFim;
        private Integer ativo;
        private Integer liberado;

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
