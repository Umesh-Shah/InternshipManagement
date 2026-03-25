package ca.uwindsor.ims.security;

import ca.uwindsor.ims.Constants;
import ca.uwindsor.ims.entity.Login;
import ca.uwindsor.ims.entity.VbctLogin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class ImsUserDetails implements UserDetails {

    private final String username;
    private final String password;
    private final Role role;
    private final Integer studentId;   // null for admin users

    public ImsUserDetails(String username, String password, Role role, Integer studentId) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.studentId = studentId;
    }

    public static ImsUserDetails fromLogin(Login login) {
        Role role = Constants.USER_TYPE_ADMIN.equalsIgnoreCase(login.getUserType()) ? Role.ROLE_ADMIN : Role.ROLE_STUDENT;
        return new ImsUserDetails(login.getUsername(), login.getPwd(), role, login.getStudentId());
    }

    public static ImsUserDetails fromVbctLogin(VbctLogin vl) {
        // vbct_login users are admin/staff (designation-based)
        return new ImsUserDetails(vl.getLoginName(), vl.getLoginPassword(), Role.ROLE_ADMIN, null);
    }

    public Role getRole() { return role; }
    public Integer getStudentId() { return studentId; }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.authority()));
    }

    @Override public String getPassword()               { return password; }
    @Override public String getUsername()               { return username; }
    @Override public boolean isAccountNonExpired()      { return true; }
    @Override public boolean isAccountNonLocked()       { return true; }
    @Override public boolean isCredentialsNonExpired()  { return true; }
    @Override public boolean isEnabled()                { return true; }
}
