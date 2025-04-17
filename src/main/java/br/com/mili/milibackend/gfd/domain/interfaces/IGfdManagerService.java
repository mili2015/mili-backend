package br.com.mili.milibackend.gfd.domain.interfaces;

import br.com.mili.milibackend.gfd.application.dto.GfdFornecedorGetInputDto;
import br.com.mili.milibackend.gfd.application.dto.GfdFornecedorGetOutputDto;
import br.com.mili.milibackend.gfd.application.dto.GfdVerificarDocumentosInputDto;
import br.com.mili.milibackend.gfd.application.dto.GfdVerificarDocumentosOutputDto;

public interface IGfdManagerService {
    public GfdVerificarDocumentosOutputDto verifyDocumentos (GfdVerificarDocumentosInputDto inputDto) ;
    public GfdFornecedorGetOutputDto  getFornecedor (GfdFornecedorGetInputDto inputDto) ;
}
