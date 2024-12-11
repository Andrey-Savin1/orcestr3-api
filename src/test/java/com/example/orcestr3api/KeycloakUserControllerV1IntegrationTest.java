package com.example.orcestr3api;


import com.example.orcestr3api.dto.AccessTokenResponse;
import com.example.orcestr3api.dto.UserRegistrationRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@Slf4j
public class KeycloakUserControllerV1IntegrationTest extends BaseTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void registerUser() {
        UserRegistrationRequest registrationRequest = new UserRegistrationRequest();

        registrationRequest.setEmail("test@example.com");
        registrationRequest.setPassword("0000");
        registrationRequest.setToken("eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJJVWxjOFJ0WDdiTkJSclRvX1FyRXA5b3NBajl2eDU2MHJESElfbUk4VTRnIn0.eyJleHAiOjE3MzM4NTkwMTcsImlhdCI6MTczMzg1ODk1NywianRpIjoiMzRiY2EwNDgtYmNmYS00ODJhLWE2YjctMTRiNDk5OTZiMTYzIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL3JlYWxtcy9tYXN0ZXIiLCJhdWQiOlsibWFzdGVyLXJlYWxtIiwiYWNjb3VudCJdLCJzdWIiOiI1ZTNiNzJiYS1hMjM0LTQ2MDYtYjM4NS1kNDhkMTczYzE5ZTIiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJteS1hcHAiLCJzaWQiOiIwYTA5NzAwMS0wYTIxLTQxNjktOGRiYy03MTM5ZTk3ODY4MTIiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly9sb2NhbGhvc3Q6ODA4MCJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiY3JlYXRlLXJlYWxtIiwiZGVmYXVsdC1yb2xlcy1tYXN0ZXIiLCJvZmZsaW5lX2FjY2VzcyIsImFkbWluIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJtYXN0ZXItcmVhbG0iOnsicm9sZXMiOlsidmlldy1pZGVudGl0eS1wcm92aWRlcnMiLCJ2aWV3LXJlYWxtIiwibWFuYWdlLWlkZW50aXR5LXByb3ZpZGVycyIsImltcGVyc29uYXRpb24iLCJjcmVhdGUtY2xpZW50IiwibWFuYWdlLXVzZXJzIiwicXVlcnktcmVhbG1zIiwidmlldy1hdXRob3JpemF0aW9uIiwicXVlcnktY2xpZW50cyIsInF1ZXJ5LXVzZXJzIiwibWFuYWdlLWV2ZW50cyIsIm1hbmFnZS1yZWFsbSIsInZpZXctZXZlbnRzIiwidmlldy11c2VycyIsInZpZXctY2xpZW50cyIsIm1hbmFnZS1hdXRob3JpemF0aW9uIiwibWFuYWdlLWNsaWVudHMiLCJxdWVyeS1ncm91cHMiXX0sImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoiZW1haWwgcHJvZmlsZSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhZG1pbkBleGFtcGxlLmNvbSIsImVtYWlsIjoiYWRtaW5AZXhhbXBsZS5jb20ifQ.en5VyQLrAa_F7sBHUrMUvdoxA1r7S78L6m-eHD6K3-6ek1dldlsa0_mqZkq4xyE1q2f6f-H2BwiSbZuR1_RZ_E06LCxX7e9Uyo6cSkExcAe-Pv-l4-v8yggI5fCNVjE6yCeBIWytDZanxnsDMcdlo-hxg8VoBBaY1mkXR5B60mvFH619QsE4hF_ppOim5Sdq8VNLzikiwDquBb2d7cdAKH7go7cojfxr8soiCBWQUMkzCU6aa-Nq7D18OlMTNwvCTLxsC7nVj8vJINJhMRZcRVCEyFJ8nAio_1sj1T8nBSj0Z2BAy_zS-8WTVtPCaTGZ9Qw5xeavODknJ5k4uHDtTA");

        webTestClient.post()
                .uri("/api/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(registrationRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AccessTokenResponse.class)
                .consumeWith(response -> {
                    Assertions.assertNotNull(response.getResponseBody());
                    Assertions.assertNotNull(response.getResponseBody().getAccessToken());
                });
    }

    @Test
    void  getAccessToken() {

        webTestClient.post()
                .uri("/api/v1/access-token")
                .exchange()
                .expectStatus().isOk()
                .expectBody(AccessTokenResponse.class)
                .consumeWith(response -> {
                    Assertions.assertNotNull(response.getResponseBody());
                    Assertions.assertNotNull(response.getResponseBody().getAccessToken());
                });
    }

    @Test
    void  getRefreshToken() {

        webTestClient.post()
                .uri("/api/v1/refresh-token")
                .exchange()
                .expectStatus().isOk()
                .expectBody(AccessTokenResponse.class)
                .consumeWith(response -> {
                    Assertions.assertNotNull(response.getResponseBody());
                    Assertions.assertNotNull(response.getResponseBody().getRefreshToken());
                });
    }

    @Test
    void  getLogin() {

        webTestClient.post()
                .uri("/api/v1/login")
                .exchange()
                .expectStatus().isOk()
                .expectBody(AccessTokenResponse.class)
                .consumeWith(response -> {
                    Assertions.assertNotNull(response.getResponseBody());
                    Assertions.assertNotNull(response.getResponseBody().getRefreshToken());
                });
    }


}
