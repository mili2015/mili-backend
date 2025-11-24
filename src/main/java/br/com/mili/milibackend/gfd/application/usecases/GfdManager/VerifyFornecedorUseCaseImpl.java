package br.com.mili.milibackend.gfd.application.usecases.GfdManager;

import br.com.mili.milibackend.fornecedor.domain.entity.Fornecedor;
import br.com.mili.milibackend.fornecedor.domain.usecases.GetFornecedorByCodOrIdUseCase;
import br.com.mili.milibackend.gfd.application.dto.manager.funcionario.GfdMVerificarFornecedorInputDto;
import br.com.mili.milibackend.gfd.application.dto.manager.funcionario.GfdMVerificarFornecedorOutputDto;
import br.com.mili.milibackend.gfd.domain.usecases.gfdManager.VerifyFornecedorUseCase;
import br.com.mili.milibackend.gfd.infra.repository.gfdDocumento.GfdDocumentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class VerifyFornecedorUseCaseImpl implements VerifyFornecedorUseCase {
    private final GetFornecedorByCodOrIdUseCase getFornecedorByCodOrIdUseCase;
    private final GfdDocumentoRepository gfdDocumentoRepository;

    @Override
    public GfdMVerificarFornecedorOutputDto execute(GfdMVerificarFornecedorInputDto inputDto) {
        var fornecedor = getFornecedorByCodOrIdUseCase.execute(
                inputDto.getCodUsuario(),
                inputDto.getId(),
                inputDto.isAnalista()
        );

        String razaoSocial = fornecedor.getRazaoSocial();

        return new GfdMVerificarFornecedorOutputDto(
                representanteCadastrado(fornecedor),
                razaoSocial != null ? razaoSocial : "Bem-Vindo",
                documentosPendente(fornecedor)
        );
    }

    private boolean representanteCadastrado(Fornecedor fornecedor) {
        return fornecedor.getContato() != null
                && fornecedor.getEmail() != null
                && fornecedor.getCelular() != null
                && (fornecedor.getAceiteLgpd() != null && fornecedor.getAceiteLgpd().equals(1));
    }

    private boolean documentosPendente(Fornecedor fornecedor) {
        LocalDate inicioMesAnterior = LocalDate.now()
                .minusMonths(1)
                .withDayOfMonth(1);

        var documentos = gfdDocumentoRepository.getAllCount(fornecedor.getCodigo(), inicioMesAnterior);

        return documentos.getTotalEnviado() > 0 ||
                documentos.getTotalNaoConforme() > 0 ||
                documentos.getTotalEmAnalise() > 0 ||
                documentos.getNaoEnviado() > 0;
    }
}
