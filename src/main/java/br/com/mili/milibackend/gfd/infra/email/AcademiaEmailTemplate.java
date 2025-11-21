package br.com.mili.milibackend.gfd.infra.email;

import org.springframework.stereotype.Component;

@Component
public class AcademiaEmailTemplate {

    public static String template(
            String nomeCompleto,
            String url,
            String email
    ) {

        String html = """
                <p>Olá,</p>
                <p>Colaborador(a) <strong>%1$s</strong>,</p>
                <p>Para realizar o curso de integração, é <strong>necessário redefinir sua senha</strong> clicando em <strong>login</strong> usando o email registrado. Clique no link abaixo para acessar o portal da academia e realizar o processo:</p>
                <p>
                    <a href="%2$s" target="_blank"><strong>Clique aqui para acessar academia</strong></a><br>
                    ou copie e cole este link em seu navegador: <br>
                    <a href="%2$s" target="_blank">%2$s</a>
                </p>
                
                <p>
                email: %3$s
                </p>
                
                <p>Atenciosamente, <br>
                <strong>Equipe Mili S.A.</strong></p>
                """;

        // %1$s → razaoSocial
        // %2$s → url
        // %2$s → email

        return String.format(html, nomeCompleto, url, email);
    }
}
