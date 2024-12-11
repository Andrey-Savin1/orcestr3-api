package com.example.orcestr3api.security;


import com.example.orcestr3api.security.CustomReactiveJwtAuthenticationConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@Configuration
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomReactiveJwtAuthenticationConverter jwtConverter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf().disable()
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/**").permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(customizer -> {
                    customizer.jwt(jwtSpec -> {
                        jwtSpec.jwtAuthenticationConverter(jwtConverter::convert);
                    });
                })
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance());

        return http.build();
    }
}
