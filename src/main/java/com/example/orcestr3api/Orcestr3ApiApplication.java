package com.example.orcestr3api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "orcestr3-api",
                version = "1.0",
                description = " API для регистрации, аутентификации, обновления токенов и получения данных пользователей."
        )
)
public class Orcestr3ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(Orcestr3ApiApplication.class, args);
    }

}
