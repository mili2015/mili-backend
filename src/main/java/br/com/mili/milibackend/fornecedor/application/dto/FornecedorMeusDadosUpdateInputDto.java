package br.com.mili.milibackend.fornecedor.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
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

    @Valid
    @NotNull
    private GfdTipoFornecedorDto tipoFornecedor;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class GfdTipoFornecedorDto {
        @NotNull
        private Integer id;
    }
}
