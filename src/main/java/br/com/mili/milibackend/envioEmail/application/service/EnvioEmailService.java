package br.com.mili.milibackend.envioEmail.application.service;

import br.com.mili.milibackend.envioEmail.domain.entity.EnvioEmail;
import br.com.mili.milibackend.envioEmail.domain.interfaces.IEnvioEmailService;
import br.com.mili.milibackend.envioEmail.infra.repository.EnvioEmailRepository;
import org.springframework.stereotype.Service;

@Service
public class EnvioEmailService implements IEnvioEmailService {
    private final EnvioEmailRepository envioEmailRepository;


    public EnvioEmailService(EnvioEmailRepository envioEmailRepository) {
        this.envioEmailRepository = envioEmailRepository;
    }

    public void enviarFila(EnvioEmail envioEmail) {
        envioEmailRepository.save(envioEmail);
    }

}
