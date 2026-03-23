package ca.uwindsor.ims.security;

/**
 * Application roles. The enum name is the full Spring Security authority string
 * (i.e. it includes the "ROLE_" prefix), so it can be passed directly to
 * {@link org.springframework.security.core.authority.SimpleGrantedAuthority}.
 */
public enum Role {
    ROLE_ADMIN,
    ROLE_STUDENT;

    /** Returns the authority string stored in the JWT "role" claim. */
    public String authority() {
        return name();
    }
}
