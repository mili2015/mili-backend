package br.com.mili.milibackend.shared.infra.security.repository.dummy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "CT_USUARIO")
public class AuthDummy  {
    @Id
    @Column(name = "CTUSU_CODIGO")
    private Integer id;

}