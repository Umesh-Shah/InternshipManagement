package ca.uwindsor.ims.controller;

import ca.uwindsor.ims.dto.*;
import ca.uwindsor.ims.service.JasperReportService;
import ca.uwindsor.ims.service.ReportService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@PreAuthorize("hasRole('ADMIN')")
public class ReportController {

    private final ReportService reportService;
    private final JasperReportService jasperReportService;

    public ReportController(ReportService reportService, JasperReportService jasperReportService) {
        this.reportService = reportService;
        this.jasperReportService = jasperReportService;
    }

    @GetMapping("/filters")
    public ReportFiltersResponse filters() {
        return reportService.getFilters();
    }

    // ── Students ──────────────────────────────────────────────────────────

    @GetMapping("/students")
    public List<StudentReportRow> students(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String semester,
            @RequestParam(required = false) String internshipStatus,
            @RequestParam(required = false) String studentStatus) {
        return reportService.studentsReport(year, country, semester, internshipStatus, studentStatus);
    }

    @GetMapping("/students/pdf")
    public ResponseEntity<byte[]> studentsPdf(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String semester,
            @RequestParam(required = false) String internshipStatus,
            @RequestParam(required = false) String studentStatus) throws JRException {
        List<StudentReportRow> data = reportService.studentsReport(year, country, semester, internshipStatus, studentStatus);
        return pdfResponse("students", data, "students-report.pdf");
    }

    // ── Companies ─────────────────────────────────────────────────────────

    @GetMapping("/companies")
    public List<CompanyReportRow> companies(@RequestParam(required = false) String city) {
        return reportService.companiesReport(city);
    }

    @GetMapping("/companies/pdf")
    public ResponseEntity<byte[]> companiesPdf(@RequestParam(required = false) String city) throws JRException {
        List<CompanyReportRow> data = reportService.companiesReport(city);
        return pdfResponse("companies", data, "companies-report.pdf");
    }

    // ── Internship Types ──────────────────────────────────────────────────

    @GetMapping("/internship-types")
    public List<InternshipTypeReportRow> internshipTypes(
            @RequestParam(required = false) String internshipType) {
        return reportService.internshipTypesReport(internshipType);
    }

    @GetMapping("/internship-types/pdf")
    public ResponseEntity<byte[]> internshipTypesPdf(
            @RequestParam(required = false) String internshipType) throws JRException {
        List<InternshipTypeReportRow> data = reportService.internshipTypesReport(internshipType);
        return pdfResponse("internship-types", data, "internship-types-report.pdf");
    }

    // ── GPA ───────────────────────────────────────────────────────────────

    @GetMapping("/gpa")
    public List<GpaReportRow> gpa(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String university,
            @RequestParam(required = false) String universityLocation,
            @RequestParam(required = false) String degreeType) {
        return reportService.gpaReport(year, country, university, universityLocation, degreeType);
    }

    @GetMapping("/gpa/pdf")
    public ResponseEntity<byte[]> gpaPdf(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String university,
            @RequestParam(required = false) String universityLocation,
            @RequestParam(required = false) String degreeType) throws JRException {
        List<GpaReportRow> data = reportService.gpaReport(year, country, university, universityLocation, degreeType);
        return pdfResponse("gpa", data, "gpa-report.pdf");
    }

    // ── Jobs ──────────────────────────────────────────────────────────────

    @GetMapping("/jobs")
    public List<JobReportRow> jobs(@RequestParam(required = false) Integer companyId) {
        return reportService.jobsReport(companyId);
    }

    @GetMapping("/jobs/pdf")
    public ResponseEntity<byte[]> jobsPdf(@RequestParam(required = false) Integer companyId) throws JRException {
        List<JobReportRow> data = reportService.jobsReport(companyId);
        return pdfResponse("jobs", data, "jobs-report.pdf");
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private ResponseEntity<byte[]> pdfResponse(String reportName, List<?> data, String filename)
            throws JRException {
        byte[] pdf = jasperReportService.generatePdf(reportName, data);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdf);
    }
}
