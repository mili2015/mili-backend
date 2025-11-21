package br.com.mili.milibackend.gfd.application.dto.gfdFuncionario;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@Builder
@Data
public class MatricularAcademiaFuncionarioInputDto {
    private Integer id;
    private String nome;
    private String cpf;
    private String email;

    @Schema(description = "Campo opcional, esse campo é usado para buscar as informações do usuario, atualizando o email antigo do usuário para o novo")
    private String oldEmail;

    private List<Integer> locaisTrabalho = new ArrayList<>();
}
