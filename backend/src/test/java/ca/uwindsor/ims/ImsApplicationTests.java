package ca.uwindsor.ims;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * Smoke test: verifies the application context can be assembled.
 * Uses an in-memory H2 database so no MySQL instance is required.
 * JasperReports compilation errors (missing .jrxml) are non-fatal here
 * because JasperReportService.init() logs and continues on error.
 */
@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "jwt.secret=test-secret-at-least-32-bytes-long!!",
        "spring.mail.host=localhost",
        "spring.mail.port=25"
})
class ImsApplicationTests {

    @Test
    void contextLoads() {
    }
}
