package ca.uwindsor.ims.security;

import ca.uwindsor.ims.Constants;
import ca.uwindsor.ims.entity.Login;
import ca.uwindsor.ims.entity.VbctLogin;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ImsUserDetailsTest {

    // ── fromLogin ─────────────────────────────────────────────────────────────

    @Test
    void fromLogin_adminUserType_assignsRoleAdmin() {
        Login login = new Login();
        login.setUsername("admin");
        login.setPwd("secret");
        login.setUserType(Constants.USER_TYPE_ADMIN);

        ImsUserDetails details = ImsUserDetails.fromLogin(login);

        assertThat(details.getRole()).isEqualTo(Role.ROLE_ADMIN);
        assertThat(details.getAuthorities())
                .extracting(a -> a.getAuthority())
                .containsExactly("ROLE_ADMIN");
    }

    @Test
    void fromLogin_studentUserType_assignsRoleStudent() {
        Login login = new Login();
        login.setUsername("alice");
        login.setPwd("pass");
        login.setUserType(Constants.USER_TYPE_STUDENT);
        login.setStudentId(1001);

        ImsUserDetails details = ImsUserDetails.fromLogin(login);

        assertThat(details.getRole()).isEqualTo(Role.ROLE_STUDENT);
        assertThat(details.getUsername()).isEqualTo("alice");
    }

    @Test
    void fromLogin_preservesStudentId() {
        Login login = new Login();
        login.setUsername("alice");
        login.setPwd("pass");
        login.setUserType(Constants.USER_TYPE_STUDENT);
        login.setStudentId(1001);

        assertThat(ImsUserDetails.fromLogin(login).getStudentId()).isEqualTo(1001);
    }

    // ── fromVbctLogin ─────────────────────────────────────────────────────────

    @Test
    void fromVbctLogin_assignsRoleAdmin() {
        VbctLogin vl = new VbctLogin();
        vl.setLoginId("v1");
        vl.setLoginName("vbct_admin");
        vl.setLoginPassword("pwd");

        ImsUserDetails details = ImsUserDetails.fromVbctLogin(vl);

        assertThat(details.getRole()).isEqualTo(Role.ROLE_ADMIN);
        assertThat(details.getUsername()).isEqualTo("vbct_admin");
        assertThat(details.getPassword()).isEqualTo("pwd");
    }

    @Test
    void fromVbctLogin_studentIdIsNull() {
        VbctLogin vl = new VbctLogin();
        vl.setLoginId("v1");
        vl.setLoginName("vbct_admin");
        vl.setLoginPassword("pwd");

        assertThat(ImsUserDetails.fromVbctLogin(vl).getStudentId()).isNull();
    }

    // ── getAuthorities ────────────────────────────────────────────────────────

    @Test
    void getAuthorities_studentRole_returnsRoleStudent() {
        ImsUserDetails details = new ImsUserDetails("u", "p", Role.ROLE_STUDENT, 5);

        assertThat(details.getAuthorities())
                .extracting(a -> a.getAuthority())
                .containsExactly("ROLE_STUDENT");
    }

    // ── UserDetails boolean methods ───────────────────────────────────────────

    @Test
    void isAccountNonExpired_returnsTrue() {
        assertThat(new ImsUserDetails("u", "p", Role.ROLE_ADMIN, null).isAccountNonExpired()).isTrue();
    }

    @Test
    void isAccountNonLocked_returnsTrue() {
        assertThat(new ImsUserDetails("u", "p", Role.ROLE_ADMIN, null).isAccountNonLocked()).isTrue();
    }

    @Test
    void isCredentialsNonExpired_returnsTrue() {
        assertThat(new ImsUserDetails("u", "p", Role.ROLE_ADMIN, null).isCredentialsNonExpired()).isTrue();
    }

    @Test
    void isEnabled_returnsTrue() {
        assertThat(new ImsUserDetails("u", "p", Role.ROLE_ADMIN, null).isEnabled()).isTrue();
    }
}
