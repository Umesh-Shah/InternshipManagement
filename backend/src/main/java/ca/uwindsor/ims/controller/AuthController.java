package ca.uwindsor.ims.controller;

import ca.uwindsor.ims.dto.LoginRequest;
import ca.uwindsor.ims.dto.LoginResponse;
import ca.uwindsor.ims.logging.AuditLogger;
import ca.uwindsor.ims.service.AuthResult;
import ca.uwindsor.ims.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    public static final String COOKIE_NAME = "ims-jwt";
    private static final Duration COOKIE_MAX_AGE = Duration.ofHours(8);
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;
    private final boolean cookieSecure;

    public AuthController(AuthService authService,
                          @Value("${jwt.cookie.secure:false}") boolean cookieSecure) {
        this.authService = authService;
        this.cookieSecure = cookieSecure;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletRequest httpRequest,
            HttpServletResponse response) {
        String username = loginRequest.username();
        String ip = httpRequest.getRemoteAddr();
        log.info("Login attempt for user: {}", username);
        try {
            AuthResult result = authService.login(loginRequest);
            log.info("Login successful for user: {}", username);
            AuditLogger.logLogin(username, true, ip);
            addJwtCookie(response, result.token(), COOKIE_MAX_AGE);
            return ResponseEntity.ok(result.user());
        } catch (RuntimeException ex) {
            log.warn("Login failed for user: {}", username);
            AuditLogger.logLogin(username, false, ip);
            throw ex;
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(Authentication authentication, HttpServletResponse response) {
        String username = authentication != null ? authentication.getName() : "unknown";
        log.info("User logged out: {}", username);
        AuditLogger.logLogout(username);
        addJwtCookie(response, "", Duration.ZERO);
        return ResponseEntity.noContent().build();
    }

    private void addJwtCookie(HttpServletResponse response, String value, Duration maxAge) {
        ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, value)
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .maxAge(maxAge)
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
