package com.example.orcestr3api.controllerAnnotation;


import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация RegisterApiResponses описывает набор ответов при получении данных пользователя
 */
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Данные пользователя успешно получены",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(type = "object"))),
        @ApiResponse(responseCode = "401", description = "Неверный или истекший токен доступа"),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден")
})
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MeApiResponse {
}
