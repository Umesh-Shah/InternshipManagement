package ca.uwindsor.ims.service;

import ca.uwindsor.ims.dto.*;
import ca.uwindsor.ims.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@Import(ReportService.class)
class ReportServiceTest {

    @Autowired ReportService service;
    @Autowired TestEntityManager em;

    // ── studentsReport ────────────────────────────────────────────────────────

    @Test
    void studentsReport_noFilters_returnsAll() {
        em.persist(student(1001, "Alice", "Smith", 2024, "Canada", "Fall", "Placed", "Active"));
        em.persist(student(1002, "Bob", "Jones", 2023, "USA", "Winter", "Pending", "Hired"));
        em.flush(); em.clear();

        List<StudentReportRow> result = service.studentsReport(null, null, null, null, null);

        assertThat(result).hasSize(2);
    }

    @Test
    void studentsReport_filterByYear_returnsMatchingOnly() {
        em.persist(student(1001, "Alice", "Smith", 2024, "Canada", "Fall", "Placed", "Active"));
        em.persist(student(1002, "Bob", "Jones", 2023, "USA", "Winter", "Pending", "Hired"));
        em.flush(); em.clear();

        List<StudentReportRow> result = service.studentsReport(2024, null, null, null, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFname()).isEqualTo("Alice");
    }

    @Test
    void studentsReport_filterByCountry_returnsMatchingOnly() {
        em.persist(student(1001, "Alice", "Smith", 2024, "Canada", "Fall", "Placed", "Active"));
        em.persist(student(1002, "Bob", "Jones", 2023, "USA", "Winter", "Pending", "Hired"));
        em.flush(); em.clear();

        List<StudentReportRow> result = service.studentsReport(null, "USA", null, null, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFname()).isEqualTo("Bob");
    }

    @Test
    void studentsReport_filterBySemester_returnsMatchingOnly() {
        em.persist(student(1001, "Alice", "Smith", 2024, "Canada", "Fall", "Placed", "Active"));
        em.persist(student(1002, "Bob", "Jones", 2023, "USA", "Winter", "Pending", "Hired"));
        em.flush(); em.clear();

        List<StudentReportRow> result = service.studentsReport(null, null, "Fall", null, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFname()).isEqualTo("Alice");
    }

    @Test
    void studentsReport_filterByInternshipStatus_returnsMatchingOnly() {
        em.persist(student(1001, "Alice", "Smith", 2024, "Canada", "Fall", "Placed", "Active"));
        em.persist(student(1002, "Bob", "Jones", 2023, "USA", "Winter", "Pending", "Hired"));
        em.flush(); em.clear();

        List<StudentReportRow> result = service.studentsReport(null, null, null, "Pending", null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFname()).isEqualTo("Bob");
    }

    @Test
    void studentsReport_filterByStudentStatus_returnsMatchingOnly() {
        em.persist(student(1001, "Alice", "Smith", 2024, "Canada", "Fall", "Placed", "Active"));
        em.persist(student(1002, "Bob", "Jones", 2023, "USA", "Winter", "Pending", "Hired"));
        em.flush(); em.clear();

        List<StudentReportRow> result = service.studentsReport(null, null, null, null, "Hired");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFname()).isEqualTo("Bob");
    }

    @Test
    void studentsReport_allFilters_returnsExactMatch() {
        em.persist(student(1001, "Alice", "Smith", 2024, "Canada", "Fall", "Placed", "Active"));
        em.persist(student(1002, "Bob", "Jones", 2023, "USA", "Winter", "Pending", "Hired"));
        em.flush(); em.clear();

        List<StudentReportRow> result =
                service.studentsReport(2024, "Canada", "Fall", "Placed", "Active");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStudentId()).isEqualTo(1001);
    }

    @Test
    void studentsReport_noMatch_returnsEmpty() {
        em.persist(student(1001, "Alice", "Smith", 2024, "Canada", "Fall", "Placed", "Active"));
        em.flush(); em.clear();

        List<StudentReportRow> result = service.studentsReport(1999, null, null, null, null);

        assertThat(result).isEmpty();
    }

    // ── companiesReport ───────────────────────────────────────────────────────

    @Test
    void companiesReport_noFilter_returnsAll() {
        em.persist(company("Acme Corp", "Windsor"));
        em.persist(company("Beta Inc", "Toronto"));
        em.flush(); em.clear();

        List<CompanyReportRow> result = service.companiesReport(null);

        assertThat(result).hasSize(2);
    }

    @Test
    void companiesReport_filterByCity_returnsMatchingOnly() {
        em.persist(company("Acme Corp", "Windsor"));
        em.persist(company("Beta Inc", "Toronto"));
        em.flush(); em.clear();

        List<CompanyReportRow> result = service.companiesReport("Windsor");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCompanyName()).isEqualTo("Acme Corp");
    }

    // ── internshipTypesReport ─────────────────────────────────────────────────

    @Test
    void internshipTypesReport_noFilter_returnsAllTypes() {
        em.persist(internship("Co-op"));
        em.persist(internship("Part-time"));
        em.flush(); em.clear();

        List<InternshipTypeReportRow> result = service.internshipTypesReport(null);

        assertThat(result).hasSize(2)
                .extracting(InternshipTypeReportRow::getInternshipType)
                .containsExactlyInAnyOrder("Co-op", "Part-time");
    }

    @Test
    void internshipTypesReport_filterByType_returnsMatchingOnly() {
        em.persist(internship("Co-op"));
        em.persist(internship("Part-time"));
        em.flush(); em.clear();

        List<InternshipTypeReportRow> result = service.internshipTypesReport("Co-op");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getInternshipType()).isEqualTo("Co-op");
        assertThat(result.get(0).getStudentCount()).isEqualTo(1L);
    }

    // ── gpaReport ─────────────────────────────────────────────────────────────

    @Test
    void gpaReport_noFilters_returnsAll() {
        em.persist(student(1001, "Alice", "Smith", 2024, "Canada", "Fall", "Active", "Active"));
        em.persist(education(1001, "BSc", "UWindsor", "Windsor, ON"));
        em.flush(); em.clear();

        List<GpaReportRow> result = service.gpaReport(null, null, null, null, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFname()).isEqualTo("Alice");
    }

    @Test
    void gpaReport_filterByYear_returnsMatchingOnly() {
        em.persist(student(1001, "Alice", "Smith", 2024, "Canada", "Fall", "Active", "Active"));
        em.persist(education(1001, "BSc", "UWindsor", "Windsor, ON"));
        em.persist(student(1002, "Bob", "Jones", 2023, "USA", "Winter", "Pending", "Hired"));
        em.persist(education(1002, "MSc", "UofT", "Toronto, ON"));
        em.flush(); em.clear();

        List<GpaReportRow> result = service.gpaReport(2024, null, null, null, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFname()).isEqualTo("Alice");
    }

    @Test
    void gpaReport_filterByDegreeType_returnsMatchingOnly() {
        em.persist(student(1001, "Alice", "Smith", 2024, "Canada", "Fall", "Active", "Active"));
        em.persist(education(1001, "BSc", "UWindsor", "Windsor, ON"));
        em.persist(student(1002, "Bob", "Jones", 2023, "USA", "Winter", "Pending", "Hired"));
        em.persist(education(1002, "MSc", "UofT", "Toronto, ON"));
        em.flush(); em.clear();

        List<GpaReportRow> result = service.gpaReport(null, null, null, null, "MSc");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFname()).isEqualTo("Bob");
    }

    // ── jobsReport ────────────────────────────────────────────────────────────

    @Test
    void jobsReport_noFilter_returnsAllWithApplicantCount() {
        Company c = company("Acme Corp", "Windsor");
        em.persist(c);
        em.flush();

        Job j = job("Software Engineer", c.getCompanyId());
        em.persist(j);
        em.flush();

        StudentJobMapping mapping = new StudentJobMapping();
        mapping.setJobId(j.getJobId());
        mapping.setStudentId(1001);
        em.persist(mapping);
        em.flush(); em.clear();

        List<JobReportRow> result = service.jobsReport(null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getJobPosition()).isEqualTo("Software Engineer");
        assertThat(result.get(0).getApplicantCount()).isEqualTo(1L);
    }

    @Test
    void jobsReport_filterByCompanyId_returnsMatchingOnly() {
        Company c1 = company("Acme Corp", "Windsor");
        em.persist(c1);
        Company c2 = company("Beta Inc", "Toronto");
        em.persist(c2);
        em.flush();

        em.persist(job("Engineer", c1.getCompanyId()));
        em.persist(job("Analyst", c2.getCompanyId()));
        em.flush(); em.clear();

        List<JobReportRow> result = service.jobsReport(c1.getCompanyId());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getJobPosition()).isEqualTo("Engineer");
    }

    // ── getFilters ────────────────────────────────────────────────────────────

    @Test
    void getFilters_returnsDistinctValuesFromAllTables() {
        em.persist(student(1001, "Alice", "Smith", 2024, "Canada", "Fall", "Active", "Active"));
        em.persist(student(1002, "Bob", "Jones", 2024, "USA", "Winter", "Pending", "Hired"));
        em.persist(education(1001, "BSc", "UWindsor", "Windsor, ON"));
        em.persist(education(1002, "MSc", "UofT", "Toronto, ON"));
        em.persist(internship("Co-op"));
        em.flush(); em.clear();

        ReportFiltersResponse filters = service.getFilters();

        assertThat(filters.years()).containsExactly(2024);
        assertThat(filters.countries()).containsExactlyInAnyOrder("Canada", "USA");
        assertThat(filters.universities()).containsExactlyInAnyOrder("UWindsor", "UofT");
        assertThat(filters.universityLocations()).containsExactlyInAnyOrder("Windsor, ON", "Toronto, ON");
        assertThat(filters.internshipTypes()).containsExactly("Co-op");
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private StudentInfo student(int studentId, String fname, String lname, int year,
                                String country, String semester,
                                String internshipStatus, String studentStatus) {
        StudentInfo s = new StudentInfo();
        s.setStudentId(studentId);
        s.setFname(fname);
        s.setLname(lname);
        s.setYear(year);
        s.setCountry(country);
        s.setSemester(semester);
        s.setInternshipStatus(internshipStatus);
        s.setStudentStatus(studentStatus);
        return s;
    }

    private Company company(String name, String city) {
        Company c = new Company();
        c.setCompanyName(name);
        c.setCity(city);
        return c;
    }

    private Job job(String position, Integer companyId) {
        Job j = new Job();
        j.setJobPosition(position);
        j.setCompanyId(companyId);
        return j;
    }

    private StudentInternship internship(String type) {
        StudentInternship si = new StudentInternship();
        si.setInternshipType(type);
        si.setStudentId(1001);
        return si;
    }

    private StudentEducation education(int studentId, String degreeType,
                                       String university, String location) {
        StudentEducation e = new StudentEducation();
        e.setStudentId(studentId);
        e.setDegreeType(degreeType);
        e.setUniversity(university);
        e.setUniversityLocation(location);
        return e;
    }
}
