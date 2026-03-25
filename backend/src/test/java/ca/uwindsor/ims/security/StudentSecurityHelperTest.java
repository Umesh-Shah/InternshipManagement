package ca.uwindsor.ims.security;

import ca.uwindsor.ims.Constants;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StudentSecurityHelperTest {

    private final StudentSecurityHelper helper = new StudentSecurityHelper();

    @Test
    void canAccess_adminToken_returnsTrue() {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                "admin", null,
                List.of(new SimpleGrantedAuthority(Role.ROLE_ADMIN.authority())));

        assertThat(helper.canAccess(auth, 1001)).isTrue();
    }

    @Test
    void canAccess_studentToken_ownId_returnsTrue() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaim(Constants.JWT_CLAIM_STUDENT_ID)).thenReturn(1001);
        JwtAuthenticationToken token = new JwtAuthenticationToken(jwt,
                List.of(new SimpleGrantedAuthority(Role.ROLE_STUDENT.authority())));

        assertThat(helper.canAccess(token, 1001)).isTrue();
    }

    @Test
    void canAccess_studentToken_otherId_returnsFalse() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaim(Constants.JWT_CLAIM_STUDENT_ID)).thenReturn(1001);
        JwtAuthenticationToken token = new JwtAuthenticationToken(jwt,
                List.of(new SimpleGrantedAuthority(Role.ROLE_STUDENT.authority())));

        assertThat(helper.canAccess(token, 9999)).isFalse();
    }
}
