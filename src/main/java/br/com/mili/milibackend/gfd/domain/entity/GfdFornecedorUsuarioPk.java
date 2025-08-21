package br.com.mili.milibackend.gfd.domain.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
public class GfdFornecedorUsuarioPk {
    private Integer codUsuario;
    private Integer fornecedorId;
}
