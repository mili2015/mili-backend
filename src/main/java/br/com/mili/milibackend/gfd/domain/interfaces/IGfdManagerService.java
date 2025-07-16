package br.com.mili.milibackend.gfd.domain.interfaces;

import br.com.mili.milibackend.gfd.application.dto.*;
import br.com.mili.milibackend.shared.page.pagination.MyPage;

import java.util.List;

public interface IGfdManagerService {
    GfdMVerificarFornecedorOutputDto verifyFornecedor(GfdMVerificarFornecedorInputDto inputDto);

    GfdMVerificarDocumentosOutputDto verifyDocumentos(GfdMVerificarDocumentosInputDto inputDto);

    GfdMFornecedorGetOutputDto getFornecedor(GfdMFornecedorGetInputDto inputDto);

    GfdMUploadDocumentoOutputDto uploadDocumento(GfdMUploadDocumentoInputDto inputDto);

    GfdMDocumentosGetAllOutputDto getAllDocumentos(GfdMDocumentosGetAllInputDto inputDto);

    GfdMFuncionarioGetAllOutputDto getAllFuncionarios(GfdMFuncionarioGetAllInputDto inputDto);

    GfdMFuncionarioCreateOutputDto createFuncionario(GfdMFuncionarioCreateInputDto inputDto);

    GfdMFuncionarioUpdateOutputDto updateFuncionario(GfdMFuncionarioUpdateInputDto inputDto);

    GfdMFuncionarioGetOutputDto getFuncionario(GfdMFuncionarioGetInputDto inputDto);

    void deleteFuncionario(GfdMFuncionarioDeleteInputDto inputDto);
}
