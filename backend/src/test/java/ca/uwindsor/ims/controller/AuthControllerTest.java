package ca.uwindsor.ims.controller;

import ca.uwindsor.ims.dto.LoginRequest;
import ca.uwindsor.ims.dto.LoginResponse;
import ca.uwindsor.ims.service.AuthResult;
import ca.uwindsor.ims.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller slice test — security is excluded here so we can focus on
 * request/response handling and bean validation without needing a JWT setup.
 * Spring Security integration is covered by the running application.
 */
@WebMvcTest(
        value = AuthController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class,
                OAuth2ResourceServerAutoConfiguration.class
        }
)
class AuthControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    @MockBean AuthService authService;

    @Test
    void loginSuccess_setsCookieAndReturnsUserWithoutToken() throws Exception {
        AuthResult result = new AuthResult("jwt-token",
                new LoginResponse("ROLE_ADMIN", null, "admin"));
        when(authService.login(any())).thenReturn(result);

        mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new LoginRequest("admin", "secret"))))
                .andExpect(status().isOk())
                .andExpect(header().string("Set-Cookie", containsString("ims-jwt=jwt-token")))
                .andExpect(header().string("Set-Cookie", containsString("HttpOnly")))
                .andExpect(header().string("Set-Cookie", containsString("SameSite=Strict")))
                .andExpect(jsonPath("$.role").value("ROLE_ADMIN"))
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(jsonPath("$.token").doesNotExist());
    }

    @Test
    void logout_clearsCookie() throws Exception {
        mvc.perform(post("/api/auth/logout"))
                .andExpect(status().isNoContent())
                .andExpect(header().string("Set-Cookie", containsString("ims-jwt=")))
                .andExpect(header().string("Set-Cookie", containsString("Max-Age=0")));
    }

    @Test
    void loginBadCredentials_returns401() throws Exception {
        when(authService.login(any())).thenThrow(new BadCredentialsException("bad credentials"));

        mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new LoginRequest("admin", "wrong"))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginBlankUsername_returns400() throws Exception {
        mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"username":"","password":"pass"}
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginMissingBody_returns400() throws Exception {
        mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
