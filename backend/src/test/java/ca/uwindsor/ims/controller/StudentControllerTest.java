package ca.uwindsor.ims.controller;

import ca.uwindsor.ims.Constants;
import ca.uwindsor.ims.dto.StudentCreateRequest;
import ca.uwindsor.ims.entity.StudentInfo;
import ca.uwindsor.ims.service.StudentService;
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

import ca.uwindsor.ims.dto.StudentCertificateRequest;
import ca.uwindsor.ims.dto.StudentInfoRequest;
import ca.uwindsor.ims.dto.StudentSkillsRequest;
import ca.uwindsor.ims.dto.StudentWorkRequest;
import ca.uwindsor.ims.entity.StudentCertificate;
import ca.uwindsor.ims.entity.StudentSkill;
import ca.uwindsor.ims.entity.StudentWork;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        value = StudentController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class,
                OAuth2ResourceServerAutoConfiguration.class
        }
)
class StudentControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    @MockBean StudentService studentService;

    // ── GET /api/students ────────────────────────────────────────────────────

    @Test
    void getAll_returnsStudentList() throws Exception {
        StudentInfo s = studentInfo(1001, "Alice", "Smith");
        when(studentService.findAll()).thenReturn(List.of(s));

        mvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].studentId").value(1001))
                .andExpect(jsonPath("$[0].fname").value("Alice"));
    }

    // ── POST /api/students ───────────────────────────────────────────────────

    @Test
    void createStudent_validRequest_returns200() throws Exception {
        StudentCreateRequest req = new StudentCreateRequest(
                2001, 3, "Bob", "Jones", null,
                "bob@example.com", null, null, null, "Fall", "Canada");
        StudentInfo created = studentInfo(2001, "Bob", "Jones");
        when(studentService.create(any())).thenReturn(created);

        mvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value(2001));
    }

    @Test
    void createStudent_missingRequiredFields_returns400() throws Exception {
        // fname is @NotBlank — send empty string
        String body = """
                {"studentId":2002,"fname":"","lname":"Doe","stuEmail":"x@y.com"}
                """;
        mvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createStudent_missingBody_returns400() throws Exception {
        mvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // ── GET /api/students/{id}/info ──────────────────────────────────────────

    @Test
    void getInfo_existingStudent_returns200() throws Exception {
        StudentInfo s = studentInfo(1001, "Carol", "White");
        when(studentService.findByStudentId(1001)).thenReturn(Optional.of(s));

        mvc.perform(get("/api/students/1001/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fname").value("Carol"));
    }

    @Test
    void getInfo_unknownStudent_returns404() throws Exception {
        when(studentService.findByStudentId(9999)).thenReturn(Optional.empty());

        mvc.perform(get("/api/students/9999/info"))
                .andExpect(status().isNotFound());
    }

    // ── GET /api/students/{id}/education ─────────────────────────────────────

    @Test
    void getEducation_unknownStudent_returns404() throws Exception {
        when(studentService.getEducation(9999)).thenReturn(Optional.empty());

        mvc.perform(get("/api/students/9999/education"))
                .andExpect(status().isNotFound());
    }

    // ── PUT /api/students/{id}/info ──────────────────────────────────────────

    @Test
    void updateInfo_validRequest_returns200() throws Exception {
        StudentInfo updated = studentInfo(1001, "Carol", "White");
        when(studentService.updateInfo(eq(1001), any())).thenReturn(Optional.of(updated));

        mvc.perform(put("/api/students/1001/info")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(
                        new StudentInfoRequest(null, "Carol", null, null, null, null, null, null, null, null, null, null))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fname").value("Carol"));
    }

    @Test
    void updateInfo_unknownStudent_returns404() throws Exception {
        when(studentService.updateInfo(eq(9999), any())).thenReturn(Optional.empty());

        mvc.perform(put("/api/students/9999/info")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isNotFound());
    }

    // ── GET /api/students/{id}/work ──────────────────────────────────────────

    @Test
    void getWork_returns200() throws Exception {
        StudentWork work = new StudentWork();
        work.setStudentId(1001);
        work.setCompany("Acme");
        when(studentService.getWork(1001)).thenReturn(Optional.of(work));

        mvc.perform(get("/api/students/1001/work"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.company").value("Acme"));
    }

    @Test
    void updateWork_validRequest_returns200() throws Exception {
        StudentWork work = new StudentWork();
        work.setStudentId(1001);
        work.setPosition("Intern");
        when(studentService.upsertWork(eq(1001), any())).thenReturn(work);

        mvc.perform(put("/api/students/1001/work")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(
                        new StudentWorkRequest("2024-01", "2024-04", "Acme", "Windsor", "Intern"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.position").value("Intern"));
    }

    // ── GET /api/students/{id}/certificates ──────────────────────────────────

    @Test
    void getCertificates_returns200() throws Exception {
        StudentCertificate cert = new StudentCertificate();
        cert.setCertificateTitle("AWS Dev");
        when(studentService.getCertificate(1001)).thenReturn(Optional.of(cert));

        mvc.perform(get("/api/students/1001/certificates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.certificateTitle").value("AWS Dev"));
    }

    @Test
    void updateCertificates_validRequest_returns200() throws Exception {
        StudentCertificate cert = new StudentCertificate();
        cert.setCertificateTitle("AWS Dev");
        when(studentService.upsertCertificate(eq(1001), any())).thenReturn(cert);

        mvc.perform(put("/api/students/1001/certificates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(
                        new StudentCertificateRequest("AWS Dev", "Body text"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.certificateTitle").value("AWS Dev"));
    }

    // ── GET /api/students/{id}/skills ────────────────────────────────────────

    @Test
    void getSkills_returns200() throws Exception {
        StudentSkill ss = new StudentSkill();
        ss.setSkillId(5);
        ss.setSkillName("Java");
        when(studentService.getSkills(1001)).thenReturn(List.of(ss));

        mvc.perform(get("/api/students/1001/skills"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].skillName").value("Java"));
    }

    @Test
    void updateSkills_validRequest_returns200() throws Exception {
        StudentSkill ss = new StudentSkill();
        ss.setSkillId(5);
        ss.setSkillName("Java");
        when(studentService.replaceSkills(eq(1001), any())).thenReturn(List.of(ss));

        mvc.perform(put("/api/students/1001/skills")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new StudentSkillsRequest(List.of(5)))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].skillName").value("Java"));
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private StudentInfo studentInfo(Integer id, String fname, String lname) {
        StudentInfo s = new StudentInfo();
        s.setStudentId(id);
        s.setFname(fname);
        s.setLname(lname);
        s.setStuEmail(fname.toLowerCase() + "@example.com");
        s.setInternshipStatus(Constants.INTERNSHIP_STATUS_PENDING);
        s.setStudentStatus(Constants.STUDENT_STATUS_ACTIVE);
        return s;
    }
}
