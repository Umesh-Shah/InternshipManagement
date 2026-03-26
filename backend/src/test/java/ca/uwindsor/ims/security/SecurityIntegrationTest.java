package ca.uwindsor.ims.security;

import ca.uwindsor.ims.config.SecurityConfig;
import ca.uwindsor.ims.controller.CompanyController;
import ca.uwindsor.ims.controller.StudentController;
import ca.uwindsor.ims.service.CompanyService;
import ca.uwindsor.ims.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.mock.web.MockCookie;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Verifies that Spring Security's filter chain and @PreAuthorize rules are
 * correctly enforced at the HTTP layer.  Uses a real SecurityConfig (not
 * excluded) and a mock JwtDecoder so tests control the token claims.
 */
@WebMvcTest(controllers = {CompanyController.class, StudentController.class})
@Import({SecurityConfig.class, StudentSecurityHelper.class})
class SecurityIntegrationTest {

    @Autowired MockMvc mvc;

    // SecurityConfig requires JwtDecoder and ImsUserDetailsService as beans
    @MockBean JwtDecoder jwtDecoder;
    @MockBean ImsUserDetailsService userDetailsService;

    @MockBean CompanyService companyService;
    @MockBean StudentService studentService;

    // ── 401 — no token ───────────────────────────────────────────────────────

    @Test
    void noToken_returns401() throws Exception {
        mvc.perform(get("/api/companies"))
                .andExpect(status().isUnauthorized());
    }

    // ── 403 — STUDENT token on admin-only endpoints ──────────────────────────

    @Test
    void studentToken_postCompany_returns403() throws Exception {
        when(jwtDecoder.decode(anyString())).thenReturn(studentJwt(1));

        mvc.perform(post("/api/companies")
                        .header("Authorization", "Bearer student-token")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void studentToken_getStudents_returns403() throws Exception {
        when(jwtDecoder.decode(anyString())).thenReturn(studentJwt(1));

        mvc.perform(get("/api/students")
                        .header("Authorization", "Bearer student-token"))
                .andExpect(status().isForbidden());
    }

    // ── 200 — ADMIN token on admin-only endpoint ─────────────────────────────

    @Test
    void adminToken_getStudents_returns200() throws Exception {
        when(jwtDecoder.decode(anyString())).thenReturn(adminJwt());
        when(studentService.findAll()).thenReturn(List.of());

        mvc.perform(get("/api/students")
                        .header("Authorization", "Bearer admin-token"))
                .andExpect(status().isOk());
    }

    // ── 200 — STUDENT token on shared (ADMIN|STUDENT) endpoint ───────────────

    @Test
    void studentToken_getCompanies_returns200() throws Exception {
        when(jwtDecoder.decode(anyString())).thenReturn(studentJwt(1));
        when(companyService.findAll()).thenReturn(List.of());

        mvc.perform(get("/api/companies")
                        .header("Authorization", "Bearer student-token"))
                .andExpect(status().isOk());
    }

    // ── Cross-student access (@studentSecurity.canAccess) ────────────────────

    @Test
    void studentToken_ownResource_accessGranted() throws Exception {
        // student_id=1 accessing /students/1 — security passes; 404 proves we
        // reached the service layer (not blocked at 403)
        when(jwtDecoder.decode(anyString())).thenReturn(studentJwt(1));
        when(studentService.getEducation(1)).thenReturn(Optional.empty());

        mvc.perform(get("/api/students/1/education")
                        .header("Authorization", "Bearer student-token"))
                .andExpect(status().isNotFound());
    }

    @Test
    void studentToken_otherStudentResource_returns403() throws Exception {
        // student_id=1 accessing /students/2 — cross-student access blocked
        when(jwtDecoder.decode(anyString())).thenReturn(studentJwt(1));

        mvc.perform(get("/api/students/2/education")
                        .header("Authorization", "Bearer student-token"))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminToken_anyStudentResource_accessGranted() throws Exception {
        // admin can access any student sub-resource; 404 proves security passed
        when(jwtDecoder.decode(anyString())).thenReturn(adminJwt());
        when(studentService.getEducation(2)).thenReturn(Optional.empty());

        mvc.perform(get("/api/students/2/education")
                        .header("Authorization", "Bearer admin-token"))
                .andExpect(status().isNotFound());
    }

    // ── Cookie BearerTokenResolver ───────────────────────────────────────────

    @Test
    void cookieToken_validImsJwtCookie_authenticates() throws Exception {
        when(jwtDecoder.decode("admin-token")).thenReturn(adminJwt());
        when(companyService.findAll()).thenReturn(List.of());

        mvc.perform(get("/api/companies")
                        .cookie(new MockCookie("ims-jwt", "admin-token")))
                .andExpect(status().isOk());
    }

    @Test
    void cookieToken_blankCookie_fallsBackToAuthorizationHeader() throws Exception {
        when(jwtDecoder.decode(anyString())).thenReturn(adminJwt());
        when(companyService.findAll()).thenReturn(List.of());

        mvc.perform(get("/api/companies")
                        .cookie(new MockCookie("ims-jwt", "  "))
                        .header("Authorization", "Bearer admin-token"))
                .andExpect(status().isOk());
    }

    @Test
    void cookieToken_wrongCookieName_fallsBackToAuthorizationHeader() throws Exception {
        when(jwtDecoder.decode(anyString())).thenReturn(adminJwt());
        when(companyService.findAll()).thenReturn(List.of());

        mvc.perform(get("/api/companies")
                        .cookie(new MockCookie("other-cookie", "admin-token"))
                        .header("Authorization", "Bearer admin-token"))
                .andExpect(status().isOk());
    }

    // ── JwtAuthenticationConverter ────────────────────────────────────────────

    @Test
    void jwtToken_missingRoleClaim_returns403() throws Exception {
        Jwt jwtWithoutRole = Jwt.withTokenValue("no-role-token")
                .header("alg", "HS256")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(1, ChronoUnit.HOURS))
                .build();
        when(jwtDecoder.decode(anyString())).thenReturn(jwtWithoutRole);

        mvc.perform(get("/api/companies")
                        .header("Authorization", "Bearer no-role-token"))
                .andExpect(status().isForbidden());
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private Jwt adminJwt() {
        return Jwt.withTokenValue("admin-token")
                .header("alg", "HS256")
                .claim("role", "ROLE_ADMIN")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(1, ChronoUnit.HOURS))
                .build();
    }

    private Jwt studentJwt(Integer studentId) {
        return Jwt.withTokenValue("student-token")
                .header("alg", "HS256")
                .claim("role", "ROLE_STUDENT")
                .claim("student_id", studentId)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(1, ChronoUnit.HOURS))
                .build();
    }
}
