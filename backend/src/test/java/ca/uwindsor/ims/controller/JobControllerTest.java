package ca.uwindsor.ims.controller;

import ca.uwindsor.ims.dto.JobRequest;
import ca.uwindsor.ims.entity.Job;
import ca.uwindsor.ims.service.JobService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.autoconfigure.web.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.security.autoconfigure.web.servlet.ServletWebSecurityAutoConfiguration;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.web.OAuth2ResourceServerWebSecurityAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        value = JobController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class,
                OAuth2ResourceServerAutoConfiguration.class,
                ServletWebSecurityAutoConfiguration.class,
                OAuth2ResourceServerWebSecurityAutoConfiguration.class
        }
)
class JobControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    @MockitoBean JobService jobService;

    // ── GET /api/jobs ────────────────────────────────────────────────────────

    @Test
    void getAll_returnsJobList() throws Exception {
        when(jobService.findAll()).thenReturn(List.of(job(1, "Software Engineer", 10)));

        mvc.perform(get("/api/jobs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].jobId").value(1))
                .andExpect(jsonPath("$[0].jobPosition").value("Software Engineer"));
    }

    @Test
    void getAll_withCompanyId_returnsFilteredList() throws Exception {
        when(jobService.findByCompanyId(10)).thenReturn(List.of(
                job(1, "Software Engineer", 10),
                job(2, "QA Engineer", 10)));

        mvc.perform(get("/api/jobs").param("companyId", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // ── GET /api/jobs/{id} ───────────────────────────────────────────────────

    @Test
    void getById_existingJob_returns200() throws Exception {
        when(jobService.findById(1)).thenReturn(Optional.of(job(1, "Software Engineer", 10)));

        mvc.perform(get("/api/jobs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jobPosition").value("Software Engineer"));
    }

    @Test
    void getById_unknownJob_returns404() throws Exception {
        when(jobService.findById(9999)).thenReturn(Optional.empty());

        mvc.perform(get("/api/jobs/9999"))
                .andExpect(status().isNotFound());
    }

    // ── POST /api/jobs ───────────────────────────────────────────────────────

    @Test
    void create_validRequest_returns200() throws Exception {
        JobRequest req = new JobRequest("Dev", "Desc", "Req", 60000, 10, "Resp", "Java", "Full-time");
        when(jobService.create(any())).thenReturn(job(1, "Dev", 10));

        mvc.perform(post("/api/jobs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jobId").value(1));
    }

    // ── PUT /api/jobs/{id} ───────────────────────────────────────────────────

    @Test
    void update_existingJob_returns200() throws Exception {
        JobRequest req = new JobRequest("Senior Dev", null, null, 80000, 10, null, null, null);
        when(jobService.update(eq(1), any())).thenReturn(Optional.of(job(1, "Senior Dev", 10)));

        mvc.perform(put("/api/jobs/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jobPosition").value("Senior Dev"));
    }

    @Test
    void update_unknownJob_returns404() throws Exception {
        when(jobService.update(eq(9999), any())).thenReturn(Optional.empty());

        mvc.perform(put("/api/jobs/9999")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isNotFound());
    }

    // ── DELETE /api/jobs/{id} ────────────────────────────────────────────────

    @Test
    void delete_existingJob_returns204() throws Exception {
        when(jobService.delete(1)).thenReturn(true);

        mvc.perform(delete("/api/jobs/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_unknownJob_returns404() throws Exception {
        when(jobService.delete(9999)).thenReturn(false);

        mvc.perform(delete("/api/jobs/9999"))
                .andExpect(status().isNotFound());
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private Job job(Integer id, String position, Integer companyId) {
        Job j = new Job();
        j.setJobId(id);
        j.setJobPosition(position);
        j.setCompanyId(companyId);
        return j;
    }
}
