package ca.uwindsor.ims.logging;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

class AuditLoggerTest {

    private ListAppender<ILoggingEvent> appender;
    private Logger auditLogger;

    @BeforeEach
    void setUp() {
        auditLogger = (Logger) LoggerFactory.getLogger("AUDIT");
        appender = new ListAppender<>();
        appender.start();
        auditLogger.addAppender(appender);
    }

    @AfterEach
    void tearDown() {
        auditLogger.detachAppender(appender);
        appender.stop();
        appender.list.clear();
    }

    @Test
    void logLogin_success_writesLoginEvent() {
        AuditLogger.logLogin("alice", true, "127.0.0.1");

        assertThat(appender.list).hasSize(1);
        assertThat(appender.list.get(0).getFormattedMessage())
                .contains("LOGIN |")
                .contains("user=alice")
                .contains("success=true")
                .contains("ip=127.0.0.1");
    }

    @Test
    void logLogin_failure_writesLoginEvent() {
        AuditLogger.logLogin("bob", false, "10.0.0.1");

        assertThat(appender.list).hasSize(1);
        assertThat(appender.list.get(0).getFormattedMessage())
                .contains("LOGIN |")
                .contains("success=false");
    }

    @Test
    void logLogout_writesLogoutEvent() {
        AuditLogger.logLogout("alice");

        assertThat(appender.list).hasSize(1);
        assertThat(appender.list.get(0).getFormattedMessage())
                .contains("LOGOUT |")
                .contains("user=alice");
    }

    @Test
    void logDataAccess_writesDataEvent() {
        AuditLogger.logDataAccess("admin", "Company", "READ", 42);

        assertThat(appender.list).hasSize(1);
        assertThat(appender.list.get(0).getFormattedMessage())
                .contains("DATA |")
                .contains("entity=Company")
                .contains("action=READ")
                .contains("id=42");
    }

    @Test
    void logAdminAction_writesAdminEvent() {
        AuditLogger.logAdminAction("admin", "DELETE_STUDENT", "id=1001");

        assertThat(appender.list).hasSize(1);
        assertThat(appender.list.get(0).getFormattedMessage())
                .contains("ADMIN |")
                .contains("action=DELETE_STUDENT")
                .contains("details=id=1001");
    }
}
