package br.com.mili.milibackend.gfd.application.dto.gfdFuncionario;

import br.com.mili.milibackend.gfd.domain.entity.GfdTipoContratacao;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class GfdFuncionarioUpdateInputDto {
    private Integer id;
    private FornecedorDto fornecedor;
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private String paisNacionalidade;
    private String funcao;
    private LocalDate periodoInicial;
    private LocalDate periodoFinal;
    private String email;
    private String observacao;
    private List<LocalTrabalhoDto> locaisTrabalho;

    private GfdTipoContratacaoDto tipoContratacao;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class GfdTipoContratacaoDto {
        private Integer id;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class FornecedorDto {
        private Integer codigo;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class LocalTrabalhoDto {
        private Integer ctempCodigo;
    }
}
