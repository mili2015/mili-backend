package br.com.mili.milibackend.gfd.application.usecases.GfdDocumento;

import br.com.mili.milibackend.gfd.application.dto.gfdDocumento.GfdDocumentoDeleteInputDto;
import br.com.mili.milibackend.gfd.domain.entity.GfdDocumentoStatusEnum;
import br.com.mili.milibackend.gfd.domain.usecases.DeleteGfdDocumentoUseCase;
import br.com.mili.milibackend.gfd.infra.repository.GfdDocumentoPeriodoRepository;
import br.com.mili.milibackend.gfd.infra.repository.gfdDocumento.GfdDocumentoRepository;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static br.com.mili.milibackend.gfd.adapter.exception.GfdDocumentoCodeException.GFD_DOCUMENTO_DELETE_PERMISSAO_INVALIDA;
import static br.com.mili.milibackend.gfd.adapter.exception.GfdDocumentoCodeException.GFD_DOCUMENTO_NAO_ENCONTRADO;

@Service
@RequiredArgsConstructor
public class DeleteGfdDocumentoUseCaseImpl implements DeleteGfdDocumentoUseCase {
    private final GfdDocumentoRepository gfdDocumentoRepository;
    private final GfdDocumentoPeriodoRepository gfdDocumentoPeriodoRepository;

    @Override
    @Transactional
    public void execute(GfdDocumentoDeleteInputDto inputDto) {
        var gfdDocumento = gfdDocumentoRepository.findById(inputDto.getId())
                .orElseThrow(() ->
                        new NotFoundException(GFD_DOCUMENTO_NAO_ENCONTRADO.getMensagem(), GFD_DOCUMENTO_NAO_ENCONTRADO.getCode())
                );

        boolean isNotStatusEnviado = !gfdDocumento.getStatus().equals(GfdDocumentoStatusEnum.ENVIADO);

        if (isNotStatusEnviado) {
            throw new NotFoundException(GFD_DOCUMENTO_DELETE_PERMISSAO_INVALIDA.getMensagem(), GFD_DOCUMENTO_DELETE_PERMISSAO_INVALIDA.getCode());
        }

        var gfdDocumentoPeriodo = gfdDocumentoPeriodoRepository.getByGfdDocumento_Id(gfdDocumento.getId());

        gfdDocumentoPeriodo.ifPresent(gfdDocumentoPeriodoRepository::delete);

        gfdDocumentoRepository.delete(gfdDocumento);
    }
}
