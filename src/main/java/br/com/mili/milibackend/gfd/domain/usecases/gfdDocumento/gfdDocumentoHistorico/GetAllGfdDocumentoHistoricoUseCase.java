package br.com.mili.milibackend.gfd.domain.usecases.gfdDocumento.gfdDocumentoHistorico;

import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.gfdHistoricoDocumento.GfdDocumentoHistoricoGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.gfdHistoricoDocumento.GfdDocumentoHistoricoGetAllOutputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.gfdFuncionarioLiberacao.GfdFuncionarioLiberacaoGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdFuncionario.gfdFuncionarioLiberacao.GfdFuncionarioLiberacaoGetAllOutputDto;

import java.util.List;

public interface GetAllGfdDocumentoHistoricoUseCase {
    List<GfdDocumentoHistoricoGetAllOutputDto> execute(GfdDocumentoHistoricoGetAllInputDto inputDto);
}
