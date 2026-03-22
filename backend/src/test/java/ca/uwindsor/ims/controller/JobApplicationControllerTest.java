package ca.uwindsor.ims.controller;

import ca.uwindsor.ims.dto.JobApplicationRequest;
import ca.uwindsor.ims.dto.JobApplicationResponse;
import ca.uwindsor.ims.service.JobApplicationService;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        value = JobApplicationController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class,
                OAuth2ResourceServerAutoConfiguration.class
        }
)
class JobApplicationControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    @MockBean JobApplicationService jobApplicationService;

    // ── POST /api/job-applications ───────────────────────────────────────────

    @Test
    void apply_validRequest_returns200() throws Exception {
        JobApplicationResponse resp = response(1, 101, "Software Engineer", 10, "Acme", 1001, "Alice Smith", "N");
        when(jobApplicationService.apply(any())).thenReturn(resp);

        mvc.perform(post("/api/job-applications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new JobApplicationRequest(1001, 101))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jobPosition").value("Software Engineer"))
                .andExpect(jsonPath("$.flag").value("N"));
    }

    @Test
    void apply_missingStudentId_returns400() throws Exception {
        mvc.perform(post("/api/job-applications")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"jobId":101}
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void apply_missingJobId_returns400() throws Exception {
        mvc.perform(post("/api/job-applications")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"studentId":1001}
                        """))
                .andExpect(status().isBadRequest());
    }

    // ── GET /api/job-applications?studentId=X ────────────────────────────────

    @Test
    void getByStudent_returnsList() throws Exception {
        List<JobApplicationResponse> list = List.of(
                response(1, 101, "Dev", 10, "Acme", 1001, "Alice", "N"),
                response(2, 102, "QA",  10, "Acme", 1001, "Alice", "A")
        );
        when(jobApplicationService.getByStudent(1001)).thenReturn(list);

        mvc.perform(get("/api/job-applications").param("studentId", "1001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // ── GET /api/job-applications/pending ────────────────────────────────────

    @Test
    void getPending_returnsPendingList() throws Exception {
        when(jobApplicationService.getPending()).thenReturn(List.of(
                response(1, 101, "Dev", 10, "Acme", 1001, "Alice", "N")));

        mvc.perform(get("/api/job-applications/pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].flag").value("N"));
    }

    // ── PUT /api/job-applications/{id}/approve ────────────────────────────────

    @Test
    void approve_existingApplication_returns200WithFlagA() throws Exception {
        JobApplicationResponse approved = response(1, 101, "Dev", 10, "Acme", 1001, "Alice", "A");
        when(jobApplicationService.approve(1)).thenReturn(approved);

        mvc.perform(put("/api/job-applications/1/approve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value("A"));
    }

    @Test
    void approve_nonExistentApplication_returns404() throws Exception {
        when(jobApplicationService.approve(9999))
                .thenThrow(new ResponseStatusException(NOT_FOUND));

        mvc.perform(put("/api/job-applications/9999/approve"))
                .andExpect(status().isNotFound());
    }

    // ── GET /api/job-applications/approved?jobId=X ───────────────────────────

    @Test
    void getApprovedByJob_returnsApprovedList() throws Exception {
        when(jobApplicationService.getApprovedByJob(101)).thenReturn(List.of(
                response(1, 101, "Dev", 10, "Acme", 1001, "Alice", "A")));

        mvc.perform(get("/api/job-applications/approved").param("jobId", "101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].flag").value("A"));
    }

    // ── Helper ───────────────────────────────────────────────────────────────

    private JobApplicationResponse response(
            Integer id, Integer jobId, String jobPos, Integer companyId, String companyName,
            Integer studentId, String studentName, String flag) {
        return new JobApplicationResponse(id, jobId, jobPos, companyId, companyName, studentId, studentName, flag);
    }
}
