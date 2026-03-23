package ca.uwindsor.ims.service;

import ca.uwindsor.ims.dto.StudentCreateRequest;
import ca.uwindsor.ims.entity.Login;
import ca.uwindsor.ims.entity.StudentInfo;
import ca.uwindsor.ims.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock StudentInfoRepository infoRepo;
    @Mock StudentEducationRepository eduRepo;
    @Mock StudentCertificateRepository certRepo;
    @Mock StudentWorkRepository workRepo;
    @Mock StudentSkillRepository skillRepo;
    @Mock SkillRepository skillMasterRepo;
    @Mock LoginRepository loginRepo;
    @Mock PasswordEncoder passwordEncoder;
    @Mock EmailService emailService;
    @Mock ApplicationEventPublisher eventPublisher;

    @InjectMocks StudentService service;

    // ── create() ─────────────────────────────────────────────────────────────

    @Test
    void create_savesStudentInfoWithDefaults() {
        StudentCreateRequest req = request(1001, "alice@uwindsor.ca");
        when(infoRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(loginRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(passwordEncoder.encode(anyString())).thenReturn("{bcrypt}hashed");

        service.create(req);

        ArgumentCaptor<StudentInfo> captor = ArgumentCaptor.forClass(StudentInfo.class);
        verify(infoRepo).save(captor.capture());
        StudentInfo saved = captor.getValue();
        assertThat(saved.getStudentId()).isEqualTo(1001);
        assertThat(saved.getInternshipStatus()).isEqualTo("Pending");
        assertThat(saved.getStudentStatus()).isEqualTo("Active");
    }

    @Test
    void create_derivesLoginCredentialsFromEmail() {
        // username = part before @
        // rawPassword = first min(4, len) chars of full email + studentId
        StudentCreateRequest req = request(1001, "alice@uwindsor.ca");
        when(infoRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(loginRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(passwordEncoder.encode(anyString())).thenReturn("{bcrypt}hashed");

        service.create(req);

        ArgumentCaptor<Login> captor = ArgumentCaptor.forClass(Login.class);
        verify(loginRepo).save(captor.capture());
        Login login = captor.getValue();
        assertThat(login.getUsername()).isEqualTo("alice");
        assertThat(login.getUserType()).isEqualTo("Student");
        assertThat(login.getStudentId()).isEqualTo(1001);
        assertThat(login.getFlag()).isEqualTo("A");

        // rawPassword = "alic" (first 4 chars of "alice@uwindsor.ca") + 1001
        verify(passwordEncoder).encode("alic1001");
    }

    @Test
    void create_emailWithNoAt_usesFullEmailAsUsername() {
        StudentCreateRequest req = request(2001, "boblee");
        when(infoRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(loginRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(passwordEncoder.encode(anyString())).thenReturn("{bcrypt}hashed");

        service.create(req);

        ArgumentCaptor<Login> captor = ArgumentCaptor.forClass(Login.class);
        verify(loginRepo).save(captor.capture());
        assertThat(captor.getValue().getUsername()).isEqualTo("boblee");
        // rawPassword = "bobl" (first 4 of "boblee") + 2001
        verify(passwordEncoder).encode("bobl2001");
    }

    @Test
    void create_shortEmail_usesAvailableCharsForPasswordPrefix() {
        // "ab" has length 2 → min(4,2) = 2 chars used
        StudentCreateRequest req = request(3001, "ab");
        when(infoRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(loginRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(passwordEncoder.encode(anyString())).thenReturn("{bcrypt}hashed");

        service.create(req);

        verify(passwordEncoder).encode("ab3001");
    }

    @Test
    void create_publishesRegistrationEventAfterSave() {
        StudentCreateRequest req = request(1001, "alice@uwindsor.ca");
        when(infoRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(loginRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(passwordEncoder.encode(anyString())).thenReturn("{bcrypt}hashed");

        service.create(req);

        ArgumentCaptor<StudentService.StudentRegisteredEvent> captor =
                ArgumentCaptor.forClass(StudentService.StudentRegisteredEvent.class);
        verify(eventPublisher).publishEvent(captor.capture());
        StudentService.StudentRegisteredEvent event = captor.getValue();
        assertThat(event.email()).isEqualTo("alice@uwindsor.ca");
        assertThat(event.username()).isEqualTo("alice");
        assertThat(event.rawPassword()).isEqualTo("alic1001");
        assertThat(event.studentId()).isEqualTo(1001);
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private StudentCreateRequest request(Integer studentId, String email) {
        return new StudentCreateRequest(
                studentId, 2, "Alice", "Smith", null,
                email, null, null, null, null, null);
    }
}
