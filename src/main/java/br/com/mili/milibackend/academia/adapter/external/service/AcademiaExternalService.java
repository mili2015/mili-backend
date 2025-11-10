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
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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


    public Optional<AcademiaExternalGetUserByEmailResponse> getUserByEmailResponse(String email) {
        var userArray = webClient.get()
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

        return webClient.post()
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

    public AcademiaExternalSaveUserResponse updateUser(AcademiaExternalUpdateUserRequest input) {
        return webClient.post()
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

        return webClient.post()
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

        return webClient.get()
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

}
