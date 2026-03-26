package ca.uwindsor.ims.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class MdcFilterTest {

    private final MdcFilter filter = new MdcFilter();

    @AfterEach
    void clearMdc() {
        MDC.clear();
    }

    @Test
    void doFilter_setsTraceIdInMdc() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/health");
        MockHttpServletResponse response = new MockHttpServletResponse();

        String[] capturedId = {null};
        FilterChain chain = (req, res) -> capturedId[0] = MDC.get("requestId");

        filter.doFilter(request, response, chain);

        assertThat(capturedId[0]).isNotNull().hasSize(8);
    }

    @Test
    void doFilter_setsMdcContextDuringChain() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/companies");
        MockHttpServletResponse response = new MockHttpServletResponse();

        String[] capturedMethod = {null};
        String[] capturedUri = {null};
        FilterChain chain = (req, res) -> {
            capturedMethod[0] = MDC.get("method");
            capturedUri[0] = MDC.get("uri");
        };

        filter.doFilter(request, response, chain);

        assertThat(capturedMethod[0]).isEqualTo("POST");
        assertThat(capturedUri[0]).isEqualTo("/api/companies");
    }

    @Test
    void doFilter_clearsTraceIdAfterChain() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/health");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        assertThat(MDC.get("requestId")).isNull();
        assertThat(MDC.get("method")).isNull();
        assertThat(MDC.get("uri")).isNull();
    }

    @Test
    void doFilter_withXForwardedFor_usesForwardedIp() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/health");
        request.addHeader("X-Forwarded-For", "203.0.113.1");
        MockHttpServletResponse response = new MockHttpServletResponse();

        String[] capturedIp = {null};
        FilterChain chain = (req, res) -> capturedIp[0] = MDC.get("clientIp");

        filter.doFilter(request, response, chain);

        assertThat(capturedIp[0]).isEqualTo("203.0.113.1");
    }

    @Test
    void doFilter_withMultipleForwardedIps_usesFirstOnly() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/health");
        request.addHeader("X-Forwarded-For", "203.0.113.1, 10.0.0.1, 192.168.0.1");
        MockHttpServletResponse response = new MockHttpServletResponse();

        String[] capturedIp = {null};
        FilterChain chain = (req, res) -> capturedIp[0] = MDC.get("clientIp");

        filter.doFilter(request, response, chain);

        assertThat(capturedIp[0]).isEqualTo("203.0.113.1");
    }
}
