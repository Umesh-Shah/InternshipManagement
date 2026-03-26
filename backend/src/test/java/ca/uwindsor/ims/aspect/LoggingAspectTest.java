package ca.uwindsor.ims.aspect;

import ca.uwindsor.ims.controller.CompanyController;
import ca.uwindsor.ims.controller.HealthController;
import ca.uwindsor.ims.repository.CompanyRepository;
import ca.uwindsor.ims.service.CompanyService;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

/**
 * Verifies LoggingAspect using a full Spring context (required for AOP proxies).
 * slow-request-threshold-ms=-1 guarantees every controller call is flagged as slow,
 * exercising the WARN log branch without relying on timing.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:logaspect;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "jwt.secret=test-secret-at-least-32-bytes-long!!",
        "spring.mail.host=localhost",
        "spring.mail.port=25",
        "app.security.slow-request-threshold-ms=-1"
})
class LoggingAspectTest {

    @MockBean CompanyRepository companyRepository;

    @Autowired HealthController healthController;
    @Autowired CompanyController companyController;
    @Autowired CompanyService companyService;

    private ListAppender<ILoggingEvent> appender;
    private Logger aspectLogger;

    @BeforeEach
    void attachAppender() {
        aspectLogger = (Logger) LoggerFactory.getLogger(LoggingAspect.class);
        aspectLogger.setLevel(Level.TRACE);
        appender = new ListAppender<>();
        appender.start();
        aspectLogger.addAppender(appender);
    }

    @AfterEach
    void detachAppender() {
        aspectLogger.detachAppender(appender);
        appender.stop();
        appender.list.clear();
    }

    // ── logController — normal + slow-request path ────────────────────────────

    @Test
    void logController_normalCall_logsDebugAndSlowWarn() {
        // threshold=-1 so any call is "slow"
        healthController.health();

        boolean hasSlowWarn = appender.list.stream()
                .anyMatch(e -> e.getLevel() == Level.WARN
                        && e.getFormattedMessage().contains("Slow request"));
        assertThat(hasSlowWarn).isTrue();
    }

    // ── logController — exception path ────────────────────────────────────────

    @Test
    @WithMockUser(roles = "ADMIN")
    void logController_exceptionThrown_logsErrorAndRethrows() {
        when(companyRepository.findAll()).thenThrow(new RuntimeException("DB error"));

        assertThatThrownBy(() -> companyController.getAll())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("DB error");

        boolean hasError = appender.list.stream()
                .anyMatch(e -> e.getLevel() == Level.ERROR
                        && e.getFormattedMessage().contains("DB error"));
        assertThat(hasError).isTrue();
    }

    // ── logService — normal path ───────────────────────────────────────────────

    @Test
    void logService_normalCall_proceedsAndReturns() {
        when(companyRepository.findAll()).thenReturn(List.of());

        List<?> result = companyService.findAll();

        assertThat(result).isEmpty();
    }

    // ── logService — exception path ───────────────────────────────────────────

    @Test
    void logService_exceptionThrown_logsWarnAndRethrows() {
        when(companyRepository.findAll()).thenThrow(new RuntimeException("Service error"));

        assertThatThrownBy(() -> companyService.findAll())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Service error");

        boolean hasWarn = appender.list.stream()
                .anyMatch(e -> e.getLevel() == Level.WARN
                        && e.getFormattedMessage().contains("Service error"));
        assertThat(hasWarn).isTrue();
    }
}
