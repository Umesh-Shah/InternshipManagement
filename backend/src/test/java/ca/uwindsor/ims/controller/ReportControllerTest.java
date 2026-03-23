package ca.uwindsor.ims.controller;

import ca.uwindsor.ims.dto.*;
import ca.uwindsor.ims.service.JasperReportService;
import ca.uwindsor.ims.service.ReportService;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        value = ReportController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class,
                OAuth2ResourceServerAutoConfiguration.class
        }
)
class ReportControllerTest {

    @Autowired MockMvc mvc;

    @MockBean ReportService reportService;
    @MockBean JasperReportService jasperReportService;

    // ── GET /api/reports/filters ─────────────────────────────────────────────

    @Test
    void filters_returnsFilterOptions() throws Exception {
        ReportFiltersResponse filters = new ReportFiltersResponse(
                List.of(2023, 2024),
                List.of("Canada", "USA"),
                List.of("University of Windsor"),
                List.of("Windsor, ON"),
                List.of("Full-time", "Part-time"));
        when(reportService.getFilters()).thenReturn(filters);

        mvc.perform(get("/api/reports/filters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.years[0]").value(2023))
                .andExpect(jsonPath("$.countries[0]").value("Canada"));
    }

    // ── GET /api/reports/students ────────────────────────────────────────────

    @Test
    void studentsReport_noFilters_returnsList() throws Exception {
        when(reportService.studentsReport(isNull(), isNull(), isNull(), isNull(), isNull()))
                .thenReturn(List.of(
                        new StudentReportRow(1001, "Alice", "Smith", 2024, "Canada", "Fall", "Active", "Active")));

        mvc.perform(get("/api/reports/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].studentId").value(1001))
                .andExpect(jsonPath("$[0].fname").value("Alice"));
    }

    @Test
    void studentsReport_withFilters_returnsList() throws Exception {
        when(reportService.studentsReport(eq(2024), eq("Canada"), isNull(), isNull(), isNull()))
                .thenReturn(List.of(
                        new StudentReportRow(1001, "Alice", "Smith", 2024, "Canada", "Fall", "Active", "Active")));

        mvc.perform(get("/api/reports/students")
                .param("year", "2024")
                .param("country", "Canada"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    // ── GET /api/reports/students/pdf ────────────────────────────────────────

    @Test
    void studentsPdf_returnsPdfBytes() throws Exception {
        when(reportService.studentsReport(isNull(), isNull(), isNull(), isNull(), isNull()))
                .thenReturn(List.of());
        when(jasperReportService.generatePdf(eq("students"), any())).thenReturn(new byte[]{1, 2, 3});

        mvc.perform(get("/api/reports/students/pdf"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"students-report.pdf\""));
    }

    // ── GET /api/reports/companies ───────────────────────────────────────────

    @Test
    void companiesReport_returnsList() throws Exception {
        when(reportService.companiesReport(isNull()))
                .thenReturn(List.of(
                        new CompanyReportRow(10, "Acme", "Windsor", "Canada", "hr@acme.com", "555-0100")));

        mvc.perform(get("/api/reports/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].companyName").value("Acme"));
    }

    @Test
    void companiesPdf_returnsPdfBytes() throws Exception {
        when(reportService.companiesReport(isNull())).thenReturn(List.of());
        when(jasperReportService.generatePdf(eq("companies"), any())).thenReturn(new byte[]{1, 2, 3});

        mvc.perform(get("/api/reports/companies/pdf"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF));
    }

    // ── GET /api/reports/internship-types ────────────────────────────────────

    @Test
    void internshipTypesReport_returnsList() throws Exception {
        when(reportService.internshipTypesReport(isNull()))
                .thenReturn(List.of(new InternshipTypeReportRow("Full-time", 5L)));

        mvc.perform(get("/api/reports/internship-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].internshipType").value("Full-time"))
                .andExpect(jsonPath("$[0].studentCount").value(5));
    }

    @Test
    void internshipTypesPdf_returnsPdfBytes() throws Exception {
        when(reportService.internshipTypesReport(isNull())).thenReturn(List.of());
        when(jasperReportService.generatePdf(eq("internship-types"), any())).thenReturn(new byte[]{1, 2, 3});

        mvc.perform(get("/api/reports/internship-types/pdf"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF));
    }

    // ── GET /api/reports/gpa ─────────────────────────────────────────────────

    @Test
    void gpaReport_returnsList() throws Exception {
        when(reportService.gpaReport(isNull(), isNull(), isNull(), isNull(), isNull()))
                .thenReturn(List.of(
                        new GpaReportRow(1001, "Alice", "Smith", "University of Windsor", "Windsor, ON", "BSc", "3.8")));

        mvc.perform(get("/api/reports/gpa"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].studentId").value(1001))
                .andExpect(jsonPath("$[0].degreeGpa").value("3.8"));
    }

    @Test
    void gpaPdf_returnsPdfBytes() throws Exception {
        when(reportService.gpaReport(isNull(), isNull(), isNull(), isNull(), isNull())).thenReturn(List.of());
        when(jasperReportService.generatePdf(eq("gpa"), any())).thenReturn(new byte[]{1, 2, 3});

        mvc.perform(get("/api/reports/gpa/pdf"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF));
    }

    // ── GET /api/reports/jobs ────────────────────────────────────────────────

    @Test
    void jobsReport_returnsList() throws Exception {
        when(reportService.jobsReport(isNull()))
                .thenReturn(List.of(new JobReportRow(1, "Software Engineer", "Acme", 12L)));

        mvc.perform(get("/api/reports/jobs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].jobPosition").value("Software Engineer"))
                .andExpect(jsonPath("$[0].applicantCount").value(12));
    }

    @Test
    void jobsPdf_returnsPdfBytes() throws Exception {
        when(reportService.jobsReport(isNull())).thenReturn(List.of());
        when(jasperReportService.generatePdf(eq("jobs"), any())).thenReturn(new byte[]{1, 2, 3});

        mvc.perform(get("/api/reports/jobs/pdf"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF));
    }
}
