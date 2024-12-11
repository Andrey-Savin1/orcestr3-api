package com.example.orcestr3api.controller;

import com.example.orcestr3api.controllerAnnotation.LoginApiResponse;
import com.example.orcestr3api.controllerAnnotation.MeApiResponse;
import com.example.orcestr3api.controllerAnnotation.RefreshTokenApiResponse;
import com.example.orcestr3api.controllerAnnotation.RegisterApiResponse;
import com.example.orcestr3api.dto.*;
import com.example.orcestr3api.service.KeycloakService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

    private final KeycloakService keycloakService;

    @Operation(
            summary = "Регистрация пользователя",
            description = "Регистрирует нового пользователя по email и паролю, возвращает токены доступа" )
    @RegisterApiResponse
    @PostMapping("/register")
    public Mono<AccessTokenResponse> register(@RequestBody UserRegistrationRequest request) throws JsonProcessingException {
        return keycloakService.registerUser(request);
    }

    @Operation(summary = "Аутентификация админа", description = "Получение токена админа")
    @RegisterApiResponse
    @PostMapping("/access-token")
    public Mono<AccessTokenResponse> getAccessAdminToken() {
        return keycloakService.getAccessAdminToken();
    }

    @Operation(summary = "Аутентификация пользователя", description = "Выполняет вход пользователя по email и паролю")
    @LoginApiResponse
    @PostMapping("/login")
    public Mono<AccessTokenResponse> login(@RequestBody LoginRequest request) {
        return keycloakService.login(request);
    }

    @Operation(summary = "Обновление токенов", description = "Обновляет токен доступа по refresh токену")
    @RefreshTokenApiResponse
    @PostMapping("/refresh-token")
    public Mono<AccessTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return keycloakService.refreshToken(request);
    }

    @Operation(summary = "Получение данных пользователя", description = "Возвращает информацию о пользователе по ID")
    @MeApiResponse
    @PostMapping("/users/{userId}")
    public Mono<UserInfo> getUserInfo(@RequestBody String token, @PathVariable String userId) {
        return keycloakService.getUserInfo(userId, token);
    }
}
