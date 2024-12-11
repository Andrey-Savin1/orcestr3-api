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
 * Аннотация RegisterApiResponses описывает набор ответов для регистрации пользователя
 */
@ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Успешная регистрация",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(type = "object"))),
        @ApiResponse(responseCode = "400", description = "Ошибка валидации запроса"),
        @ApiResponse(responseCode = "409", description = "Конфликт - пользователь с таким email уже существует")
})
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RegisterApiResponse {
}
