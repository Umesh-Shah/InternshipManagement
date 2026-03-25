package ca.uwindsor.ims.service;

import ca.uwindsor.ims.Constants;
import ca.uwindsor.ims.dto.*;
import ca.uwindsor.ims.entity.*;
import ca.uwindsor.ims.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentService.class);

    private final StudentInfoRepository infoRepo;
    private final StudentEducationRepository eduRepo;
    private final StudentCertificateRepository certRepo;
    private final StudentWorkRepository workRepo;
    private final StudentSkillRepository skillRepo;
    private final SkillRepository skillMasterRepo;
    private final LoginRepository loginRepo;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final ApplicationEventPublisher eventPublisher;

    public StudentService(
            StudentInfoRepository infoRepo,
            StudentEducationRepository eduRepo,
            StudentCertificateRepository certRepo,
            StudentWorkRepository workRepo,
            StudentSkillRepository skillRepo,
            SkillRepository skillMasterRepo,
            LoginRepository loginRepo,
            PasswordEncoder passwordEncoder,
            EmailService emailService,
            ApplicationEventPublisher eventPublisher) {
        this.infoRepo = infoRepo;
        this.eduRepo = eduRepo;
        this.certRepo = certRepo;
        this.workRepo = workRepo;
        this.skillRepo = skillRepo;
        this.skillMasterRepo = skillMasterRepo;
        this.loginRepo = loginRepo;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.eventPublisher = eventPublisher;
    }

    /** Internal event fired after a student is persisted, carrying only what email needs. */
    public record StudentRegisteredEvent(String email, String username, String rawPassword, Integer studentId) {}

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onStudentRegistered(StudentRegisteredEvent event) {
        emailService.sendRegistrationEmail(event.email(), event.username(), event.rawPassword(), event.studentId());
    }

    // ── List / find ────────────────────────────────────────────────────────────

    public List<StudentInfo> findAll() {
        return infoRepo.findAll();
    }

    public Optional<StudentInfo> findByStudentId(Integer studentId) {
        return infoRepo.findByStudentId(studentId);
    }

    // ── Create ─────────────────────────────────────────────────────────────────

    /**
     * Creates a new student: saves StudentInfo, creates a Login record, and sends
     * a registration confirmation email. Mirrors the old saveDemo() logic.
     *
     * Username  = part of email before '@'
     * Password  = first 4 chars of email + studentId  (plain-text, wrapped in {noop})
     */
    @Transactional
    public StudentInfo create(StudentCreateRequest req) {
        log.debug("Creating student: id={}, email={}", req.studentId(), req.stuEmail());
        StudentInfo info = new StudentInfo();
        applyCreate(info, req);
        info.setInternshipStatus(Constants.INTERNSHIP_STATUS_PENDING);
        info.setStudentStatus(Constants.STUDENT_STATUS_ACTIVE);
        infoRepo.save(info);

        // Derive credentials from email
        String email = req.stuEmail();
        String username = email.contains("@") ? email.substring(0, email.indexOf('@')) : email;
        String rawPassword = email.substring(0, Math.min(4, email.length())) + req.studentId();
        String encodedPassword = passwordEncoder.encode(rawPassword);

        log.debug("Creating login record: username={}, studentId={}", username, req.studentId());
        Login login = new Login();
        login.setUsername(username);
        login.setPwd(encodedPassword);
        login.setUserType(Constants.USER_TYPE_STUDENT);
        login.setStudentId(req.studentId());
        login.setFlag(Constants.FLAG_ACTIVE);
        loginRepo.save(login);

        eventPublisher.publishEvent(new StudentRegisteredEvent(email, username, rawPassword, req.studentId()));

        return info;
    }

    // ── Info ───────────────────────────────────────────────────────────────────

    public Optional<StudentInfo> updateInfo(Integer studentId, StudentInfoRequest req) {
        log.debug("Updating info for studentId={}", studentId);
        return infoRepo.findByStudentId(studentId).map(info -> {
            applyInfo(info, req);
            return infoRepo.save(info);
        });
    }

    // ── Education ──────────────────────────────────────────────────────────────

    public Optional<StudentEducation> getEducation(Integer studentId) {
        return eduRepo.findByStudentId(studentId);
    }

    public StudentEducation upsertEducation(Integer studentId, StudentEducationRequest req) {
        requireStudentExists(studentId);
        StudentEducation edu = eduRepo.findByStudentId(studentId).orElseGet(StudentEducation::new);
        edu.setStudentId(studentId);
        edu.setDegreeType(req.degreeType());
        edu.setMajor(req.major());
        edu.setDegreeGpa(req.degreeGpa());
        edu.setUniversity(req.university());
        edu.setUniversityLocation(req.universityLocation());
        return eduRepo.save(edu);
    }

    // ── Certificate ────────────────────────────────────────────────────────────

    public Optional<StudentCertificate> getCertificate(Integer studentId) {
        return certRepo.findByStudentId(studentId);
    }

    public StudentCertificate upsertCertificate(Integer studentId, StudentCertificateRequest req) {
        requireStudentExists(studentId);
        StudentCertificate cert = certRepo.findByStudentId(studentId)
                .orElseGet(StudentCertificate::new);
        cert.setStudentId(String.valueOf(studentId));
        cert.setCertificateTitle(req.certificateTitle());
        cert.setCertificateBody(req.certificateBody());
        return certRepo.save(cert);
    }

    // ── Work ───────────────────────────────────────────────────────────────────

    public Optional<StudentWork> getWork(Integer studentId) {
        return workRepo.findByStudentId(studentId);
    }

    public StudentWork upsertWork(Integer studentId, StudentWorkRequest req) {
        requireStudentExists(studentId);
        StudentWork work = workRepo.findByStudentId(studentId).orElseGet(StudentWork::new);
        work.setStudentId(studentId);
        work.setStartDate(req.startDate());
        work.setEndDate(req.endDate());
        work.setCompany(req.company());
        work.setCompanyLocation(req.companyLocation());
        work.setPosition(req.position());
        return workRepo.save(work);
    }

    // ── Skills ─────────────────────────────────────────────────────────────────

    public List<StudentSkill> getSkills(Integer studentId) {
        return skillRepo.findByStudentId(studentId);
    }

    /**
     * Replaces all skill associations for the student. Resolves skill names from
     * the master Skill table (best-effort; unknown skillIds are skipped).
     */
    @Transactional
    public List<StudentSkill> replaceSkills(Integer studentId, StudentSkillsRequest req) {
        log.debug("Replacing skills for studentId={}, count={}", studentId, req.skillIds().size());
        skillRepo.deleteByStudentId(studentId);
        List<StudentSkill> toSave = req.skillIds().stream()
                .map(skillId -> {
                    StudentSkill ss = new StudentSkill();
                    ss.setStudentId(studentId);
                    ss.setSkillId(skillId);
                    skillMasterRepo.findById(skillId)
                            .ifPresent(s -> ss.setSkillName(s.getSkillName()));
                    return ss;
                })
                .toList();
        return skillRepo.saveAll(toSave);
    }

    // ── Guards ─────────────────────────────────────────────────────────────────

    private void requireStudentExists(Integer studentId) {
        if (!infoRepo.existsById(studentId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Student not found: " + studentId);
        }
    }

    // ── Private helpers ────────────────────────────────────────────────────────

    private void applyCreate(StudentInfo info, StudentCreateRequest req) {
        info.setStudentId(req.studentId());
        info.setYear(req.year());
        info.setFname(req.fname());
        info.setLname(req.lname());
        info.setMname(req.mname());
        info.setStuEmail(req.stuEmail());
        info.setStuTelephone(req.stuTelephone());
        info.setGender(req.gender());
        info.setCanadaStatus(req.canadaStatus());
        info.setSemester(req.semester());
        info.setCountry(req.country());
    }

    private void applyInfo(StudentInfo info, StudentInfoRequest req) {
        if (req.year() != null)              info.setYear(req.year());
        if (req.fname() != null)             info.setFname(req.fname());
        if (req.lname() != null)             info.setLname(req.lname());
        if (req.mname() != null)             info.setMname(req.mname());
        if (req.stuEmail() != null)          info.setStuEmail(req.stuEmail());
        if (req.stuTelephone() != null)      info.setStuTelephone(req.stuTelephone());
        if (req.gender() != null)            info.setGender(req.gender());
        if (req.canadaStatus() != null)      info.setCanadaStatus(req.canadaStatus());
        if (req.semester() != null)          info.setSemester(req.semester());
        if (req.internshipStatus() != null)  info.setInternshipStatus(req.internshipStatus());
        if (req.studentStatus() != null)     info.setStudentStatus(req.studentStatus());
        if (req.country() != null)           info.setCountry(req.country());
    }
}
