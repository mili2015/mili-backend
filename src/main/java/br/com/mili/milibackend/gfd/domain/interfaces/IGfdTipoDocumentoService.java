package br.com.mili.milibackend.gfd.domain.interfaces;

import br.com.mili.milibackend.gfd.application.dto.gfdTipoDocumento.*;

import java.util.List;

public interface IGfdTipoDocumentoService {
    List<GfdTipoDocumentoGetAllOutputDto> getAll(GfdTipoDocumentoGetAllInputDto inputDto) ;
    GfdTipoDocumentoGetByIdOutputDto getById(Integer id);
    void delete(Integer id);
    GfdTipoDocumentoCreateOutputDto create(GfdTipoDocumentoCreateInputDto inputDto);
    GfdTipoDocumentoUpdateOutputDto update(GfdTipoDocumentoUpdateInputDto inputDto);
}
