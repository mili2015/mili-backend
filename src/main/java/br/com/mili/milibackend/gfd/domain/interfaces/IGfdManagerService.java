package br.com.mili.milibackend.gfd.domain.interfaces;

import br.com.mili.milibackend.gfd.application.dto.*;

import java.util.List;

public interface IGfdManagerService {
    GfdVerificarFornecedorOutputDto verifyFornecedor(GfdVerificarFornecedorInputDto inputDto);

    List<GfdVerificarDocumentosOutputDto> verifyDocumentos(GfdVerificarDocumentosInputDto inputDto);

    GfdFornecedorGetOutputDto getFornecedor(GfdFornecedorGetInputDto inputDto);

    GfdUploadDocumentoOutputDto uploadDocumento(GfdUploadDocumentoInputDto inputDto);

    GfdDocumentosGetAllOutputDto getAllDocumentos(GfdDocumentosGetAllInputDto inputDto);
}
