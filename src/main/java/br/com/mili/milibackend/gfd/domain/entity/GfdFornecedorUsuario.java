package br.com.mili.milibackend.gfd.domain.entity;

import br.com.mili.milibackend.fornecedor.domain.entity.Fornecedor;
import br.com.mili.milibackend.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "GFD_FORNECEDOR_USUARIO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GfdFornecedorUsuario {

    @EmbeddedId
    private GfdFornecedorUsuarioPk id;

    @ManyToOne
    @MapsId("codUsuario")
    @JoinColumn(name = "CTUSU_CODIGO")
    private User usuario;

    @ManyToOne
    @MapsId("fornecedorId")
    @JoinColumn(name = "CTFOR_CODIGO")
    private Fornecedor fornecedor;
}
