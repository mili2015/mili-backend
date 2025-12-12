package br.com.mili.milibackend.fornecedor.application.dto;

import br.com.mili.milibackend.shared.page.Filtro;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FornecedorGetAllInputDto extends Filtro {
    private Integer codigo;
    private String cgcCpf;
    private String razaoSocial;
    private Boolean ativoGfd;
}
