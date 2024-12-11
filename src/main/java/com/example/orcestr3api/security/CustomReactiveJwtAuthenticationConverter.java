package com.example.orcestr3api.security;


import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.stream.Collectors;


@Component
public class CustomReactiveJwtAuthenticationConverter {

    private final ReactiveJwtAuthenticationConverter jwtAuthenticationConverter;
    private final JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();

    public CustomReactiveJwtAuthenticationConverter() {
        this.jwtAuthenticationConverter = new ReactiveJwtAuthenticationConverter();
    }

    public Mono<AbstractAuthenticationToken> convert(Jwt jwt) {

        Collection<GrantedAuthority> authorities = authoritiesConverter.convert(jwt)
                .stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .collect(Collectors.toList());

        String principalClaimName = jwt.getClaimAsString("email");
        return Mono.just(new JwtAuthenticationToken(jwt, authorities, principalClaimName));
    }
}