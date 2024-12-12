package com.example.orcestr3api;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
 public abstract class BaseTest {
    private static final String KEYCLOAK_IMAGE = "quay.io/keycloak/keycloak:26.0.5";
    private static final String REALM_EXPORT_JSON = "/realm-export-2.json";

    @Container
    public static KeycloakContainer keycloak;

    static {
        keycloak = new KeycloakContainer(KEYCLOAK_IMAGE)
                .withAdminUsername("admin")
                .withAdminPassword("admin")
                .withRealmImportFile(REALM_EXPORT_JSON);
        keycloak.start();
    }

    @DynamicPropertySource
    static void keycloakProperties(DynamicPropertyRegistry registry) {
        log.info("keycloak.getFirstMappedPort()={}", keycloak.getAuthServerUrl());
        registry.add("keycloak.urls.auth", keycloak::getAuthServerUrl);
        registry.add("keycloak.realm", () -> "master");
        registry.add("keycloak.clientId", () -> "my-app");
        registry.add("keycloak.clientSecret", () -> "IKNMPbG8i2vxhsxc62E4ADP1hFPEs2yx");
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri", () -> keycloak.getAuthServerUrl() + "/realms/master");
        registry.add("spring.security.oauth2.resourceserver.jwt.jwk-set-uri", () -> keycloak.getAuthServerUrl() + "/realms/master/protocol/openid-connect/certs");
        registry.add("keycloak-service.client.base-admin-url", () -> keycloak.getAuthServerUrl() + "/admin/realms/master");
    }

}

