package br.com.mili.milibackend.gfd.application.mappers;

import br.com.mili.milibackend.fornecedor.domain.entity.Fornecedor;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.GfdFuncionarioUpdateInputDto;
import br.com.mili.milibackend.gfd.domain.entity.GfdFuncionario;

public class GfdFuncionarioUpdateMapper {

    public static void map(GfdFuncionarioUpdateInputDto inputDto, GfdFuncionario entity) {
        entity.setId(entity.getId());

        if (inputDto.getFornecedor() != null) {
            var fornecedor = entity.getFornecedor();

            if(fornecedor == null)
            {
                fornecedor = new Fornecedor();
            }

            fornecedor.setCodigo(inputDto.getFornecedor().getCodigo());
        }

        entity.setNome(inputDto.getNome());
        entity.setCpf(inputDto.getCpf());
        entity.setDataNascimento(inputDto.getDataNascimento());
        entity.setPaisNacionalidade(inputDto.getPaisNacionalidade());
        entity.setFuncao(inputDto.getFuncao());
        entity.setTipoContratacao(inputDto.getTipoContratacao());
        entity.setPeriodoInicial(inputDto.getPeriodoInicial());
        entity.setPeriodoFinal(inputDto.getPeriodoFinal());
        entity.setObservacao(inputDto.getObservacao());
    }
}
