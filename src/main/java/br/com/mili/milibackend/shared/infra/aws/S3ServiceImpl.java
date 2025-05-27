package br.com.mili.milibackend.shared.infra.aws;

import br.com.mili.milibackend.shared.exception.types.InternalServerException;
import br.com.mili.milibackend.shared.infra.aws.exception.S3ExceptionCode;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class S3ServiceImpl implements IS3Service {

    private final String url;
    private final String tokenName;
    private final String tokenValue;
    private final WebClient webClient;

    public S3ServiceImpl(
            @Value("${http.s3.auth.tokenName}") String tokenName,
            @Value("${http.s3.auth.tokenValue}") String tokenValue,
            @Value("${http.s3.url}") String url,

            WebClient webClient
    ) {
        this.tokenName = tokenName;
        this.tokenValue = tokenValue;
        this.url = url;
        this.webClient = webClient;
    }


    @Override
    public void upload(StorageFolderEnum folder, String json) {
        webClient.post()
                .uri(url + folder.toString())
                .header(tokenName, tokenValue)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(json)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .map(body -> new InternalServerException(S3ExceptionCode.S3_FALHA.getMensagem() + " " + body, S3ExceptionCode.S3_FALHA.getCode()))
                )
                .toBodilessEntity()
                .block();
    }

    @Override
    public String getPresignedUrl(StorageFolderEnum folder, String filename) {
        return webClient.get()
                .uri(url + folder.toString() + "?key=" + filename)
                .header(tokenName, tokenValue)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .map(body -> new InternalServerException(S3ExceptionCode.S3_FALHA.getMensagem() + " " + body, S3ExceptionCode.S3_FALHA.getCode()))
                )
                .bodyToMono(String.class)
                .block();
    }
}
