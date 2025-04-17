package br.com.mili.milibackend.infra.security.repository.dummy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "CT_USUARIO")
public class AuthDummy  {
    @Id
    @Column(name = "CTUSU_CODIGO")
    private Integer id;

}