package br.com.mili.milibackend.gfd.domain.usecases.GfdResponsavelIntegracao;

import br.com.mili.milibackend.gfd.application.dto.gfdResponsavelIntegracao.ResponsavelIntegracaoSendEmailInputDto;

public interface SendEmailResponsavelIntegracaoUseCase {
    void execute(ResponsavelIntegracaoSendEmailInputDto inputDto);
}
