package ca.uwindsor.ims.controller;

import ca.uwindsor.ims.dto.SkillRequest;
import ca.uwindsor.ims.entity.Skill;
import ca.uwindsor.ims.service.SkillService;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        value = SkillController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class,
                OAuth2ResourceServerAutoConfiguration.class
        }
)
class SkillControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    @MockBean SkillService skillService;

    // ── GET /api/skills ──────────────────────────────────────────────────────

    @Test
    void getAll_returnsSkillList() throws Exception {
        when(skillService.findAll()).thenReturn(List.of(skill(1, "Java", "Technical")));

        mvc.perform(get("/api/skills"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].skillId").value(1))
                .andExpect(jsonPath("$[0].skillName").value("Java"));
    }

    // ── GET /api/skills/{id} ─────────────────────────────────────────────────

    @Test
    void getById_existingSkill_returns200() throws Exception {
        when(skillService.findById(1)).thenReturn(Optional.of(skill(1, "Java", "Technical")));

        mvc.perform(get("/api/skills/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.skillName").value("Java"));
    }

    @Test
    void getById_unknownSkill_returns404() throws Exception {
        when(skillService.findById(9999)).thenReturn(Optional.empty());

        mvc.perform(get("/api/skills/9999"))
                .andExpect(status().isNotFound());
    }

    // ── POST /api/skills ─────────────────────────────────────────────────────

    @Test
    void create_validRequest_returns200() throws Exception {
        SkillRequest req = new SkillRequest("Python", "Technical");
        when(skillService.create(any())).thenReturn(skill(2, "Python", "Technical"));

        mvc.perform(post("/api/skills")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.skillId").value(2));
    }

    // ── PUT /api/skills/{id} ─────────────────────────────────────────────────

    @Test
    void update_existingSkill_returns200() throws Exception {
        SkillRequest req = new SkillRequest("Python 3", "Technical");
        when(skillService.update(eq(2), any())).thenReturn(Optional.of(skill(2, "Python 3", "Technical")));

        mvc.perform(put("/api/skills/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.skillName").value("Python 3"));
    }

    @Test
    void update_unknownSkill_returns404() throws Exception {
        when(skillService.update(eq(9999), any())).thenReturn(Optional.empty());

        mvc.perform(put("/api/skills/9999")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isNotFound());
    }

    // ── DELETE /api/skills/{id} ──────────────────────────────────────────────

    @Test
    void delete_existingSkill_returns204() throws Exception {
        when(skillService.delete(1)).thenReturn(true);

        mvc.perform(delete("/api/skills/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_unknownSkill_returns404() throws Exception {
        when(skillService.delete(9999)).thenReturn(false);

        mvc.perform(delete("/api/skills/9999"))
                .andExpect(status().isNotFound());
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private Skill skill(Integer id, String name, String type) {
        Skill s = new Skill();
        s.setSkillId(id);
        s.setSkillName(name);
        s.setSkillType(type);
        return s;
    }
}
