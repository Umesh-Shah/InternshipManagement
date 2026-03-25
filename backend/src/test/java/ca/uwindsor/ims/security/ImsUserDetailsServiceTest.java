package ca.uwindsor.ims.security;

import ca.uwindsor.ims.Constants;
import ca.uwindsor.ims.entity.Login;
import ca.uwindsor.ims.repository.LoginRepository;
import ca.uwindsor.ims.repository.VbctLoginRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImsUserDetailsServiceTest {

    @Mock LoginRepository loginRepo;
    @Mock VbctLoginRepository vbctLoginRepo;

    @InjectMocks ImsUserDetailsService service;

    @Test
    void loadUserByUsername_adminFound_returnsAdminDetails() {
        Login login = new Login();
        login.setUsername("admin");
        login.setPwd("hashed");
        login.setUserType(Constants.USER_TYPE_ADMIN);
        when(loginRepo.findByUsername("admin")).thenReturn(Optional.of(login));

        UserDetails details = service.loadUserByUsername("admin");

        assertThat(details).isInstanceOf(ImsUserDetails.class);
        ImsUserDetails imsDetails = (ImsUserDetails) details;
        assertThat(imsDetails.getRole()).isEqualTo(Role.ROLE_ADMIN);
        assertThat(imsDetails.getStudentId()).isNull();
        assertThat(imsDetails.getUsername()).isEqualTo("admin");
    }

    @Test
    void loadUserByUsername_studentFound_returnsStudentDetails() {
        Login login = new Login();
        login.setUsername("alice");
        login.setPwd("hashed");
        login.setUserType(Constants.USER_TYPE_STUDENT);
        login.setStudentId(1001);
        when(loginRepo.findByUsername("alice")).thenReturn(Optional.of(login));

        UserDetails details = service.loadUserByUsername("alice");

        ImsUserDetails imsDetails = (ImsUserDetails) details;
        assertThat(imsDetails.getRole()).isEqualTo(Role.ROLE_STUDENT);
        assertThat(imsDetails.getStudentId()).isEqualTo(1001);
    }

    @Test
    void loadUserByUsername_notFound_throwsUsernameNotFoundException() {
        when(loginRepo.findByUsername("unknown")).thenReturn(Optional.empty());
        when(vbctLoginRepo.findByLoginName("unknown")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.loadUserByUsername("unknown"))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}
