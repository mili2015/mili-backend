package br.com.mili.milibackend.gfd.infra.email;

import org.springframework.stereotype.Component;

@Component
public class GfdDocumentoEmailTemplate {
    public static String template(String status, String url) {
        String html = """
                <p>Olá,</p>
                <p>Um documento foi marcado como <strong>%1$s</strong>.</p>
                <p>Por favor, acesse o portal para verificar.</p>
                <p>
                <a href="%2$s" target="_blank">Clique aqui para acessar o portal</a>
                 ou acesse diretamente o link %2$s
                 </p>
                 <p>Atenciosamente,
                <strong>Equipe Mili S.A.</strong>
                </p>
                """;

        // %1$s → status
        // %2$s → url
        return String.format(html, status, url);
    }

}