package br.com.mili.milibackend.fornecedor.infra.email;

public class GfdDocumentoEmailTemplate {
    public static String template(String status, String url) {
        String html = """
                <p>Olá,</p> 
                <p>Um documento foi marcado como <strong>não conforme</strong>.</p>
                <p>Por favor, acesse o portal para verificar.</p>
                <p> 
                <a href="https://web2.mili.com.br/mili/gfd" target="_blank">Clique aqui para acessar o portal</a>
                 ou acesse diretamente o link </br> https://web2.mili.com.br/mili/gfd 
                 </p> 
                 <p>Atenciosamente,
                <strong>Equipe Mili S.A.</strong>
                </p>
                """;

        // %1$d → status
        // %2$s → url
        return String.format(html, status, url);
    }

}
