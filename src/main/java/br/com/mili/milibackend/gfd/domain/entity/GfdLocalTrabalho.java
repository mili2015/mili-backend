package br.com.mili.milibackend.gfd.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "GFD_LOCAL_TRABALHO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GfdLocalTrabalho {

    @EmbeddedId
    private GfdLocalTrabalhoPk id;

    @ManyToOne
    @MapsId("idFuncionario")
    @JoinColumn(name = "ID_FUNCIONARIO")
    private GfdFuncionario funcionario;

    public Integer getIdFuncionario() {
        return id != null ? id.getIdFuncionario() : null;
    }

    public void setIdFuncionario(Integer idFuncionario) {
        if (id == null) {
            id = new GfdLocalTrabalhoPk();
        }
        id.setIdFuncionario(idFuncionario);
    }

    public Integer getCtempCodigo() {
        return id != null ? id.getCtempCodigo() : null;
    }

    public void setCtempCodigo(Integer ctempCodigo) {
        if (id == null) {
            id = new GfdLocalTrabalhoPk();
        }
        id.setCtempCodigo(ctempCodigo);
    }
}