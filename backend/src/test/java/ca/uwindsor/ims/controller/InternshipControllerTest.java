package ca.uwindsor.ims.controller;

import ca.uwindsor.ims.dto.InternshipStatusRequest;
import ca.uwindsor.ims.dto.InternshipStatusResponse;
import ca.uwindsor.ims.service.InternshipStatusService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        value = InternshipController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class,
                OAuth2ResourceServerAutoConfiguration.class
        }
)
class InternshipControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    @MockBean InternshipStatusService internshipStatusService;

    // ── POST /api/internships/status ─────────────────────────────────────────

    @Test
    void assign_validRequest_returns200() throws Exception {
        InternshipStatusRequest req = new InternshipStatusRequest(1001, 10, 1, 5, "Full-time", "Active");
        InternshipStatusResponse resp = statusResponse(1, 1001, "Alice Smith", 10, "Acme", 1, "Dev", 5, "Full-time", "Active", "Active");
        when(internshipStatusService.assign(any())).thenReturn(resp);

        mvc.perform(post("/api/internships/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value(1001))
                .andExpect(jsonPath("$.internshipStatus").value("Active"));
    }

    // ── GET /api/internships/status ──────────────────────────────────────────

    @Test
    void getAll_returnsStatusList() throws Exception {
        when(internshipStatusService.getAll()).thenReturn(List.of(
                statusResponse(1, 1001, "Alice Smith", 10, "Acme", 1, "Dev", 5, "Full-time", "Active", "Active"),
                statusResponse(2, 1002, "Bob Jones",  10, "Acme", 2, "QA",  5, "Part-time", "Pending", "Active")
        ));

        mvc.perform(get("/api/internships/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].studentName").value("Alice Smith"));
    }

    // ── GET /api/internships/status/student/{studentId} ──────────────────────

    @Test
    void getByStudent_returnsStudentStatuses() throws Exception {
        when(internshipStatusService.getByStudent(1001)).thenReturn(List.of(
                statusResponse(1, 1001, "Alice Smith", 10, "Acme", 1, "Dev", 5, "Full-time", "Active", "Active")
        ));

        mvc.perform(get("/api/internships/status/student/1001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].studentId").value(1001))
                .andExpect(jsonPath("$[0].companyName").value("Acme"));
    }

    @Test
    void getByStudent_noResults_returnsEmptyList() throws Exception {
        when(internshipStatusService.getByStudent(9999)).thenReturn(List.of());

        mvc.perform(get("/api/internships/status/student/9999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private InternshipStatusResponse statusResponse(
            Integer id, Integer studentId, String studentName,
            Integer companyId, String companyName,
            Integer jobId, String jobPosition,
            Integer internshipId, String internshipType,
            String internshipStatus, String studentStatus) {
        return new InternshipStatusResponse(
                id, studentId, studentName,
                companyId, companyName,
                jobId, jobPosition,
                internshipId, internshipType,
                internshipStatus, studentStatus);
    }
}
