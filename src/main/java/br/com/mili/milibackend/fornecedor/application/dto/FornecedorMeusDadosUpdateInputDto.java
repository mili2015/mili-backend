package br.com.mili.milibackend.fornecedor.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FornecedorMeusDadosUpdateInputDto {
    private Integer id;
    private Integer codUsuario;

    @NotBlank
    private String contato;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String celular;
}
