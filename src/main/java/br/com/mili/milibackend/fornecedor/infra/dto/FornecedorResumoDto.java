package br.com.mili.milibackend.fornecedor.infra.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FornecedorResumoDto {
    private Integer codigo;
    private String cgcCpf;
    private String razaoSocial;
    private String nomeFantasia;
    private String celular;
    private String contato;
    private String email;
    private Integer aceiteLgpd;
}
