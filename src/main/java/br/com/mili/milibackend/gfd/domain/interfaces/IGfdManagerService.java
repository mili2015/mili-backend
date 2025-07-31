package br.com.mili.milibackend.gfd.domain.interfaces;

import br.com.mili.milibackend.gfd.application.dto.*;
import br.com.mili.milibackend.gfd.application.dto.gfdTipoDocumento.*;

public interface IGfdManagerService {
    GfdMVerificarFornecedorOutputDto verifyFornecedor(GfdMVerificarFornecedorInputDto inputDto);

    GfdMVerificarDocumentosOutputDto verifyDocumentos(GfdMVerificarDocumentosInputDto inputDto);

    GfdMFornecedorGetOutputDto getFornecedor(GfdMFornecedorGetInputDto inputDto);

    GfdMDocumentosGetAllOutputDto getAllDocumentos(GfdMDocumentosGetAllInputDto inputDto);

    GfdMFuncionarioGetAllOutputDto getAllFuncionarios(GfdMFuncionarioGetAllInputDto inputDto);

    GfdMFuncionarioCreateOutputDto createFuncionario(GfdMFuncionarioCreateInputDto inputDto);

    GfdMFuncionarioUpdateOutputDto updateFuncionario(GfdMFuncionarioUpdateInputDto inputDto);

    GfdMFuncionarioGetOutputDto getFuncionario(GfdMFuncionarioGetInputDto inputDto);

    void deleteFuncionario(GfdMFuncionarioDeleteInputDto inputDto);

    GfdMDocumentoUpdateOutputDto updateDocumento(GfdMDocumentoUpdateInputDto inputDto);

    GfdMDocumentoDownloadOutputDto downloadDocumento(GfdMDocumentoDownloadInputDto inputDto);

    void deleteDocumento(GfdMDocumentoDeleteInputDto inputDto);


}
