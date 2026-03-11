package ca.uwindsor.ims.controller;

import ca.uwindsor.ims.dto.*;
import ca.uwindsor.ims.entity.*;
import ca.uwindsor.ims.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    // ── Admin: list all students ────────────────────────────────────────────────

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<StudentInfo> getAll() {
        return service.findAll();
    }

    // ── Admin: create student ──────────────────────────────────────────────────

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public StudentInfo create(@Valid @RequestBody StudentCreateRequest request) {
        return service.create(request);
    }

    // ── Info ───────────────────────────────────────────────────────────────────

    @GetMapping("/{studentId}/info")
    @PreAuthorize("@studentSecurity.canAccess(authentication, #studentId)")
    public ResponseEntity<StudentInfo> getInfo(@PathVariable Integer studentId) {
        return service.findByStudentId(studentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{studentId}/info")
    @PreAuthorize("@studentSecurity.canAccess(authentication, #studentId)")
    public ResponseEntity<StudentInfo> updateInfo(
            @PathVariable Integer studentId,
            @RequestBody StudentInfoRequest request) {
        return service.updateInfo(studentId, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ── Education ──────────────────────────────────────────────────────────────

    @GetMapping("/{studentId}/education")
    @PreAuthorize("@studentSecurity.canAccess(authentication, #studentId)")
    public ResponseEntity<StudentEducation> getEducation(@PathVariable Integer studentId) {
        return service.getEducation(studentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{studentId}/education")
    @PreAuthorize("@studentSecurity.canAccess(authentication, #studentId)")
    public StudentEducation upsertEducation(
            @PathVariable Integer studentId,
            @RequestBody StudentEducationRequest request) {
        return service.upsertEducation(studentId, request);
    }

    // ── Certificate ────────────────────────────────────────────────────────────

    @GetMapping("/{studentId}/certificates")
    @PreAuthorize("@studentSecurity.canAccess(authentication, #studentId)")
    public ResponseEntity<StudentCertificate> getCertificate(@PathVariable Integer studentId) {
        return service.getCertificate(studentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{studentId}/certificates")
    @PreAuthorize("@studentSecurity.canAccess(authentication, #studentId)")
    public StudentCertificate upsertCertificate(
            @PathVariable Integer studentId,
            @RequestBody StudentCertificateRequest request) {
        return service.upsertCertificate(studentId, request);
    }

    // ── Work ───────────────────────────────────────────────────────────────────

    @GetMapping("/{studentId}/work")
    @PreAuthorize("@studentSecurity.canAccess(authentication, #studentId)")
    public ResponseEntity<StudentWork> getWork(@PathVariable Integer studentId) {
        return service.getWork(studentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{studentId}/work")
    @PreAuthorize("@studentSecurity.canAccess(authentication, #studentId)")
    public StudentWork upsertWork(
            @PathVariable Integer studentId,
            @RequestBody StudentWorkRequest request) {
        return service.upsertWork(studentId, request);
    }

    // ── Skills ─────────────────────────────────────────────────────────────────

    @GetMapping("/{studentId}/skills")
    @PreAuthorize("@studentSecurity.canAccess(authentication, #studentId)")
    public List<StudentSkill> getSkills(@PathVariable Integer studentId) {
        return service.getSkills(studentId);
    }

    @PutMapping("/{studentId}/skills")
    @PreAuthorize("@studentSecurity.canAccess(authentication, #studentId)")
    public List<StudentSkill> replaceSkills(
            @PathVariable Integer studentId,
            @RequestBody StudentSkillsRequest request) {
        return service.replaceSkills(studentId, request);
    }
}
