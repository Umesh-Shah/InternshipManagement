package ca.uwindsor.ims.service;

import ca.uwindsor.ims.Constants;
import ca.uwindsor.ims.config.AppProperties;
import ca.uwindsor.ims.dto.LoginRequest;
import ca.uwindsor.ims.security.ImsUserDetails;
import ca.uwindsor.ims.security.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock AuthenticationManager authManager;
    @Mock JwtEncoder jwtEncoder;

    AuthService service;

    @BeforeEach
    void setUp() {
        service = new AuthService(authManager, jwtEncoder, new AppProperties(24, 1000));
    }

    @Test
    void login_adminCredentials_returnsTokenAndAdminRole() {
        ImsUserDetails adminDetails = new ImsUserDetails("admin", "hashed", Role.ROLE_ADMIN, null);
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(adminDetails);
        when(authManager.authenticate(any())).thenReturn(auth);

        Jwt mockJwt = mock(Jwt.class);
        when(mockJwt.getTokenValue()).thenReturn("test.jwt.token");
        when(jwtEncoder.encode(any())).thenReturn(mockJwt);

        AuthResult result = service.login(new LoginRequest("admin", "admin123"));

        assertThat(result.token()).isEqualTo("test.jwt.token");
        assertThat(result.user().role()).isEqualTo(Role.ROLE_ADMIN.authority());
        assertThat(result.user().studentId()).isNull();
        assertThat(result.user().username()).isEqualTo("admin");
    }

    @Test
    void login_studentCredentials_includesStudentIdClaim() {
        ImsUserDetails studentDetails = new ImsUserDetails("alice", "hashed", Role.ROLE_STUDENT, 1001);
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(studentDetails);
        when(authManager.authenticate(any())).thenReturn(auth);

        Jwt mockJwt = mock(Jwt.class);
        when(mockJwt.getTokenValue()).thenReturn("student.jwt.token");
        when(jwtEncoder.encode(any())).thenReturn(mockJwt);

        AuthResult result = service.login(new LoginRequest("alice", "alic1001"));

        assertThat(result.user().role()).isEqualTo(Role.ROLE_STUDENT.authority());
        assertThat(result.user().studentId()).isEqualTo(1001);

        ArgumentCaptor<JwtEncoderParameters> captor = ArgumentCaptor.forClass(JwtEncoderParameters.class);
        verify(jwtEncoder).encode(captor.capture());
        Object studentIdClaim = captor.getValue().getClaims().getClaim(Constants.JWT_CLAIM_STUDENT_ID);
        assertThat(studentIdClaim).isEqualTo(1001);
    }

    @Test
    void login_badCredentials_propagatesBadCredentialsException() {
        when(authManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));

        assertThatThrownBy(() -> service.login(new LoginRequest("admin", "wrong")))
                .isInstanceOf(BadCredentialsException.class);
    }
}
