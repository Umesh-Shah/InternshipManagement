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
