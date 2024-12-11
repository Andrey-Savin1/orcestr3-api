package com.example.orcestr3api.service;

import com.example.orcestr3api.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class KeycloakService {

    private WebClient webClient;

    private static final String KEYCLOAK_TOKEN_URL = "http://localhost:8080/realms/master/protocol/openid-connect/token";
    private static final String CREATE_USER_URL = "http://localhost:8080/admin/realms/master/users";
    private static final String GET_USER_INFO = "http://localhost:8080/admin/realms/master/users/";


    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    @Value("${keycloak.username}")
    private String username;

    @Value("${keycloak.password}")
    private String password;


    public Mono<AccessTokenResponse> registerUser(UserRegistrationRequest request) throws JsonProcessingException {

        webClient = WebClient.builder().build();

        Map<String, Object> data = new HashMap<>();
        data.put("email", request.getEmail());
        data.put("enabled", true);
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("type", "password");
        credentials.put("value", request.getPassword());
        data.put("credentials", new Object[]{credentials});
        data.put("emailVerified", true);

        ObjectMapper objectMapper = new ObjectMapper();
        String rawJson = objectMapper.writeValueAsString(data);

        return webClient.post()
                .uri(CREATE_USER_URL)
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON.toString())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + request.getToken())
                .bodyValue(rawJson)
                .retrieve()
                .toEntity(String.class)
                .flatMap(this::getAccessTokenResponseMono)
                .onErrorResume(this::getClientError);
    }

    public Mono<AccessTokenResponse> refreshToken(RefreshTokenRequest token) {

        webClient = WebClient.builder().build();

        String requestBody = "grant_type=refresh_token&client_id=" + clientId
                + "&client_secret=" + clientSecret + "&refresh_token=" + token.getRefreshToken();

        return webClient.post()
                .uri(KEYCLOAK_TOKEN_URL)
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .bodyValue(requestBody)
                .retrieve()
                .toEntity(String.class)
                .flatMap(this::getAccessTokenResponseMono)
                .onErrorResume(this::getClientError);

    }

    public Mono<AccessTokenResponse> getAccessAdminToken() {
        webClient = WebClient.builder().build();

        String requestBody = "grant_type=password&client_id=" + clientId
                + "&client_secret=" + clientSecret + "&username=" + username + "&password=" + password;

        return webClient.post()
                .uri(KEYCLOAK_TOKEN_URL)
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .bodyValue(requestBody)
                .retrieve()
                .toEntity(String.class)
                .flatMap(this::getAccessTokenResponseMono)
                .onErrorResume(this::getClientError);
    }

    public Mono<AccessTokenResponse> login(LoginRequest request) {
        webClient = WebClient.builder().build();

        String requestBody = "grant_type=password&client_id=" + clientId
                + "&client_secret=" + clientSecret + "&username=" + request.getUserName() + "&password=" + request.getPassword();

        return webClient.post()
                .uri(KEYCLOAK_TOKEN_URL)
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .bodyValue(requestBody)
                .retrieve()
                .toEntity(String.class)
                .flatMap(this::getAccessTokenResponseMono)
                .onErrorResume(this::getClientError);
    }

    public Mono<UserInfo> getUserInfo(String userId, String token) {

        String tok = "Bearer " + token;
        webClient = WebClient.builder().build();

        return webClient.get()
                .uri(GET_USER_INFO + userId)
                .header(HttpHeaders.AUTHORIZATION, tok)
                .retrieve()
                .toEntity(String.class)
                .flatMap(response -> {
                    UserInfo userInfo = new UserInfo();

                    var time = extractBodyFromResponse(response.getBody(), "createdTimestamp");
                    Instant instant = Instant.ofEpochMilli(Long.parseLong(time));
                    LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();

                    if (response.getStatusCode().is2xxSuccessful()) {
                        userInfo.setId(extractBodyFromResponse(response.getBody(), "id"));
                        userInfo.setEmail(extractBodyFromResponse(response.getBody(), "email"));
                        userInfo.setCreatedAt(localDateTime.toString());

                        return Mono.just(userInfo);
                    } else if (response.getStatusCode().is4xxClientError()) {
                        userInfo.setError(extractBodyFromResponse(response.getBody(), "error"));
                        userInfo.setStatus(extractBodyFromResponse(response.getBody(), "status"));
                        return Mono.just(userInfo);
                    }
                    return Mono.just(userInfo);

                })
                .onErrorResume(e -> {
                    UserInfo userInfo = new UserInfo();
                    userInfo.setError("Client error: " + e.getMessage());
                    userInfo.setStatus("CLIENT_ERROR");
                    return Mono.just(userInfo);
                });

    }

    private Mono<AccessTokenResponse> getClientError(Throwable e) {
        AccessTokenResponse errorResponse = new AccessTokenResponse();
        errorResponse.setError("Client error: " + e.getMessage());
        errorResponse.setStatus("CLIENT_ERROR");
        return Mono.just(errorResponse);
    }

    private Mono<AccessTokenResponse> getAccessTokenResponseMono(ResponseEntity<String> response) {
        AccessTokenResponse tokenResponse = new AccessTokenResponse();
        if (response.getStatusCode().is2xxSuccessful()) {
            tokenResponse.setAccessToken(extractBodyFromResponse(response.getBody(), "access_token"));
            tokenResponse.setRefreshToken(extractBodyFromResponse(response.getBody(), "refresh_token"));
            tokenResponse.setTokenType(extractBodyFromResponse(response.getBody(), "token_type"));
            tokenResponse.setExpiresIn(extractBodyFromResponse(response.getBody(), "expires_in"));

            return Mono.just(tokenResponse);
        } else if (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()) {
            tokenResponse.setError(extractBodyFromResponse(response.getBody(), "error"));
            tokenResponse.setStatus(extractBodyFromResponse(response.getBody(), "status"));
            return Mono.just(tokenResponse);
        }
        return Mono.just(tokenResponse);
    }

    private String extractBodyFromResponse(String responseBody, String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            return jsonNode.get(jsonResponse).asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse token from response", e);
        }
    }
}
