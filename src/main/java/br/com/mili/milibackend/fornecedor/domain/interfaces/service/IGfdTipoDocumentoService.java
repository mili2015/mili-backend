package br.com.mili.milibackend.fornecedor.domain.interfaces.service;

import br.com.mili.milibackend.fornecedor.application.dto.GfdTipoDocumentoGetByIdOutputDto;
import br.com.mili.milibackend.fornecedor.application.dto.gfdTipoDocumento.GfdTipoDocumentoGetAllInputDto;
import br.com.mili.milibackend.fornecedor.application.dto.gfdTipoDocumento.GfdTipoDocumentoGetAllOutputDto;

import java.util.List;

public interface IGfdTipoDocumentoService {
    List<GfdTipoDocumentoGetAllOutputDto> getAll(GfdTipoDocumentoGetAllInputDto inputDto) ;
    GfdTipoDocumentoGetByIdOutputDto getById(Integer id);
}
