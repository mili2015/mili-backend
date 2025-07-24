package br.com.mili.milibackend.envioEmail.domain.interfaces;


import br.com.mili.milibackend.envioEmail.domain.entity.EnvioEmail;

public interface IEnvioEmailService {
    void enviarFila(EnvioEmail envioEmail);
}
