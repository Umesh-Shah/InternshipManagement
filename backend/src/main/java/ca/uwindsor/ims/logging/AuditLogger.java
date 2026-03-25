package ca.uwindsor.ims.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AuditLogger {

    private static final Logger audit = LoggerFactory.getLogger("AUDIT");

    private AuditLogger() {}

    public static void logLogin(String username, boolean success, String ip) {
        audit.info("LOGIN | user={} | success={} | ip={}", username, success, ip);
    }

    public static void logLogout(String username) {
        audit.info("LOGOUT | user={}", username);
    }

    public static void logDataAccess(String username, String entity, String action, Object entityId) {
        audit.info("DATA | user={} | entity={} | action={} | id={}", username, entity, action, entityId);
    }

    public static void logAdminAction(String username, String action, String details) {
        audit.info("ADMIN | user={} | action={} | details={}", username, action, details);
    }
}
