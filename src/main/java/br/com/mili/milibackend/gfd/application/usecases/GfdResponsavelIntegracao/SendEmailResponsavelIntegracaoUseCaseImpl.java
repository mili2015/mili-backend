package br.com.mili.milibackend.gfd.application.usecases.GfdResponsavelIntegracao;

import br.com.mili.milibackend.envioEmail.domain.entity.EnvioEmail;
import br.com.mili.milibackend.envioEmail.domain.interfaces.IEnvioEmailService;
import br.com.mili.milibackend.envioEmail.shared.RemetenteEnum;
import br.com.mili.milibackend.gfd.application.dto.gfdResponsavelIntegracao.ResponsavelIntegracaoSendEmailInputDto;
import br.com.mili.milibackend.gfd.domain.usecases.GfdResponsavelIntegracao.SendEmailResponsavelIntegracaoUseCase;
import br.com.mili.milibackend.gfd.infra.email.GfdResponsavelIntegracaoEmailTemplate;
import br.com.mili.milibackend.gfd.infra.repository.gfdResponsavelIntegracao.GfdResponsavelIntegracaoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SendEmailResponsavelIntegracaoUseCaseImpl implements SendEmailResponsavelIntegracaoUseCase {
    private final String baseUrl;
    private final IEnvioEmailService envioEmailService;
    private final GfdResponsavelIntegracaoRepository gfdResponsavelIntegracaoRepository;

    public SendEmailResponsavelIntegracaoUseCaseImpl(@Value("${frontend.url.origin}") String baseUrl, IEnvioEmailService envioEmailService, GfdResponsavelIntegracaoRepository gfdResponsavelIntegracaoRepository) {
        this.baseUrl = baseUrl;
        this.envioEmailService = envioEmailService;
        this.gfdResponsavelIntegracaoRepository = gfdResponsavelIntegracaoRepository;
    }

    @Override
    public void execute(ResponsavelIntegracaoSendEmailInputDto inputDto) {
        var funcionario = inputDto.getFuncionario();
        var fornecedor = inputDto.getFornecedor();
        var ctempCodigo = inputDto.getCtempCodigo();

        var urlDocumento = baseUrl + "/gfd/empresa/" + fornecedor.getCodigo() + "/fornecedor/funcionario/" + funcionario.getId() + "/docs";

        var template = GfdResponsavelIntegracaoEmailTemplate.template(
                fornecedor.getCodigo(),
                fornecedor.getRazaoSocial(),
                funcionario.getId(),
                funcionario.getNome(),
                urlDocumento
        );

        var titulo = "Mili - Integração SESMT";

        var assunto = "Integração SESMT";

        var listResponsavelIntegracao = gfdResponsavelIntegracaoRepository.findByCtempCodigo(ctempCodigo);

        listResponsavelIntegracao.forEach(responsavelIntegracao -> {
            var envioEmail = EnvioEmail.builder()
                    .remetente(RemetenteEnum.MILI.getEndereco())
                    .destinatario(responsavelIntegracao.getEmail())
                    .assunto(assunto)
                    .titulo(titulo)
                    .texto(template)
                    .build();

            envioEmailService.enviarFila(envioEmail);
        });
    }
}
