package com.example.orcestr3api;

import com.example.orcestr3api.dto.*;
import com.example.orcestr3api.service.KeycloakService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.keycloak.OAuth2Constants.PASSWORD;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.USERNAME;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class KeycloakServiceTest {

    @InjectMocks
    private KeycloakService keycloakService;

    private static final String USER_ID = "be208d21-40d7-42b1-a2d1-75d54f5e62b1";
    private static final String TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJJVWxjOFJ0WDdiTkJSclRvX1FyRXA5b3NBajl2eDU2MHJESElfbUk4VTRnIn0.eyJleHAiOjE3MzMwNjQ5ODQsImlhdCI6MTczMzA2NDkyNCwianRpIjoiNTIwNTYzYWEtMzRkZC00ZjRiLTljOWEtMDE4ZGE4YjQ3YWRlIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL3JlYWxtcy9tYXN0ZXIiLCJhdWQiOlsib3JjZXN0cjMtYXBpLXJlYWxtIiwic3ByaW5nLWFwcC1yZWFsbSIsIm1hc3Rlci1yZWFsbSIsImFjY291bnQiLCJ1c2Vycy1yZWFsbSJdLCJzdWIiOiI1ZTNiNzJiYS1hMjM0LTQ2MDYtYjM4NS1kNDhkMTczYzE5ZTIiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJteS1hcHAiLCJzaWQiOiIzNTY5NDAyNi0wYmUzLTQyZGEtYjI0Mi02MWMxZDg5MjI5ZTYiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly9sb2NhbGhvc3Q6ODA4MCJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiY3JlYXRlLXJlYWxtIiwiZGVmYXVsdC1yb2xlcy1tYXN0ZXIiLCJvZmZsaW5lX2FjY2VzcyIsImFkbWluIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJvcmNlc3RyMy1hcGktcmVhbG0iOnsicm9sZXMiOlsidmlldy1yZWFsbSIsInZpZXctaWRlbnRpdHktcHJvdmlkZXJzIiwibWFuYWdlLWlkZW50aXR5LXByb3ZpZGVycyIsImltcGVyc29uYXRpb24iLCJjcmVhdGUtY2xpZW50IiwibWFuYWdlLXVzZXJzIiwicXVlcnktcmVhbG1zIiwidmlldy1hdXRob3JpemF0aW9uIiwicXVlcnktY2xpZW50cyIsInF1ZXJ5LXVzZXJzIiwibWFuYWdlLWV2ZW50cyIsIm1hbmFnZS1yZWFsbSIsInZpZXctZXZlbnRzIiwidmlldy11c2VycyIsInZpZXctY2xpZW50cyIsIm1hbmFnZS1hdXRob3JpemF0aW9uIiwibWFuYWdlLWNsaWVudHMiLCJxdWVyeS1ncm91cHMiXX0sInNwcmluZy1hcHAtcmVhbG0iOnsicm9sZXMiOlsidmlldy1yZWFsbSIsInZpZXctaWRlbnRpdHktcHJvdmlkZXJzIiwibWFuYWdlLWlkZW50aXR5LXByb3ZpZGVycyIsImltcGVyc29uYXRpb24iLCJjcmVhdGUtY2xpZW50IiwibWFuYWdlLXVzZXJzIiwicXVlcnktcmVhbG1zIiwidmlldy1hdXRob3JpemF0aW9uIiwicXVlcnktY2xpZW50cyIsInF1ZXJ5LXVzZXJzIiwibWFuYWdlLWV2ZW50cyIsIm1hbmFnZS1yZWFsbSIsInZpZXctZXZlbnRzIiwidmlldy11c2VycyIsInZpZXctY2xpZW50cyIsIm1hbmFnZS1hdXRob3JpemF0aW9uIiwibWFuYWdlLWNsaWVudHMiLCJxdWVyeS1ncm91cHMiXX0sIm1hc3Rlci1yZWFsbSI6eyJyb2xlcyI6WyJ2aWV3LWlkZW50aXR5LXByb3ZpZGVycyIsInZpZXctcmVhbG0iLCJtYW5hZ2UtaWRlbnRpdHktcHJvdmlkZXJzIiwiaW1wZXJzb25hdGlvbiIsImNyZWF0ZS1jbGllbnQiLCJtYW5hZ2UtdXNlcnMiLCJxdWVyeS1yZWFsbXMiLCJ2aWV3LWF1dGhvcml6YXRpb24iLCJxdWVyeS1jbGllbnRzIiwicXVlcnktdXNlcnMiLCJtYW5hZ2UtZXZlbnRzIiwibWFuYWdlLXJlYWxtIiwidmlldy1ldmVudHMiLCJ2aWV3LXVzZXJzIiwidmlldy1jbGllbnRzIiwibWFuYWdlLWF1dGhvcml6YXRpb24iLCJtYW5hZ2UtY2xpZW50cyIsInF1ZXJ5LWdyb3VwcyJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19LCJ1c2Vycy1yZWFsbSI6eyJyb2xlcyI6WyJ2aWV3LXJlYWxtIiwidmlldy1pZGVudGl0eS1wcm92aWRlcnMiLCJtYW5hZ2UtaWRlbnRpdHktcHJvdmlkZXJzIiwiaW1wZXJzb25hdGlvbiIsImNyZWF0ZS1jbGllbnQiLCJtYW5hZ2UtdXNlcnMiLCJxdWVyeS1yZWFsbXMiLCJ2aWV3LWF1dGhvcml6YXRpb24iLCJxdWVyeS1jbGllbnRzIiwicXVlcnktdXNlcnMiLCJtYW5hZ2UtZXZlbnRzIiwibWFuYWdlLXJlYWxtIiwidmlldy1ldmVudHMiLCJ2aWV3LXVzZXJzIiwidmlldy1jbGllbnRzIiwibWFuYWdlLWF1dGhvcml6YXRpb24iLCJtYW5hZ2UtY2xpZW50cyIsInF1ZXJ5LWdyb3VwcyJdfX0sInNjb3BlIjoiZW1haWwgcHJvZmlsZSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhZG1pbkBleGFtcGxlLmNvbSIsImVtYWlsIjoiYWRtaW5AZXhhbXBsZS5jb20ifQ.Mu88AG7Ia7Ev_0bQLZh0zZxH-Vm0ZgghaJMafhm54sWd-a5pdsLor5qtPpzx3R9vlRqbpenUVVPc__yI93QICvVjLVPfrk9Cs9seMxo7kBde6XkpZSmRgJxn0Qqb3eP6W1tER1vBKftxt7d8LAi97Be7IxMTtonJ2z8xwr2Qbn4RwxDQ2PWoC_tAiwwRA-D77AVi3ismECRbfqCa7NJdksG_eb45k_JUJshiOu-AADxlTUu00LIHe0L5jnFpqzlRerWLxmcthQhuHGPDf37fbO1G_nTNO5AridQzw4H2XRgflKCiExUbJt2DZ7d7vQB-q-dXI7_qWn_tW7Q0y8me2g";
    private static final String REFRESH_TOKEN = "eyJhbGciOiJIUzUxMiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJlY2UwNzhhZi1iYTU2LTQxZWItYjIzZS0xNzU1ODU1MmRiYzYifQ.eyJleHAiOjE3MzMwNjY3MjQsImlhdCI6MTczMzA2NDkyNCwianRpIjoiODViNjdiOGItYWFkNi00OGI3LTgzNDYtMDE0ZWUxZmM0ODI1IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL3JlYWxtcy9tYXN0ZXIiLCJhdWQiOiJodHRwOi8vbG9jYWxob3N0OjgwODAvcmVhbG1zL21hc3RlciIsInN1YiI6IjVlM2I3MmJhLWEyMzQtNDYwNi1iMzg1LWQ0OGQxNzNjMTllMiIsInR5cCI6IlJlZnJlc2giLCJhenAiOiJteS1hcHAiLCJzaWQiOiIzNTY5NDAyNi0wYmUzLTQyZGEtYjI0Mi02MWMxZDg5MjI5ZTYiLCJzY29wZSI6ImFjciBlbWFpbCBwcm9maWxlIHJvbGVzIHdlYi1vcmlnaW5zIGJhc2ljIn0.bDj1-Q9jHEWoLCbKDbewAx_Ky6YcX3NWqPAgK3cSFRRyCpVQ7I7QY85w4Ez3V52GIyVPUFOjcYVUFgdFA_tlDg";


    @Test
    void registerUser() throws JsonProcessingException {

        String TOKEN = "mock-token";
        String EMAIL = "test@example.com";
        String PASSWORD = "password123";

        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setEmail(EMAIL);
        request.setPassword(PASSWORD);
        request.setToken(TOKEN);

    }

    @Test
    void testRefreshTokenSuccess() {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        refreshTokenRequest.setRefreshToken(REFRESH_TOKEN);

        String mockResponse = "{\"access_token\":\"mock-access-token\", \"expires_in\":3600}";

        Mono<AccessTokenResponse> result = keycloakService.refreshToken(refreshTokenRequest);

        AccessTokenResponse responseEntity = result.block();
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatus());
        assertEquals(mockResponse, responseEntity.getTokenType());
    }

    @Test
    void refreshToken() {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        refreshTokenRequest.setRefreshToken(REFRESH_TOKEN);

        Mono<AccessTokenResponse> result = keycloakService.refreshToken(refreshTokenRequest);
        assertThrows(RuntimeException.class, result::block);
    }

    @Test
    void getAccessToken() {

        Mono<AccessTokenResponse> result = keycloakService.getAccessAdminToken();
        assertThrows(RuntimeException.class, result::block);
    }

    @Test
    void login() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword(PASSWORD);
        loginRequest.setUserName(USERNAME);

        Mono<AccessTokenResponse> result = keycloakService.login(loginRequest);
        assertThrows(RuntimeException.class, result::block);
    }

    @Test
    void getUserInfo() {

        String email = "{\"id\":\"be208d21-40d7-42b1-a2d1-75d54f5e62b1\",\"username\":\"test@example.com\",\"firstName\":\"Samon\",\"lastName\":\"Pix\",\"email\":\"test@example.com\",\"emailVerified\":true,\"createdTimestamp\":1732622504326,\"enabled\":true,\"totp\":false,\"disableableCredentialTypes\":[],\"requiredActions\":[],\"notBefore\":0,\"access\":{\"manageGroupMembership\":true,\"view\":true,\"mapRoles\":true,\"impersonate\":true,\"manage\":true}}";

        Mono<UserInfo> result = keycloakService.getUserInfo(USER_ID, TOKEN);

        UserInfo responseEntity = result.block();
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatus());
        assertEquals(email, responseEntity.getEmail());
    }

    @Test
    void testGetUserInfoUnauthorized() {

        Mono<UserInfo> result = keycloakService.getUserInfo(USER_ID, TOKEN);
        assertThrows(RuntimeException.class, result::block);
    }
}