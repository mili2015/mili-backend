package br.com.mili.milibackend.cliente.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CLIENTES")
public class Cliente {

    @Id
    private Integer codCliente;
    @Column(name = "RAZAO_SOCIAL")
    private String razaoSocial;
}
