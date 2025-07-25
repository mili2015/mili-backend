package br.com.mili.milibackend.gfd.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "GFD_TIPO_DOCUMENTO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GfdTipoDocumento {

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NOME")
    private String nome;

    @Column(name = "TIPO")
    @Enumerated(EnumType.STRING)
    private GfdTipoDocumentoTipoEnum tipo;

    @Column(name = "DIAS_VALIDADE")
    private Integer diasValidade;

    @Column(name = "OBRIGATORIEDADE")
    private Boolean obrigatoriedade;

    @Column(name = "ATIVO")
    private Boolean ativo;
}