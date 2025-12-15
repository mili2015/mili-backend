package br.com.mili.milibackend.gfd.adapter.web.external.external;

import br.com.mili.milibackend.gfd.adapter.web.external.external.dto.ReportGfdDocumentoRequestDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static br.com.mili.milibackend.gfd.adapter.web.external.external.exception.ReportExternalCodeException.REPORT_EXTERNAL_ERROR;


@Service
public class ReportExternalService {

    private final WebClient webClient;

    @Value("${report.url}")
    private String url;

    @Value("${report.key}")
    private String key;

    @Value("${report.value}")
    private String value;

    public ReportExternalService(WebClient webClient) {
        this.webClient = webClient;
    }

    public byte[] getDocumento(ReportGfdDocumentoRequestDto input) {
        return webClient.post()
                .uri(url + "/gfd/v1/documentos")
                .headers(header -> header.set(key, value))
                .bodyValue(input)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .map(body -> new RuntimeException(REPORT_EXTERNAL_ERROR.getMensagem() + body))
                )
                .bodyToMono(byte[].class)
                .block();
    }
}
