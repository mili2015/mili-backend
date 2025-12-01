package br.com.mili.milibackend.academia.adapter.external.service;

import br.com.mili.milibackend.academia.adapter.external.dto.*;
import br.com.mili.milibackend.shared.exception.types.InternalServerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static br.com.mili.milibackend.academia.adapter.external.exceptions.AcademiaExternalCodeException.ACADEMIA_EXTERNAL_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
public class AcademiaExternalService {

    private final WebClient webClient;

    @Value("${academia.wp.base}")
    private String wpBase;

    @Value("${academia.wp.user}")
    private String wpUser;

    @Value("${academia.wp.password}")
    private String wpAppPassword;

    @Value("${academia.wp.sendPlainPass:true}")
    private boolean sendPlainPass;


    private String wordpressApi(String path) {
        return wpBase.replaceAll("/$", "") + "/wp-json/wp/v2" + path;
    }

    private String ldApi(String path) {
        return wpBase.replaceAll("/$", "") + "/wp-json/ldlms/v2" + path;
    }


    private WebClient webClientWithLog() {
        return WebClient.builder()
                .filter(logResponse())
                .build();
    }

    public Optional<AcademiaExternalGetUserByEmailResponse> getUserByEmailResponse(String email) {
        var userArray = webClientWithLog()
                .get()
                .uri(wordpressApi("/users?search=" + email))
                .headers(h -> h.setBasicAuth(wpUser, wpAppPassword))
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .map(body -> new InternalServerException(ACADEMIA_EXTERNAL_ERROR.getMensagem() + body, ACADEMIA_EXTERNAL_ERROR.getCode()))
                )
                .bodyToMono(new ParameterizedTypeReference<List<AcademiaExternalGetUserByEmailResponse>>() {
                })
                .block();


        if (userArray == null || userArray.isEmpty()) {
            return Optional.empty();
        }

        return Optional.ofNullable(userArray.get(0));
    }

    public AcademiaExternalSaveUserResponse createUser(AcademiaExternalCreateUserRequest input) {

        return webClientWithLog()
                .post()
                .uri(wordpressApi("/users"))
                .headers(h -> h.setBasicAuth(wpUser, wpAppPassword))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(input)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .map(body -> new InternalServerException("WORDPRESS" + " " + body, ACADEMIA_EXTERNAL_ERROR.getCode()))
                )
                .bodyToMono(AcademiaExternalSaveUserResponse.class)
                .block();
    }

    public void updateMetaPlainPass(Integer userId, String plainPass) {
        var meta = new HashMap<String, String>();
        meta.put("import_plain_pass", plainPass);

        var payload = new HashMap<String, Object>();
        payload.put("meta", meta);

        webClientWithLog()
                .put()
                .uri(wordpressApi("/users/" + userId))
                .headers(h -> h.setBasicAuth(wpUser, wpAppPassword))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .map(body -> new InternalServerException("WORDPRESS" + " " + body, ACADEMIA_EXTERNAL_ERROR.getCode()))
                )
                .bodyToMono(AcademiaExternalSaveUserResponse.class)
                .block();
    }


    public AcademiaExternalSaveUserResponse updateUser(AcademiaExternalUpdateUserRequest input) {
        return webClientWithLog()
                .post()
                .uri(wordpressApi("/users/" + input.getUserId()))
                .headers(h -> h.setBasicAuth(wpUser, wpAppPassword))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(input)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .map(body -> new InternalServerException("WORDPRESS" + " " + body, ACADEMIA_EXTERNAL_ERROR.getCode()))
                )
                .bodyToMono(AcademiaExternalSaveUserResponse.class)
                .block();
    }

    public List<AcademiaExternalMatricularUserResponse> matricularUser(int userId, List<Integer> courseIds) {
        var payload = AcademiaExternalMatricularUser.builder()
                .courseIds(courseIds)
                .build();

        return webClientWithLog()
                .post()
                .uri(ldApi("/users/" + userId + "/courses"))
                .headers(h -> h.setBasicAuth(wpUser, wpAppPassword))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .retrieve()
                .onStatus(HttpStatusCode::isError, resp ->
                        resp.bodyToMono(String.class)
                                .map(body -> new InternalServerException(ACADEMIA_EXTERNAL_ERROR.getMensagem() + body, "GET_COURSES_BY_USER")))
                .bodyToMono(new ParameterizedTypeReference<List<AcademiaExternalMatricularUserResponse>>() {
                })
                .block();
    }

    public List<AcademiaExternalGetCousesByIdResponse> getCoursesByUser(int userId, List<Integer> courseIds) {
        StringBuilder uriBuilder = new StringBuilder("/users/")
                .append(userId)
                .append("/courses");

        if (courseIds != null && !courseIds.isEmpty()) {
            String joinedIds = courseIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            uriBuilder.append("?include=").append(joinedIds);
        }

        return webClientWithLog()
                .get()
                .uri(ldApi(uriBuilder.toString()))
                .headers(h -> h.setBasicAuth(wpUser, wpAppPassword))
                .retrieve()
                .onStatus(HttpStatusCode::isError, resp ->
                        resp.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new InternalServerException(
                                        ACADEMIA_EXTERNAL_ERROR.getMensagem() + body, "GET_COURSES_BY_USER"))))
                .bodyToMono(new ParameterizedTypeReference<List<AcademiaExternalGetCousesByIdResponse>>() {
                })
                .block();
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(response ->
                response.bodyToMono(String.class)
                        .defaultIfEmpty("")
                        .flatMap(body -> {
                            var requestUri = response.request().getURI();
                            var method = response.request().getMethod();

                            log.info("RESPONSE {} {} => Status: {} BODY: {}",
                                    method, requestUri, response.statusCode(), body
                            );

                            // reconstruir o ClientResponse com o body para não consumir o stream
                            ClientResponse newResponse = ClientResponse.from(response)
                                    .body(body)
                                    .build();

                            return Mono.just(newResponse);
                        })
        );
    }

}
