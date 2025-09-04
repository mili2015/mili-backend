package br.com.mili.milibackend.gfd.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GfdLocalTrabalhoPk {
    @Column(name = "ID_FUNCIONARIO")
    private Integer idFuncionario;

    @Column(name = "CTEMP_CODIGO")
    private Integer ctempCodigo;
}
