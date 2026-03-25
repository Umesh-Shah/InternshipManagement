package ca.uwindsor.ims.service;

import ca.uwindsor.ims.Constants;
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

import ca.uwindsor.ims.dto.StudentCertificateRequest;
import ca.uwindsor.ims.dto.StudentEducationRequest;
import ca.uwindsor.ims.dto.StudentInfoRequest;
import ca.uwindsor.ims.dto.StudentSkillsRequest;
import ca.uwindsor.ims.dto.StudentWorkRequest;
import ca.uwindsor.ims.entity.Skill;
import ca.uwindsor.ims.entity.StudentCertificate;
import ca.uwindsor.ims.entity.StudentEducation;
import ca.uwindsor.ims.entity.StudentSkill;
import ca.uwindsor.ims.entity.StudentWork;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
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
        assertThat(saved.getInternshipStatus()).isEqualTo(Constants.INTERNSHIP_STATUS_PENDING);
        assertThat(saved.getStudentStatus()).isEqualTo(Constants.STUDENT_STATUS_ACTIVE);
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
        assertThat(login.getUserType()).isEqualTo(Constants.USER_TYPE_STUDENT);
        assertThat(login.getStudentId()).isEqualTo(1001);
        assertThat(login.getFlag()).isEqualTo(Constants.FLAG_ACTIVE);

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

    // ── findAll / findByStudentId ─────────────────────────────────────────────

    @Test
    void findAll_delegatesToRepo() {
        StudentInfo s = new StudentInfo();
        s.setStudentId(1001);
        when(infoRepo.findAll()).thenReturn(List.of(s));

        List<StudentInfo> result = service.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStudentId()).isEqualTo(1001);
        verify(infoRepo).findAll();
    }

    @Test
    void findByStudentId_returnsEntity() {
        StudentInfo s = new StudentInfo();
        s.setStudentId(1001);
        when(infoRepo.findByStudentId(1001)).thenReturn(Optional.of(s));

        Optional<StudentInfo> result = service.findByStudentId(1001);

        assertThat(result).isPresent();
        assertThat(result.get().getStudentId()).isEqualTo(1001);
    }

    // ── getEducation / getWork / getCertificate / getSkills ───────────────────

    @Test
    void getEducation_returnsOptional() {
        StudentEducation edu = new StudentEducation();
        edu.setStudentId(1001);
        when(eduRepo.findByStudentId(1001)).thenReturn(Optional.of(edu));

        assertThat(service.getEducation(1001)).isPresent();
    }

    @Test
    void getWork_returnsOptional() {
        StudentWork work = new StudentWork();
        work.setStudentId(1001);
        when(workRepo.findByStudentId(1001)).thenReturn(Optional.of(work));

        assertThat(service.getWork(1001)).isPresent();
    }

    @Test
    void getCertificate_returnsOptional() {
        StudentCertificate cert = new StudentCertificate();
        when(certRepo.findByStudentId(1001)).thenReturn(Optional.of(cert));

        assertThat(service.getCertificate(1001)).isPresent();
    }

    @Test
    void getSkills_returnsList() {
        StudentSkill ss = new StudentSkill();
        ss.setStudentId(1001);
        when(skillRepo.findByStudentId(1001)).thenReturn(List.of(ss));

        List<StudentSkill> result = service.getSkills(1001);

        assertThat(result).hasSize(1);
    }

    // ── updateInfo ────────────────────────────────────────────────────────────

    @Test
    void updateInfo_mapsAndSaves() {
        StudentInfo existing = new StudentInfo();
        existing.setStudentId(1001);
        existing.setFname("Old");
        when(infoRepo.findByStudentId(1001)).thenReturn(Optional.of(existing));
        when(infoRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        StudentInfoRequest req = new StudentInfoRequest(
                null, "New", null, null, null, null, null, null, null, null, null, null);
        Optional<StudentInfo> result = service.updateInfo(1001, req);

        assertThat(result).isPresent();
        assertThat(result.get().getFname()).isEqualTo("New");
    }

    // ── upsertEducation ───────────────────────────────────────────────────────

    @Test
    void updateEducation_mapsAndSaves() {
        when(infoRepo.existsById(1001)).thenReturn(true);
        when(eduRepo.findByStudentId(1001)).thenReturn(Optional.empty());
        when(eduRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        StudentEducationRequest req = new StudentEducationRequest("BSc", "CS", "3.9", "UWindsor", "Windsor, ON");
        StudentEducation result = service.upsertEducation(1001, req);

        assertThat(result.getDegreeType()).isEqualTo("BSc");
        assertThat(result.getMajor()).isEqualTo("CS");
        assertThat(result.getStudentId()).isEqualTo(1001);
    }

    // ── upsertWork ────────────────────────────────────────────────────────────

    @Test
    void updateWork_mapsAndSaves() {
        when(infoRepo.existsById(1001)).thenReturn(true);
        when(workRepo.findByStudentId(1001)).thenReturn(Optional.empty());
        when(workRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        StudentWorkRequest req = new StudentWorkRequest("2024-01", "2024-04", "Acme", "Windsor", "Intern");
        StudentWork result = service.upsertWork(1001, req);

        assertThat(result.getCompany()).isEqualTo("Acme");
        assertThat(result.getPosition()).isEqualTo("Intern");
        assertThat(result.getStudentId()).isEqualTo(1001);
    }

    // ── upsertCertificate ─────────────────────────────────────────────────────

    @Test
    void updateCertificates_mapsAndSaves() {
        when(infoRepo.existsById(1001)).thenReturn(true);
        when(certRepo.findByStudentId(1001)).thenReturn(Optional.empty());
        when(certRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        StudentCertificateRequest req = new StudentCertificateRequest("AWS Dev", "Some body text");
        StudentCertificate result = service.upsertCertificate(1001, req);

        assertThat(result.getCertificateTitle()).isEqualTo("AWS Dev");
    }

    // ── replaceSkills ─────────────────────────────────────────────────────────

    @Test
    void updateSkills_mapsAndSaves() {
        Skill masterSkill = new Skill();
        masterSkill.setSkillId(5);
        masterSkill.setSkillName("Java");
        when(skillMasterRepo.findById(5)).thenReturn(Optional.of(masterSkill));

        service.replaceSkills(1001, new StudentSkillsRequest(List.of(5)));

        verify(skillRepo).deleteByStudentId(1001);
        verify(skillRepo).saveAll(any());
    }

    @Test
    void upsertEducation_unknownStudent_throws404() {
        when(infoRepo.existsById(9999)).thenReturn(false);

        assertThatThrownBy(() -> service.upsertEducation(9999,
                new StudentEducationRequest("BSc", "CS", "3.9", "U", "City")))
                .isInstanceOf(ResponseStatusException.class);
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private StudentCreateRequest request(Integer studentId, String email) {
        return new StudentCreateRequest(
                studentId, 2, "Alice", "Smith", null,
                email, null, null, null, null, null);
    }
}
