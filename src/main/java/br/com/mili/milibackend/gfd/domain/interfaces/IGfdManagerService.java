package br.com.mili.milibackend.gfd.domain.interfaces;

import br.com.mili.milibackend.gfd.application.dto.manager.documentos.*;
import br.com.mili.milibackend.gfd.application.dto.manager.fornecedor.GfdMFornecedorGetInputDto;
import br.com.mili.milibackend.gfd.application.dto.manager.fornecedor.GfdMFornecedorGetOutputDto;
import br.com.mili.milibackend.gfd.application.dto.manager.funcionario.*;

public interface IGfdManagerService {
    GfdMVerificarFornecedorOutputDto verifyFornecedor(GfdMVerificarFornecedorInputDto inputDto);

    GfdMFornecedorGetOutputDto getFornecedor(GfdMFornecedorGetInputDto inputDto);

    GfdMFuncionarioGetAllOutputDto getAllFuncionarios(GfdMFuncionarioGetAllInputDto inputDto);

    GfdMFuncionarioCreateOutputDto createFuncionario(GfdMFuncionarioCreateInputDto inputDto);

    GfdMFuncionarioUpdateOutputDto updateFuncionario(GfdMFuncionarioUpdateInputDto inputDto);

    GfdMFuncionarioGetOutputDto getFuncionario(GfdMFuncionarioGetInputDto inputDto);

    void deleteFuncionario(GfdMFuncionarioDeleteInputDto inputDto);

    GfdMDocumentoUpdateOutputDto updateDocumento(GfdMDocumentoUpdateInputDto inputDto);

    GfdMDocumentoDownloadOutputDto downloadDocumento(GfdMDocumentoDownloadInputDto inputDto);

    void deleteDocumento(GfdMDocumentoDeleteInputDto inputDto);


}
