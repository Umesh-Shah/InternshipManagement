package ca.uwindsor.ims.controller;

import ca.uwindsor.ims.dto.CompanyRequest;
import ca.uwindsor.ims.entity.Company;
import ca.uwindsor.ims.service.CompanyService;
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
        value = CompanyController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class,
                OAuth2ResourceServerAutoConfiguration.class,
                ServletWebSecurityAutoConfiguration.class,
                OAuth2ResourceServerWebSecurityAutoConfiguration.class
        }
)
class CompanyControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    @MockitoBean CompanyService companyService;

    // ── GET /api/companies ───────────────────────────────────────────────────

    @Test
    void getAll_returnsCompanyList() throws Exception {
        Company c = company(10, "Acme Corp", "Windsor");
        when(companyService.findAll()).thenReturn(List.of(c));

        mvc.perform(get("/api/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].companyId").value(10))
                .andExpect(jsonPath("$[0].companyName").value("Acme Corp"));
    }

    // ── GET /api/companies/{id} ──────────────────────────────────────────────

    @Test
    void getById_existingCompany_returns200() throws Exception {
        Company c = company(10, "Acme Corp", "Windsor");
        when(companyService.findById(10)).thenReturn(Optional.of(c));

        mvc.perform(get("/api/companies/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companyName").value("Acme Corp"));
    }

    @Test
    void getById_unknownCompany_returns404() throws Exception {
        when(companyService.findById(9999)).thenReturn(Optional.empty());

        mvc.perform(get("/api/companies/9999"))
                .andExpect(status().isNotFound());
    }

    // ── POST /api/companies ──────────────────────────────────────────────────

    @Test
    void create_validRequest_returns200() throws Exception {
        CompanyRequest req = new CompanyRequest(
                "Acme Corp", "123 Main St", "Windsor", "N9A 1A1",
                "Canada", "John", "Doe", "HR", "555-0100",
                "hr@acme.com", "https://acme.com", null);
        Company created = company(10, "Acme Corp", "Windsor");
        when(companyService.create(any())).thenReturn(created);

        mvc.perform(post("/api/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companyId").value(10));
    }

    // ── PUT /api/companies/{id} ──────────────────────────────────────────────

    @Test
    void update_existingCompany_returns200() throws Exception {
        CompanyRequest req = new CompanyRequest(
                "Acme Updated", null, "Toronto", null,
                "Canada", null, null, null, null, null, null, null);
        Company updated = company(10, "Acme Updated", "Toronto");
        when(companyService.update(eq(10), any())).thenReturn(Optional.of(updated));

        mvc.perform(put("/api/companies/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companyName").value("Acme Updated"));
    }

    @Test
    void update_unknownCompany_returns404() throws Exception {
        when(companyService.update(eq(9999), any())).thenReturn(Optional.empty());

        mvc.perform(put("/api/companies/9999")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isNotFound());
    }

    // ── DELETE /api/companies/{id} ───────────────────────────────────────────

    @Test
    void delete_existingCompany_returns204() throws Exception {
        when(companyService.delete(10)).thenReturn(true);

        mvc.perform(delete("/api/companies/10"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_unknownCompany_returns404() throws Exception {
        when(companyService.delete(9999)).thenReturn(false);

        mvc.perform(delete("/api/companies/9999"))
                .andExpect(status().isNotFound());
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private Company company(Integer id, String name, String city) {
        Company c = new Company();
        c.setCompanyId(id);
        c.setCompanyName(name);
        c.setCity(city);
        c.setCountry("Canada");
        return c;
    }
}
