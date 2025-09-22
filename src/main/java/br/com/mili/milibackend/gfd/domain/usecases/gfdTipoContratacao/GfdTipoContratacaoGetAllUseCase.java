package br.com.mili.milibackend.gfd.domain.usecases.gfdTipoContratacao;

import br.com.mili.milibackend.gfd.application.dto.tipoContratacao.GfdTipoContratacaoGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.tipoContratacao.GfdTipoContratacaoGetAllOutputDto;

import java.util.List;

public interface GfdTipoContratacaoGetAllUseCase {
    List<GfdTipoContratacaoGetAllOutputDto> execute(GfdTipoContratacaoGetAllInputDto inputDto);

}
