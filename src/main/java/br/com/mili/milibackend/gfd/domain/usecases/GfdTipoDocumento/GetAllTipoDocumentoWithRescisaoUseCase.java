package br.com.mili.milibackend.gfd.domain.usecases.GfdTipoDocumento;

import br.com.mili.milibackend.gfd.application.usecases.GfdTipoDocumento.GfdTipoDocumentoWithRescisaoGetAllInputDto;
import br.com.mili.milibackend.gfd.application.usecases.GfdTipoDocumento.GfdTipoDocumentoWithRescisaoGetAllOutputDto;

public interface GetAllTipoDocumentoWithRescisaoUseCase {

    GfdTipoDocumentoWithRescisaoGetAllOutputDto execute (GfdTipoDocumentoWithRescisaoGetAllInputDto inputDto);
}
