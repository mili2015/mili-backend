package br.com.mili.milibackend.fornecedor.application.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FornecedorMeusDadosUpdateInputDto {
    private Integer id;
    private Integer codUsuario;

    @NotBlank
    private String contato;


    private List<@NotBlank @Email String> emails;

    @NotBlank
    private String celular;

    @NotNull
    private Integer aceiteLgpd;
}
