package ca.uwindsor.ims.controller;

import ca.uwindsor.ims.dto.InternshipTypeRequest;
import ca.uwindsor.ims.entity.InternshipType;
import ca.uwindsor.ims.service.InternshipTypeService;
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
        value = InternshipTypeController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class,
                OAuth2ResourceServerAutoConfiguration.class,
                ServletWebSecurityAutoConfiguration.class,
                OAuth2ResourceServerWebSecurityAutoConfiguration.class
        }
)
class InternshipTypeControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    @MockitoBean InternshipTypeService internshipTypeService;

    // ── GET /api/internship-types ────────────────────────────────────────────

    @Test
    void getAll_returnsTypeList() throws Exception {
        when(internshipTypeService.findAll()).thenReturn(List.of(internshipType(5, "Full-time", "FT Internship")));

        mvc.perform(get("/api/internship-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].internshipId").value(5))
                .andExpect(jsonPath("$[0].internshipType").value("Full-time"));
    }

    // ── GET /api/internship-types/{id} ───────────────────────────────────────

    @Test
    void getById_existingType_returns200() throws Exception {
        when(internshipTypeService.findById(5)).thenReturn(Optional.of(internshipType(5, "Full-time", "FT Internship")));

        mvc.perform(get("/api/internship-types/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.internshipType").value("Full-time"));
    }

    @Test
    void getById_unknownType_returns404() throws Exception {
        when(internshipTypeService.findById(9999)).thenReturn(Optional.empty());

        mvc.perform(get("/api/internship-types/9999"))
                .andExpect(status().isNotFound());
    }

    // ── POST /api/internship-types ───────────────────────────────────────────

    @Test
    void create_validRequest_returns200() throws Exception {
        InternshipTypeRequest req = new InternshipTypeRequest("Part-time", "PT Internship", "PT");
        when(internshipTypeService.create(any())).thenReturn(internshipType(6, "Part-time", "PT Internship"));

        mvc.perform(post("/api/internship-types")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.internshipId").value(6));
    }

    // ── PUT /api/internship-types/{id} ───────────────────────────────────────

    @Test
    void update_existingType_returns200() throws Exception {
        InternshipTypeRequest req = new InternshipTypeRequest("Full-time Updated", "Updated desc", "FT");
        when(internshipTypeService.update(eq(5), any())).thenReturn(
                Optional.of(internshipType(5, "Full-time Updated", "Updated desc")));

        mvc.perform(put("/api/internship-types/5")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.internshipType").value("Full-time Updated"));
    }

    @Test
    void update_unknownType_returns404() throws Exception {
        when(internshipTypeService.update(eq(9999), any())).thenReturn(Optional.empty());

        mvc.perform(put("/api/internship-types/9999")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isNotFound());
    }

    // ── DELETE /api/internship-types/{id} ────────────────────────────────────

    @Test
    void delete_existingType_returns204() throws Exception {
        when(internshipTypeService.delete(5)).thenReturn(true);

        mvc.perform(delete("/api/internship-types/5"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_unknownType_returns404() throws Exception {
        when(internshipTypeService.delete(9999)).thenReturn(false);

        mvc.perform(delete("/api/internship-types/9999"))
                .andExpect(status().isNotFound());
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private InternshipType internshipType(Integer id, String type, String description) {
        InternshipType it = new InternshipType();
        it.setInternshipId(id);
        it.setInternshipType(type);
        it.setDescription(description);
        it.setInternshipName(type);
        return it;
    }
}
