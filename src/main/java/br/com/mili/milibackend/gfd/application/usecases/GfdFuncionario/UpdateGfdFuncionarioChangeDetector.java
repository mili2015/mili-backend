package br.com.mili.milibackend.gfd.application.usecases.GfdFuncionario;

import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioUpdateInputDto;
import br.com.mili.milibackend.gfd.domain.entity.GfdFuncionario;
import br.com.mili.milibackend.gfd.domain.entity.GfdLocalTrabalho;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class UpdateGfdFuncionarioChangeDetector {
    public static boolean hasFuncionarioChanges(GfdFuncionarioUpdateInputDto dto, GfdFuncionario entity) {
        if (!Objects.equals(dto.getFornecedor() != null ? dto.getFornecedor().getCodigo() : null,
                entity.getFornecedor() != null ? entity.getFornecedor().getCodigo() : null)) {
            return true;
        }
        if (!Objects.equals(dto.getNome(), entity.getNome())) return true;
        if (!Objects.equals(dto.getCpf(), entity.getCpf())) return true;
        if (!Objects.equals(dto.getDataNascimento(), entity.getDataNascimento())) return true;
        if (!Objects.equals(dto.getPaisNacionalidade(), entity.getPaisNacionalidade())) return true;
        if (!Objects.equals(dto.getFuncao(), entity.getFuncao())) return true;
        if (!Objects.equals(dto.getTipoContratacao(), entity.getTipoContratacao())) return true;
        if (!Objects.equals(dto.getPeriodoInicial(), entity.getPeriodoInicial())) return true;
        if (!Objects.equals(dto.getPeriodoFinal(), entity.getPeriodoFinal())) return true;
        if (!Objects.equals(dto.getObservacao(), entity.getObservacao())) return true;

        return false;
    }

    public static Set<Integer> hasLocaisChanges(GfdFuncionarioUpdateInputDto dto, GfdFuncionario entity) {
        Set<Integer> atuais = entity.getLocaisTrabalho() == null
                ? Set.of()
                : entity.getLocaisTrabalho().stream()
                .map(GfdLocalTrabalho::getCtempCodigo)
                .collect(Collectors.toSet());

        Set<Integer> novos = dto.getLocaisTrabalho() == null ? Set.of() :
                dto.getLocaisTrabalho().stream()
                        .map(GfdFuncionarioUpdateInputDto.LocalTrabalhoDto::getCtempCodigo)
                        .collect(Collectors.toSet());

        Set<Integer> paraAdicionar = new HashSet<>(novos);
        paraAdicionar.removeAll(atuais);

        return paraAdicionar;
    }
}
