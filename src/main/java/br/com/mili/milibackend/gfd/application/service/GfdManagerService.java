package br.com.mili.milibackend.gfd.application.service;

import br.com.mili.milibackend.fornecedor.application.dto.FornecedoGetByCodUsuarioInputDto;
import br.com.mili.milibackend.fornecedor.application.service.FornecedorService;
import br.com.mili.milibackend.gfd.adapter.exception.GfdCodeException;
import br.com.mili.milibackend.gfd.adapter.exception.GfdMessageException;
import br.com.mili.milibackend.gfd.application.dto.GfdFornecedorGetInputDto;
import br.com.mili.milibackend.gfd.application.dto.GfdFornecedorGetOutputDto;
import br.com.mili.milibackend.gfd.application.dto.GfdVerificarDocumentosInputDto;
import br.com.mili.milibackend.gfd.application.dto.GfdVerificarDocumentosOutputDto;
import br.com.mili.milibackend.gfd.domain.interfaces.IGfdManagerService;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GfdManagerService implements IGfdManagerService {

    @Autowired
    private FornecedorService fornecedorService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public GfdVerificarDocumentosOutputDto verifyDocumentos(GfdVerificarDocumentosInputDto inputDto) {
        return null;
    }

    @Override
    public GfdFornecedorGetOutputDto getFornecedor(GfdFornecedorGetInputDto inputDto) {
        var fornecedoGetByCodUsuarioInputDto = new FornecedoGetByCodUsuarioInputDto(inputDto.getCodUsuario());

        var fornecedor = fornecedorService.getByCodUsuario(fornecedoGetByCodUsuarioInputDto);

        if(fornecedor == null) {
            throw new NotFoundException(GfdMessageException.GFD_FORNECEDOR_NAOENCONTRADO, GfdCodeException.GFD_FORNECEDOR_NAOENCONTRADO);
        }

        return modelMapper.map(fornecedor, GfdFornecedorGetOutputDto.class);
    }
}
