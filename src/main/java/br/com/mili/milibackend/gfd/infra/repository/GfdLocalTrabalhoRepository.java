package br.com.mili.milibackend.gfd.infra.repository;

import br.com.mili.milibackend.gfd.domain.entity.GfdLocalTrabalho;
import br.com.mili.milibackend.gfd.domain.entity.GfdLocalTrabalhoPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface GfdLocalTrabalhoRepository extends JpaRepository<GfdLocalTrabalho, GfdLocalTrabalhoPk> {

    @Query("SELECT g FROM GfdLocalTrabalho g LEFT JOIN FETCH g.funcionario WHERE g.funcionario.id IN (:id)")
    List<GfdLocalTrabalho> findByInIdFuncionario(List<Integer> id);

    @Modifying
    @Query("DELETE FROM GfdLocalTrabalho g WHERE g.id.idFuncionario = (:id)")
    void deleteByIdFuncionario(Integer id);
}
