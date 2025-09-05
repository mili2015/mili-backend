package br.com.mili.milibackend.gfd.infra.email;

import org.springframework.stereotype.Component;

@Component
public class GfdResponsavelIntegracaoEmailTemplate {

    public static String template(Integer idEmpresa,
                                  String razaoSocial,
                                  Integer idFuncionario,
                                  String nomeFuncionario,
                                  String url) {

        String html = """
                <p>Olá,</p>
                <p>Empresa <strong>%1$s</strong> (ID: %2$d).</p>
                <p>Solicita integração para funcionário <strong>%3$s</strong> (ID: %4$d).</p>
                <p>Por favor, acesse o portal para verificar.</p>
                <p>
                    <a href="%5$s" target="_blank">Clique aqui para acessar o portal</a>
                    ou acesse diretamente o link %5$s
                </p>
                <p>Atenciosamente,
                    <strong>Equipe Mili S.A.</strong>
                </p>
                """;

        // %1$s → razaoSocial
        // %2$d → idEmpresa
        // %3$s → nomeFuncionario
        // %4$d → idFuncionario
        // %5$s → url

        return String.format(html, razaoSocial, idEmpresa, nomeFuncionario, idFuncionario, url);
    }
}
