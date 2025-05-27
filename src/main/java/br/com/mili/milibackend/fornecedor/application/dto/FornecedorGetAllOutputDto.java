package br.com.mili.milibackend.fornecedor.application.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class FornecedorGetAllOutputDto {
    private Integer codigo;
    private String cgcCpf;
    private String razaoSocial;
    private String nomeFantasia;
    private String celular;
    private String contato;
    private String email;
}
