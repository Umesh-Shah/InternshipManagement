package ca.uwindsor.ims.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

/**
 * SpEL-accessible bean for @PreAuthorize checks on student sub-resources.
 * Usage: @PreAuthorize("@studentSecurity.canAccess(authentication, #studentId)")
 */
@Component("studentSecurity")
public class StudentSecurityHelper {

    public boolean canAccess(Authentication authentication, Integer studentId) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) return true;

        if (authentication instanceof JwtAuthenticationToken jat) {
            Object claim = jat.getToken().getClaim("student_id");
            if (claim instanceof Number n) {
                return n.intValue() == studentId;
            }
        }
        return false;
    }
}
