package br.com.mili.milibackend.envioEmail.infra.repository;

import br.com.mili.milibackend.envioEmail.domain.entity.EnvioEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnvioEmailRepository extends JpaRepository<EnvioEmail, Long> {
}
