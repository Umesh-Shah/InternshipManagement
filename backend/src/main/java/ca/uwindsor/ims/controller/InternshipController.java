package ca.uwindsor.ims.controller;

import ca.uwindsor.ims.dto.InternshipStatusRequest;
import ca.uwindsor.ims.dto.InternshipStatusResponse;
import ca.uwindsor.ims.service.InternshipStatusService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/internships")
public class InternshipController {

    private final InternshipStatusService internshipStatusService;

    public InternshipController(InternshipStatusService internshipStatusService) {
        this.internshipStatusService = internshipStatusService;
    }

    @PostMapping("/status")
    @PreAuthorize("hasRole('ADMIN')")
    public InternshipStatusResponse assign(@RequestBody InternshipStatusRequest req) {
        return internshipStatusService.assign(req);
    }

    @GetMapping("/status")
    @PreAuthorize("hasRole('ADMIN')")
    public List<InternshipStatusResponse> getAll() {
        return internshipStatusService.getAll();
    }

    @GetMapping("/status/student/{studentId}")
    @PreAuthorize("@studentSecurity.canAccess(authentication, #studentId)")
    public List<InternshipStatusResponse> getByStudent(@PathVariable Integer studentId) {
        return internshipStatusService.getByStudent(studentId);
    }
}
