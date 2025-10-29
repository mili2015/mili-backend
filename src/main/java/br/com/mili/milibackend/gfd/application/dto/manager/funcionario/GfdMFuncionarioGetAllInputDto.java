package br.com.mili.milibackend.gfd.application.dto.manager.funcionario;

import br.com.mili.milibackend.shared.page.Filtro;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class GfdMFuncionarioGetAllInputDto  {
    private Integer codUsuario;
    private GfdMFuncionarioDto funcionario;
    private boolean isAnalista;

    @Getter
    @AllArgsConstructor
    @Setter
    @NoArgsConstructor
    public static class GfdMFuncionarioDto extends Filtro{
        private Integer id;
        private String nome;
        private String funcao;
        private Integer tipoContratacao;
        private LocalDate periodoInicio;
        private LocalDate periodoFim;
        private Integer ativo;
        private Integer liberado;

        private FornecedorDto fornecedor;
        private List<Integer> locaisTrabalho = new ArrayList<>();

        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @Setter
        @Builder
        public static class FornecedorDto {
            private Integer codigo;
        }

        public List<Integer> getLocaisTrabalho () {
            if(locaisTrabalho == null){
                locaisTrabalho = new ArrayList<>();
            }

            return locaisTrabalho;
        }
    }
}
