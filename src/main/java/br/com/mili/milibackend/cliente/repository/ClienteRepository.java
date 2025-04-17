package br.com.mili.milibackend.cliente.repository;

import br.com.mili.milibackend.cliente.entity.Cliente;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface ClienteRepository extends Repository<Cliente, Integer> {

    @Query(value = "SELECT CODIGO_REDE FROM CLIENTES WHERE CODCLIENTE =:codCliente", nativeQuery = true)
    Integer findCodRede(Integer codCliente);
}
