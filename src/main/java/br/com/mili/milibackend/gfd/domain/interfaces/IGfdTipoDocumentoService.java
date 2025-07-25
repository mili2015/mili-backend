package br.com.mili.milibackend.gfd.domain.interfaces;

import br.com.mili.milibackend.gfd.application.dto.GfdTipoDocumentoGetByIdOutputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdTipoDocumento.GfdTipoDocumentoGetAllInputDto;
import br.com.mili.milibackend.gfd.application.dto.gfdTipoDocumento.GfdTipoDocumentoGetAllOutputDto;

import java.util.List;

public interface IGfdTipoDocumentoService {
    List<GfdTipoDocumentoGetAllOutputDto> getAll(GfdTipoDocumentoGetAllInputDto inputDto) ;
    GfdTipoDocumentoGetByIdOutputDto getById(Integer id);
}
